\c postgres postgres
DROP DATABASE IF EXISTS mood;
DROP USER IF EXISTS mooduser;
CREATE USER mooduser WITH PASSWORD 'password';
CREATE DATABASE mood;
GRANT ALL PRIVILEGES ON DATABASE mood TO mooduser;
\c mood mooduser


--DROP TABLE IF EXISTS ATTENDEE CASCADE;
--DROP TABLE IF EXISTS HOST CASCADE;
--DROP TABLE IF EXISTS SERIES CASCADE;
--DROP TABLE IF EXISTS EVENTS CASCADE;
--DROP TABLE IF EXISTS EVENTFORMS CASCADE;
--DROP TABLE IF EXISTS FORMS CASCADE;
--DROP TABLE IF EXISTS QUESTIONS CASCADE;
--DROP TABLE IF EXISTS ANSWERS CASCADE;
--DROP TABLE IF EXISTS MOOD CASCADE;
--DROP FUNCTION IF EXISTS CreateFeedback CASCADE;
--DROP FUNCTION IF EXISTS DeleteSeries CASCADE;

CREATE TABLE ATTENDEE (
  AttendeeID INTEGER PRIMARY KEY,
  Email VARCHAR(50),
  Pass VARCHAR(32),
  AccountName VARCHAR(32),
  Expires TIMESTAMP
);

CREATE TABLE HOST (
  HostID INTEGER PRIMARY KEY,
  Email VARCHAR(50) NOT NULL UNIQUE CHECK (CHAR_LENGTH(Email) > 0),
  Pass VARCHAR(32) NOT NULL CHECK (CHAR_LENGTH(Pass) > 0),
  AccountName VARCHAR(32) NOT NULL CHECK (CHAR_LENGTH(AccountName) > 0)
);

CREATE TABLE SERIES (
  SeriesID INTEGER PRIMARY KEY,
  HostID INTEGER NOT NULL,
  Title VARCHAR(30) NOT NULL CHECK (CHAR_LENGTH(Title) > 0),
  Description VARCHAR(140),
  CONSTRAINT fk_HostID
    FOREIGN KEY(HostID)
      REFERENCES HOST(HostID) ON DELETE CASCADE
);

CREATE TABLE EVENTS (
  EventID INTEGER PRIMARY KEY,
  SeriesID INTEGER NOT NULL,
  Title VARCHAR(30) NOT NULL CHECK (CHAR_LENGTH(Title) > 0),
  Description VARCHAR(140),
  TimeStart TIMESTAMP,
  TimeEnd TIMESTAMP,
  InviteCode VARCHAR(10) UNIQUE,
  CONSTRAINT fk_SeriesID
    FOREIGN KEY(SeriesID)
      REFERENCES SERIES(SeriesID) ON DELETE CASCADE
);

CREATE TABLE FORMS (
  FormID INTEGER PRIMARY KEY,
  HostID INTEGER NOT NULL,
  Title VARCHAR(30) NOT NULL CHECK (CHAR_LENGTH(Title) > 0),
  Description VARCHAR(140),
  CONSTRAINT fk_HostID
    FOREIGN KEY(HostID)
      REFERENCES HOST(HostID) ON DELETE CASCADE
);

CREATE TABLE EVENTFORMS (
  EventFormID INTEGER PRIMARY KEY,
  EventID INTEGER NOT NULL,
  FormID INTEGER NOT NULL,
  NumInEvent INTEGER NOT NULL DEFAULT 1 CHECK(NumInEvent >= 0),
  TimeStart TIMESTAMP,
  TimeEnd TIMESTAMP,
  IsActive BOOLEAN NOT NULL DEFAULT FALSE,
  CONSTRAINT fk_EventID
    FOREIGN KEY(EventID)
      REFERENCES EVENTS(EventID) ON DELETE CASCADE,
  CONSTRAINT fk_FormID
    FOREIGN KEY(FormID)
      REFERENCES FORMS(FormID) ON DELETE CASCADE
);

CREATE TABLE QUESTIONS (
  QuestionID INTEGER PRIMARY KEY,
  Type VARCHAR(8) NOT NULL CHECK (Type = 'long' OR Type = 'short' OR Type = 'multi' OR Type = 'rating'),
  FormID INTEGER NOT NULL,
  NumInForm INTEGER NOT NULL CHECK (NumInForm > 0),
  Content VARCHAR(70),
  Options VARCHAR(70),
  CONSTRAINT fk_FormID
    FOREIGN KEY(FormID)
      REFERENCES FORMS(FormID) ON DELETE CASCADE
);

-- assuming we have 5 emojis, Value is between 0 and 4
CREATE TABLE MOOD (
  MoodID INTEGER PRIMARY KEY,
  EventID INTEGER NOT NULL,
  Value DOUBLE PRECISION NOT NULL CHECK (Value >= -1), CHECK (Value <= 1),
  TimeSubmitted TIMESTAMP,
  CONSTRAINT fk_EventID
    FOREIGN KEY(EventID)
      REFERENCES EVENTS(EventID) ON DELETE CASCADE
);


CREATE TABLE ANSWERS (
  AnswerID INTEGER PRIMARY KEY,
  QuestionID INTEGER NOT NULL,
  AttendeeID INTEGER NOT NULL,
  EventFormID INTEGER NOT NULL,
  MoodID  INTEGER NOT NULL,
  IsEdited BOOLEAN NOT NULL DEFAULT FALSE,
  IsAnonymous BOOLEAN NOT NULL DEFAULT FALSE,
  TimeSubmitted TIMESTAMP,
  Response VARCHAR(100) NOT NULL CHECK (CHAR_LENGTH(Response) > 0),
  CONSTRAINT fk_QuestionID
    FOREIGN KEY(QuestionID)
      REFERENCES QUESTIONS(QuestionID) ON DELETE CASCADE,
  CONSTRAINT fk_EventFormID
    FOREIGN KEY(EventFormID)
      REFERENCES EVENTFORMS(EventFormID) ON DELETE CASCADE,
  CONSTRAINT fk_AttendeeID
    FOREIGN KEY(AttendeeID)
      REFERENCES ATTENDEE(AttendeeID) ON DELETE CASCADE,
  CONSTRAINT fk_MoodID
    FOREIGN KEY(MoodID)
      REFERENCES MOOD(MoodID) ON DELETE CASCADE
);

CREATE TABLE REGISTEREVENTS (
  AttendeeID INTEGER NOT NULL,
  EventID INTEGER NOT NULL,
  CONSTRAINT fk_AttendeeID
    FOREIGN KEY(AttendeeID)
      REFERENCES ATTENDEE(AttendeeID) ON DELETE CASCADE,
  CONSTRAINT fk_EventID
    FOREIGN KEY(EventID)
      REFERENCES EVENTS(EventID) ON DELETE CASCADE
);

-- define a sequence to generate attendee ids
CREATE SEQUENCE AttendeesAttendeeID
START 1
INCREMENT 1
MINVALUE 1
OWNED BY ATTENDEE.AttendeeID;

-- define a sequence to generate host ids
CREATE SEQUENCE HostsHostID
START 1
INCREMENT 1
MINVALUE 1
OWNED BY HOST.HostID;

-- define a sequence to generate question ids
CREATE SEQUENCE QuestionsQuestionID
START 1
INCREMENT 1
MINVALUE 1
OWNED BY QUESTIONS.QuestionID;

-- define a sequence to generate series ids
CREATE SEQUENCE SeriesSeriesID
START 1
INCREMENT 1
MINVALUE 1
OWNED BY SERIES.SeriesID;

-- define a sequence to generate event ids
CREATE SEQUENCE EventsEventID
START 1
INCREMENT 1
MINVALUE 1
OWNED BY EVENTS.EventID;

-- define a sequence to generate eventform ids
CREATE SEQUENCE EventFormsEventFormID
START 1
INCREMENT 1
MINVALUE 1
OWNED BY EVENTFORMS.EventFormID;

-- define a sequence to generate form ids
CREATE SEQUENCE FormsFormID
START 1
INCREMENT 1
MINVALUE 1
OWNED BY FORMS.FormID;

-- define a sequence to generate mood ids
CREATE SEQUENCE MoodsMoodID
START 1
INCREMENT 1
MINVALUE 1
OWNED BY MOOD.MoodID;

-- define a sequence to generate answer ids
CREATE SEQUENCE AnswersAnswerID
START 1
INCREMENT 1
MINVALUE 1
OWNED BY ANSWERS.AnswerID;

CREATE FUNCTION CreateFeedback() RETURNS TRIGGER AS $CreateFeedback$
  BEGIN
    INSERT INTO EVENTFORMS(EventFormID, EventID, FormID, NumInEvent, TimeStart, TimeEnd, IsActive)
    -- is general feedback always active?
    VALUES (nextval('EventFormsEventFormID'), NEW.EventID, 0, 0, NEW.TimeStart, NEW.TimeEnd, TRUE);
    RETURN NEW;
  END;
$CreateFeedback$ LANGUAGE plpgsql;

CREATE TRIGGER CreateFeedback
  AFTER INSERT ON EVENTS
  FOR EACH ROW
  EXECUTE FUNCTION CreateFeedback();

CREATE FUNCTION ShiftQuestions() RETURNS TRIGGER AS $ShiftQuestions$
  BEGIN
    IF tg_op = 'UPDATE' THEN
      IF NEW.NumInForm > OLD.NumInForm THEN
        UPDATE QUESTIONS
        SET NumInForm = NumInForm - 1
        WHERE NumInForm <= NEW.NumInForm
        AND NumInForm > OLD.NumInForm
        AND FormID = NEW.FormID
        AND QuestionID != NEW.QuestionID;
      ELSE
        UPDATE QUESTIONS
        SET NumInForm = NumInForm + 1
        WHERE NumInForm >= NEW.NumInForm
        AND NumInForm < OLD.NumInForm
        AND FormID = NEW.FormID
        AND QuestionID != NEW.QuestionID;
      END IF;
    ELSIF tg_op = 'INSERT' THEN
      UPDATE QUESTIONS
      SET NumInForm = NumInForm + 1
      WHERE NumInForm >= NEW.NumInForm
      AND FormID = NEW.FormID
      AND QuestionID != NEW.QuestionID;
    ELSIF tg_op = 'DELETE' THEN
      UPDATE QUESTIONS
      SET NumInForm = NumInForm - 1
      WHERE NumInForm > OLD.NumInForm
      AND FormID = OLD.FormID
      AND QuestionID != OLD.QuestionID;
      RETURN OLD;
    END IF;
    RETURN NEW;
  END;
$ShiftQuestions$ LANGUAGE plpgsql;

CREATE TRIGGER ShiftQuestions
  BEFORE INSERT OR DELETE OR UPDATE OF NumInForm ON QUESTIONS
  FOR EACH ROW WHEN (pg_trigger_depth() = 0)
  EXECUTE FUNCTION ShiftQuestions();

CREATE FUNCTION ShiftEventForms() RETURNS TRIGGER AS $ShiftEventForms$
  BEGIN
    IF tg_op = 'UPDATE' THEN
      IF NEW.NumInEvent > OLD.NumInEvent THEN
        UPDATE EVENTFORMS
        SET NumInEvent = NumInEvent - 1
        WHERE NumInEvent <= NEW.NumInEvent
        AND NumInEvent > OLD.NumInEvent
        AND EventID = NEW.EventID
        AND EventFormID != NEW.EventFormID;
      ELSE
        UPDATE EVENTFORMS
        SET NumInEvent = NumInEvent + 1
        WHERE NumInEvent >= NEW.NumInEvent
        AND NumInEvent < OLD.NumInEvent
        AND EventID = NEW.EventID
        AND EventFormID != NEW.EventFormID;
      END IF;
    ELSIF tg_op = 'INSERT' THEN
      UPDATE EVENTFORMS
      SET NumInEvent = NumInEvent + 1
      WHERE NumInEvent >= NEW.NumInEvent
      AND EventID = NEW.EventID
      AND EventFormID != NEW.EventFormID;
    ELSIF tg_op = 'DELETE' THEN
      UPDATE EVENTFORMS
      SET NumInEvent = NumInEvent - 1
      WHERE NumInEvent > OLD.NumInEvent
      AND EventID = OLD.EventID
      AND EventFormID != OLD.EventFormID;
      RETURN OLD;
    END IF;
    RETURN NEW;
  END;
$ShiftEventForms$ LANGUAGE plpgsql;

CREATE TRIGGER ShiftEventForms
  BEFORE INSERT OR DELETE OR UPDATE OF NumInEvent ON EVENTFORMS
  FOR EACH ROW WHEN (pg_trigger_depth() = 0)
  EXECUTE FUNCTION ShiftEventForms();

CREATE FUNCTION GetInviteCode() RETURNS TRIGGER AS $GetInviteCode$
  DECLARE
    IC VARCHAR(10);
  BEGIN
    LOOP
      IC := UPPER(SUBSTRING(MD5(''||NOW()::TEXT||RANDOM()::TEXT) FOR 8));
      BEGIN
        UPDATE EVENTS
        SET InviteCode = IC
        WHERE EVENTS.EventID = NEW.EventID;
        EXIT;
      EXCEPTION WHEN unique_violation THEN
      END;
    END LOOP;
    RETURN NEW;
  END;
$GetInviteCode$ LANGUAGE plpgsql;

CREATE TRIGGER GetInviteCode
  AFTER INSERT ON EVENTS
  FOR EACH ROW
  EXECUTE FUNCTION GetInviteCode();
--uses md5 which is unsecure but should be good for this purpose

INSERT INTO HOST(HostID, Email, Pass, AccountName)
VALUES(0,'default@mail.com','password','generalhost');

INSERT INTO FORMS(FormID, HostID, Title, Description)
VALUES(0,0,'General Feedback', 'Form for general feedback');

INSERT INTO QUESTIONS
VALUES(0,'long',0,1,'General Feedback');

INSERT INTO FORMS(FormID, HostID, Title, Description)
VALUES(nextval('FormsFormID'),0,'Beginning of event','Template for beginning of events');

INSERT INTO QUESTIONS
VALUES(nextval('QuestionsQuestionID'),'multi',1,1,'Can you hear everything correctly?', '["Yes","No"]');

INSERT INTO QUESTIONS
VALUES(nextval('QuestionsQuestionID'),'multi',1,2,'Can you see everything correctly?', '["Yes","No"]');

INSERT INTO FORMS(FormID, HostID, Title, Description)
VALUES(nextval('FormsFormID'),0,'Middle of event','Template for middle of events');

INSERT INTO QUESTIONS
VALUES(nextval('QuestionsQuestionID'),'short',2,1,'How do you feel about the presentation so far?');

INSERT INTO QUESTIONS
VALUES(nextval('QuestionsQuestionID'),'long',2,2,'Is there anything you would like to mention before continuing?');

INSERT INTO FORMS(FormID, HostID, Title, Description)
VALUES(nextval('FormsFormID'),0,'End of event','Template for end of events');

INSERT INTO QUESTIONS
VALUES(nextval('QuestionsQuestionID'),'rating',3,1,'How would you rate this event?');

INSERT INTO QUESTIONS
VALUES(nextval('QuestionsQuestionID'),'long',3,2,'What could be improved?');

INSERT INTO FORMS(FormID, HostID, Title, Description)
VALUES(nextval('FormsFormID'),0,'Q&A Template','Template for asking questions');

INSERT INTO QUESTIONS
VALUES(nextval('QuestionsQuestionID'),'long',4,1,'Do you have any questions?');

INSERT INTO FORMS(FormID, HostID, Title, Description)
VALUES(nextval('FormsFormID'),0,'Review/Demo Template','Template for reviewing something');

INSERT INTO QUESTIONS
VALUES(nextval('QuestionsQuestionID'),'long',5,1,'What could be improved?');

INSERT INTO QUESTIONS
VALUES(nextval('QuestionsQuestionID'),'rating',5,2,'How would you rate this?');

INSERT INTO FORMS(FormID, HostID, Title, Description)
VALUES(nextval('FormsFormID'),0,'Project Template','Template for questions and feelings on a project');

INSERT INTO QUESTIONS
VALUES(nextval('QuestionsQuestionID'),'long',6,1,'Is there anything you do not understand or would like to mention?');

INSERT INTO QUESTIONS
VALUES(nextval('QuestionsQuestionID'),'long',6,2,'How do you feel about the project?');

INSERT INTO SERIES
VALUES(0,0,'Moodless');

INSERT INTO EVENTS
VALUES(0,0,'Moodless');

INSERT INTO MOOD
VALUES(0,0,0.0);
