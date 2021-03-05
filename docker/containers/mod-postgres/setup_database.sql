\c postgres postgres
DROP DATABASE IF EXISTS mood;
DROP USER IF EXISTS mooduser;
CREATE USER mooduser WITH PASSWORD 'password';
CREATE DATABASE mood;
GRANT ALL PRIVILEGES ON DATABASE mood TO mooduser;
\c mood mooduser


DROP TABLE IF EXISTS ATTENDEE CASCADE;
DROP TABLE IF EXISTS HOST CASCADE;
DROP TABLE IF EXISTS SERIES CASCADE;
DROP TABLE IF EXISTS EVENTS CASCADE;
DROP TABLE IF EXISTS EVENTFORMS CASCADE;
DROP TABLE IF EXISTS FORMS CASCADE;
DROP TABLE IF EXISTS QUESTIONS CASCADE;
DROP TABLE IF EXISTS ANSWERS CASCADE;
DROP TABLE IF EXISTS MOOD CASCADE;
DROP FUNCTION IF EXISTS CreateFeedback CASCADE;
DROP FUNCTION IF EXISTS DeleteSeries CASCADE;

CREATE TABLE ATTENDEE (
  AttendeeID INTEGER PRIMARY KEY,
  Email VARCHAR(50),
  Pass VARCHAR(32),
  AccountName VARCHAR(32),
  Expires TIMESTAMP
);

CREATE TABLE HOST (
  HostID INTEGER PRIMARY KEY,
  Email VARCHAR(50) NOT NULL CHECK (CHAR_LENGTH(Email) > 0),
  Pass VARCHAR(32) NOT NULL CHECK (CHAR_LENGTH(Pass) > 0),
  AccountName VARCHAR(32)
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
  EventInterval INTERVAL NOT NULL,
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
  Value FLOAT,
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

INSERT INTO HOST(HostID, Email, Pass, AccountName)
VALUES(0,'default@mail.com','password','generalhost');

INSERT INTO FORMS(FormID, HostID, Title, Description)
VALUES(0,0,'General Feedback', 'Form for general feedback');

CREATE FUNCTION CreateFeedback() RETURNS TRIGGER AS $CreateFeedback$
  BEGIN
    INSERT INTO EVENTFORMS(EventFormID, EventID, FormID, NumInEvent, IsActive)
    -- is general feedback always active?
    VALUES (nextval('EventFormsEventFormID'), NEW.EventID, 0, 0, TRUE);
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
