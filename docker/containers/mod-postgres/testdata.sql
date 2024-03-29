--TEST DATA

-- multi & rating question types are the same?
-- Replace interval in events by start/end time?

INSERT INTO ATTENDEE VALUES (nextval('AttendeesAttendeeID'),'john@gmail.com','John', 'John', '2021-05-23 12:00:00');
INSERT INTO ATTENDEE VALUES (nextval('AttendeesAttendeeID'),'louisbechu@gmail.com','CoolCat223', 'LB', '2023-06-06 15:34:20');
INSERT INTO ATTENDEE VALUES (nextval('AttendeesAttendeeID'),'jude@gmail.com','xdaA!*13254', 'J.S.', '2021-01-03 10:11:59');
INSERT INTO ATTENDEE VALUES (nextval('AttendeesAttendeeID'),'andrew@gmail.com','azertyuiop', 'Andy', '2022-02-22 23:40:10');
INSERT INTO ATTENDEE VALUES (nextval('AttendeesAttendeeID'),'prithu@yahoo.com','Mississippi12', 'Prithu', '2027-01-01 00:05:33');
INSERT INTO ATTENDEE VALUES (nextval('AttendeesAttendeeID'),'thomas@warwick.ac.uk','1298', 'Peach', '2019-02-01 10:04:13');
--
INSERT INTO HOST VALUES (nextval('HostsHostID'),'erik@gmail.com','__ROAR__', 'Erik');
INSERT INTO HOST VALUES (nextval('HostsHostID'),'marie@gmail.com','AjeXXoeS_!*$edS', 'Marie1');
INSERT INTO HOST VALUES (nextval('HostsHostID'),'zhang@gmail.com','MeowMeow', 'ZZ');
INSERT INTO HOST VALUES (nextval('HostsHostID'),'drake234@gmail.com','1234567890', 'Drake');
INSERT INTO HOST VALUES (nextval('HostsHostID'),'alexandra@yahoo.com','3àd!*zeh&', 'Alex Yahoo');
INSERT INTO HOST VALUES (nextval('HostsHostID'),'Johnathan.Fernando@warwick.ac.uk','Cars Cannot Speak Spanish', 'JF');
--
INSERT INTO SERIES VALUES (nextval('SeriesSeriesID'), 1, 'SE Project', 'Software Engineering Project on Cybersecurity');
INSERT INTO SERIES VALUES (nextval('SeriesSeriesID'), 6, 'Workshop AI', '');
INSERT INTO SERIES VALUES (nextval('SeriesSeriesID'), 4, 'Protype Mood Analysis', '');
INSERT INTO SERIES VALUES (nextval('SeriesSeriesID'), 4, 'FDB', 'Finance Deutsche Bank');
INSERT INTO SERIES VALUES (nextval('SeriesSeriesID'), 3, 'DB Project', 'Deutsche Bank Project on Work Ethics');
INSERT INTO SERIES VALUES (nextval('SeriesSeriesID'), 2, 'New Tech', '');
INSERT INTO SERIES VALUES (nextval('SeriesSeriesID'), 5, 'Test', 'Test series');
--
INSERT INTO EVENTS VALUES (nextval('EventsEventID'), 1, 'Requirements', 'First meeting for the project planning', TIMESTAMP '2020-1-11 13:30:0', TIMESTAMP '2020-1-11 13:30:0' + INTERVAL '45 minutes');
INSERT INTO EVENTS VALUES (nextval('EventsEventID'), 1, 'Presentation 1', 'First prototype presentation', TIMESTAMP '2021-6-14 13:30:0', TIMESTAMP '2021-6-14 13:30:0' + INTERVAL '45 minutes');
INSERT INTO EVENTS VALUES (nextval('EventsEventID'), 1, 'Presentation 2', 'Second prototype presentation', TIMESTAMP '2021-4-16 17:0:0', TIMESTAMP '2021-4-16 17:0:0' + INTERVAL '120 minutes');
INSERT INTO EVENTS VALUES (nextval('EventsEventID'), 2, 'Workshop AI', 'Workshop on how AI can help DB', TIMESTAMP '2020-7-12 14:30:0', TIMESTAMP '2020-7-12 14:30:0' + INTERVAL '60 minutes');
INSERT INTO EVENTS VALUES (nextval('EventsEventID'), 3, 'Prototype Q&A', 'Questions about the prototype can be asked', TIMESTAMP '2021-10-8 11:0:0', TIMESTAMP '2021-10-8 11:0:0' + INTERVAL '90 minutes');
INSERT INTO EVENTS VALUES (nextval('EventsEventID'), 4, 'Finance Plan', 'DB Finance Plan Presentation', TIMESTAMP '2020-9-4 15:0:0', TIMESTAMP '2020-9-4 15:0:0' + INTERVAL '30 minutes');
INSERT INTO EVENTS VALUES (nextval('EventsEventID'), 5, 'Project Meeting 1', 'DB Project Methodology', TIMESTAMP '2021-10-22 14:0:0', TIMESTAMP '2021-10-22 14:0:0' + INTERVAL '60 minutes');
INSERT INTO EVENTS VALUES (nextval('EventsEventID'), 5, 'Project Meeting 2', 'DB Project Sprint 1', TIMESTAMP '2021-2-25 16:30:0', TIMESTAMP '2021-2-25 16:30:0' + INTERVAL '90 minutes');
INSERT INTO EVENTS VALUES (nextval('EventsEventID'), 5, 'Client Q&A', 'Q&A about Project', TIMESTAMP '2021-12-25 12:0:0', TIMESTAMP '2021-12-25 12:0:0' + INTERVAL '120 minutes');
INSERT INTO EVENTS VALUES (nextval('EventsEventID'), 6, 'Lecture 1', 'Lecture on Econometrics', TIMESTAMP '2020-5-15 10:0:0', TIMESTAMP '2020-5-15 10:0:0' + INTERVAL '90 minutes');
INSERT INTO EVENTS VALUES (nextval('EventsEventID'), 6, 'Lecture 2', 'Lecture on the Stock Market', TIMESTAMP '2021-1-17 14:30:0', TIMESTAMP '2021-1-17 14:30:0' + INTERVAL '30 minutes');
INSERT INTO EVENTS VALUES (nextval('EventsEventID'), 7, 'Test 1', 'First Test of the Series', TIMESTAMP '2021-9-16 10:30:0', TIMESTAMP '2021-9-16 10:30:0' + INTERVAL '60 minutes');
--
INSERT INTO FORMS VALUES (nextval('FormsFormID'), 1, 'Requirements Validity', '');
INSERT INTO FORMS VALUES (nextval('FormsFormID'), 1, 'Quality of Presentation', '');
INSERT INTO FORMS VALUES (nextval('FormsFormID'), 4, 'Questions', 'Form for Q&A');
INSERT INTO FORMS VALUES (nextval('FormsFormID'), 4, 'Ideas on Finance Improvement', '');
INSERT INTO FORMS VALUES (nextval('FormsFormID'), 3, 'Meetings Form', '');
INSERT INTO FORMS VALUES (nextval('FormsFormID'), 3, 'Q&A Form', '');
INSERT INTO FORMS VALUES (nextval('FormsFormID'), 5, 'Test Form', '');
--
INSERT INTO EVENTFORMS VALUES (nextval('EventFormsEventFormID'), 1, 1);
INSERT INTO EVENTFORMS VALUES (nextval('EventFormsEventFormID'), 1, 2, 2);
INSERT INTO EVENTFORMS VALUES (nextval('EventFormsEventFormID'), 2, 2);
INSERT INTO EVENTFORMS VALUES (nextval('EventFormsEventFormID'), 3, 2);
    -- question form appears 3 times in same presentation
INSERT INTO EVENTFORMS VALUES (nextval('EventFormsEventFormID'), 5, 3);
INSERT INTO EVENTFORMS VALUES (nextval('EventFormsEventFormID'), 5, 3, 2);
INSERT INTO EVENTFORMS VALUES (nextval('EventFormsEventFormID'), 5, 3, 3);
INSERT INTO EVENTFORMS VALUES (nextval('EventFormsEventFormID'), 6, 4);
INSERT INTO EVENTFORMS VALUES (nextval('EventFormsEventFormID'), 7, 5);
INSERT INTO EVENTFORMS VALUES (nextval('EventFormsEventFormID'), 8, 5);
INSERT INTO EVENTFORMS VALUES (nextval('EventFormsEventFormID'), 9, 6);
INSERT INTO EVENTFORMS VALUES (nextval('EventFormsEventFormID'), 12, 7);
--
INSERT INTO QUESTIONS VALUES (nextval('QuestionsQuestionID'), 'long', 1, 1, 'Which other requirements are needed?');
INSERT INTO QUESTIONS VALUES (nextval('QuestionsQuestionID'), 'short', 1, 2, 'Which requirements could be omitted?');
INSERT INTO QUESTIONS VALUES (nextval('QuestionsQuestionID'), 'rating', 1, 3, 'How good are the Requirements?', '1, 2, 3, 4, 5');
INSERT INTO QUESTIONS VALUES (nextval('QuestionsQuestionID'), 'multi', 1, 4, 'Could you hear the presentation well?', 'Yes, No');
INSERT INTO QUESTIONS VALUES (nextval('QuestionsQuestionID'), 'long', 1, 5, 'What could be improved for the presentation?');
INSERT INTO QUESTIONS VALUES (nextval('QuestionsQuestionID'), 'long', 1, 6, 'How would you rate the presentation?', '1, 2, 3');
INSERT INTO QUESTIONS VALUES (nextval('QuestionsQuestionID'), 'long', 3, 1, 'Do you have any questions about this?');
INSERT INTO QUESTIONS VALUES (nextval('QuestionsQuestionID'), 'long', 4, 1, 'What could make the Finance department more inclusive?');
INSERT INTO QUESTIONS VALUES (nextval('QuestionsQuestionID'), 'short', 4, 2, 'In one word, describe how you feel about Finance');
INSERT INTO QUESTIONS VALUES (nextval('QuestionsQuestionID'), 'long', 5, 1, 'Is there anything you do not understand about this?');
INSERT INTO QUESTIONS VALUES (nextval('QuestionsQuestionID'), 'multi', 5, 2, 'Which part specifically could be improved?', 'User interface, Backend, Frontend, Database Structure, Other');
INSERT INTO QUESTIONS VALUES (nextval('QuestionsQuestionID'), 'long', 6, 1, 'Ask your questions here');
INSERT INTO QUESTIONS VALUES (nextval('QuestionsQuestionID'), 'long', 7, 1, 'Test Long Question');
INSERT INTO QUESTIONS VALUES (nextval('QuestionsQuestionID'), 'short', 7, 2, 'Test Short Question');
INSERT INTO QUESTIONS VALUES (nextval('QuestionsQuestionID'), 'multi', 7, 3, 'Test Multi Question', '["Option 1", "Option 2", "Option 3"]');
INSERT INTO QUESTIONS VALUES (nextval('QuestionsQuestionID'), 'rating', 7, 4, 'Test Rating Question', '1, 2, 3, 4, 5');
--
INSERT INTO MOOD VALUES (nextval('MoodsMoodID'), 1, -0.4, '2020-05-12 10:15:00');
INSERT INTO MOOD VALUES (nextval('MoodsMoodID'), 1, 0.0, '2020-05-12 10:46:10');
INSERT INTO MOOD VALUES (nextval('MoodsMoodID'), 2, 0.0, '2021-07-31 11:14:00');
INSERT INTO MOOD VALUES (nextval('MoodsMoodID'), 2, 0.7, '2021-07-31 13:09:25');
INSERT INTO MOOD VALUES (nextval('MoodsMoodID'), 3, 0.3, '2021-08-13 11:06:01');
INSERT INTO MOOD VALUES (nextval('MoodsMoodID'), 3, 0.2, '2021-08-13 12:56:57');
INSERT INTO MOOD VALUES (nextval('MoodsMoodID'), 3, -0.5, '2021-08-13 13:29:48');
INSERT INTO MOOD VALUES (nextval('MoodsMoodID'), 4, -0.4, '2021-03-25 19:59:33');
INSERT INTO MOOD VALUES (nextval('MoodsMoodID'), 5, 0.9, '2021-01-11 16:20:08');
INSERT INTO MOOD VALUES (nextval('MoodsMoodID'), 6, 1.0, '2020-12-30 09:40:39');
INSERT INTO MOOD VALUES (nextval('MoodsMoodID'), 7, -0.6, '2021-01-06 10:12:23');
INSERT INTO MOOD VALUES (nextval('MoodsMoodID'), 8, 0.0, '2021-01-27 10:24:22');
INSERT INTO MOOD VALUES (nextval('MoodsMoodID'), 9, 0.2, '2021-05-17 10:24:22');
INSERT INTO MOOD VALUES (nextval('MoodsMoodID'), 9, 0.0, '2021-05-17 10:44:22');
INSERT INTO MOOD VALUES (nextval('MoodsMoodID'), 10, -0.5, '2021-11-10 10:06:11');
INSERT INTO MOOD VALUES (nextval('MoodsMoodID'), 11, -0.8, '2021-11-21 10:52:00');
INSERT INTO MOOD VALUES (nextval('MoodsMoodID'), 12, 0.0, '2021-01-01 00:00:00');
INSERT INTO MOOD VALUES (nextval('MoodsMoodID'), 12, 0.3, '2021-01-01 00:30:00');


-- default mood for ratings and multi choice

-- insert some sample answers

-- response to "End of event" form in event 5 with event form 18
INSERT INTO answers VALUES (nextval('answersanswerid'), 5, 1, 18, 0, FALSE, FALSE, '2021-01-01 00:30:00', '3');
INSERT INTO Mood VALUES (nextval('moodsmoodid'), 5, 0.5, '2021-01-01 00:30:00');
INSERT INTO answers VALUES (nextval('answersanswerid'), 6, 1, 18, (select last_value from moodsmoodid), FALSE, FALSE, '2021-01-01 00:30:00', 'The explaination of .. was not very good');
INSERT INTO Mood VALUES (nextval('moodsmoodid'), 5, 0.7, '2021-01-01 00:30:00');
INSERT INTO answers VALUES (nextval('answersanswerid'), 18, 1, 18, (select last_value from moodsmoodid), FALSE, FALSE, '2021-01-01 00:30:00', 'No, I dont have any questions');

-- general feedback for event 5
INSERT INTO Mood VALUES (nextval('moodsmoodid'), 5, 0.9, '2021-01-01 00:40:00');
INSERT INTO answers VALUES (nextval('answersanswerid'), 0, 1, (select eventFormID from eventForms where eventid = 5 and formid = 0), (select last_value from moodsmoodid), FALSE, FALSE, '2021-01-01 00:40:00', 'I think the event is going well!');


-- feedback for "requirements validity" form in event 12
INSERT INTO Mood VALUES (nextval('moodsmoodid'), 12, -0.5, '2021-01-04 00:30:00');
INSERT INTO answers VALUES (nextval('answersanswerid'), 24, 1, 25, (select last_value from moodsmoodid), FALSE, FALSE, '2021-01-04 00:30:00', 'Response to test long question');
INSERT INTO Mood VALUES (nextval('moodsmoodid'), 12, -0.2, '2021-01-04 00:30:00');
INSERT INTO answers VALUES (nextval('answersanswerid'), 25, 1, 25, (select last_value from moodsmoodid), FALSE, FALSE, '2021-01-04 00:30:00', 'Response to test short question');
INSERT INTO Mood VALUES (nextval('moodsmoodid'), 12, 0.5, '2021-01-04 00:30:00');
INSERT INTO answers VALUES (nextval('answersanswerid'), 26, 1, 25, (select last_value from moodsmoodid), FALSE, FALSE, '2021-01-04 00:30:00', 'Option 1');
INSERT INTO Mood VALUES (nextval('moodsmoodid'), 12, 0.7, '2021-01-04 00:30:00');
INSERT INTO answers VALUES (nextval('answersanswerid'), 27, 1, 25, (select last_value from moodsmoodid), FALSE, FALSE, '2021-01-04 00:30:00', '4');

-- general feedback event 12
INSERT INTO Mood VALUES (nextval('moodsmoodid'), 12, 0.9, '2021-01-01 00:40:00');
INSERT INTO answers VALUES (nextval('answersanswerid'), 0, 1, (select eventFormID from eventForms where eventid = 12 and formid = 0), (select last_value from moodsmoodid), FALSE, FALSE, '2021-01-01 00:40:00', 'I think the event is going well!');
