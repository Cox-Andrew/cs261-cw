
import cr1test

# this needs python 3.6 or higher


"""
CR1: The host can set up an event for a particular session, series of workshops or project.
{ DR1.1(S): Home page has a link to host an event where the host can sign into their account or create a
new account.
	host signs into account
	host creates an account
	# host deletes an account


{ DR1.2(M): Signed-in hosts have access to a page where they can view, create, edit or delete events (a
physical event; the time during which attendees provide live feedback. Events can have multiple forms)
and series (created by the host; can comprise of multiple events).
	hosts can get event info
	post
	put
	delete
	hosts can get series info,
	post
	put
	delete


{ DR1.3(M): Events have options of name, description, date, start/end time.
	No tests needed

{ DR1.4(S): Host can create feedback templates of their choice or choose from defaults.
	Create feedback template (form)
	Add form to event
	add default form to event

{ DR1.5(M): To implement a \series of workshops" the host may schedule multiple events under one series,
which will also have its own name and description.
	add event to series

{ DR1.6(S): The UI (User Interface) for series of events allows for duplicating events across repeated times.
CR2: The event should be viewable by attendees.
	duplicate an event (not implemented in back end)

{ DR2.1(M): Join an event option in the interface allows attendees to enter a unique event ID.
	we are using invite codes for this instead

{ DR2.2(M): Ask the attendee for their name. They can choose to be anonymous if they do not want their
name to be visible.
	answers can be submitted anonymously

{ DR2.3(C): A link can be generated and copied/shared by a host which uses GET parameters to automat-
ically fill in a code to grant an attendee access to an event.
	attendee is granted access to an event after putting an invite code

{ DR2.4(C): Attendees can sign in/register (optionally) using their email addresses.
	host signs into account
	host creates an account
	# host deletes an account

{ DR2.5(C): Attendees who are signed in can see a list of registered events.
	get a list of registered events

{ DR2.6(C): Attendees that are not signed in still have a user ID assigned tied to their browser/app session;
this is so that the attendee can edit their responses to questions.
	a temporary attendeeID is added (not implemented - attendees must be signed in)

CR3: Hosts can see feedback live as it is submitted.
	feedback data since a certain time can be retrieved /v0/feedback

{ DR3.1(M): Host page will have a view to indicate the current mood of the attendees.
	mood data since a certain time can be retrieved

{ DR3.2(M): Host page will have a view to display the general feedback (feedback, submitted by attendees
in a text box, that may be lled in at any point) given by the attendees.
	see CR3

{ DR3.3(M): Host page will have a view for displaying the general mood over the time of event.
	see DR3.1

{ DR3.4(S): Host page will show when the page was last updated and if the system is actively listening for
feedback.
	not back end functionality

{ DR3.5(C): The host can see if a response has been edited.
	edit an answer, check to see that edited response appears in feedback

{ DR3.6(M): Host page will have a view to display the responses given to forms by the attendees.
	not back end functionality

CR4: Attendees can provide live feedback for the questions asked by the host.
	attendees can submit answers

{ DR4.1(M): Forms asked by the host will be visible on the attendee's page, and they can be shown between
specied points in the session, the whole session, or released on demand by the host.
	attendees can get forms that are related to their registered events
	forms can be released on demand

{ DR4.2(M): Attendees may answer the general feedback form multiple times and specic templates once.
The specic templates are more personalised than the general feedback and are displayed only for a certain
period. They can be shown to the attendees whenever the host sees t.
	see CR4 and DR4.1

{ DR4.3(C): Attendees may edit their feedback.
	attendees can edit answers

{ DR4.4(M): Attendees should not be able to send multiple feedback responses more than once every 2
minutes, as to prevent abuse of the system. This will also allow the attendee to edit their response during
those 2 minutes.
	try sending two feedback responses in quick succession

{ DR4.5(M): Attendees can provide general non-form feedback at any point.
	see CR4

CR5: Attendees can explicitly provide their mood during the event.
{ DR5.1(M): Attendees can select from 5 dierent emojis representing increasing degrees of satisfaction to
indicate their mood during the event. 5 emojis gives a large enough range of emotions.
	explicit mood can be submitted

{ DR5.2(M): The attendees can provide this feedback as many times as they want during the event.
	see DR5.1

CR6: Attendees can review and track their previously submitted feedback (from project statement: \[. . . ]
tracking your own mental state can have personal benets").
	attendees can get a list of their answers

{ DR6.1(C): If a user is signed in or in the same browser/app session as when the feedback was submitted,
the previously submitted feedback can be viewed by them.
	! not implemented - attendees must be signed in

{ DR6.2(C): Their analysed mood is shown over time.
	attendees can get a list of their submitted explicit mood

CR7: Hosts can see feedback and mood analysis live as it is submitted.
{ DR7.1(M): The interacting system for backend and frontend will continuously show the updated general
and form feedback to the host. The message, time received, mood and name of the feedback provider (if
they have not chosen to remain anonymous) are displayed.
	see CR3

{ DR7.2(C): The host can choose to receive notications when feedback is received.
	not a back end requirement

{ DR7.3(M): The system will show a general mood analysis based on the feedback given by the attendees
over the time of event. The analysis information comprises of: Mood over time visualization: chart
showing moving average of mood over time Individual mood of feedback messages. 
	not a back end requirement

(C): The implicit
mood is not re-calculated if a question response is edited, however if the user edits an explicit given mood,
then this is changed in the analysis calculations.
	check mood value does not change when answer is edited
	users can edit explitictly given mood (this is not implemented - explicit mood isn't even stored alongside userID!)

{ DR7.4(C): Language analysis is performed on the feedback displayed to highlight key phrases.
	not implemented

{ DR7.5(M): Visualizations data is sent to the host to display the result of feedback forms, comprising of
pie charts for unordered data and histograms for ordered data.
	not a back end requirement

2
5 System Requirements Specication
CR8: The system is usable on all platforms (desktop, tablet and mobile).
	not a back end requirement

{ DR8.1 (S): The (web)/app is responsive i.e., it should scale components and structure to screen size.
	not a back end requirement

CR9: Host account details will be stored in the database.
	see DR1.1

{ DR9.1(M): Host will have a unique host ID, name, email and password that will be stored in a database.
	see DR1.1

{ DR9.2(M): Host will have its associated events/series stored in the database.
	see DR1.2

{ DR9.3(M): Event feedback will be stored individually for hosts.
	we did not do this!

CR10: Event details will be stored in the database.
	see DR1.2

{ DR10.1(M): Each series of events will be identied by the host ID in the database.
	see DR1.2

{ DR10.2(M): All the event forms will be identied by the unique event IDs.
	see DR1.4

CR11: The mood is analysed from the answers given by the attendees.
{ DR11.1(M): A sentiment analysis program will calculate the mood of the feedback messages.
	submit answers, verify that mood value becomes non null

{ DR11.2(S): The system will show the mood over the time of the event derived from the sentiment analysis
program. The general sentiment over time is calculated as an average sentiment of messages and explicitly
stated mood over a previous period of time. The period can be changed by the host to accommodate for
events with dierent numbers of attendees, where the amount of feedback may not be enough to calculate
a mean.
	not a back end requirement

CR12: Feedback details will be stored in database.
{ DR12.1(M): Each user's general feedback messages will be identied by unique IDs and stored in the
database.
	see CR4

{ DR12.2(M): For each form, the questions asked, the responses and the mood of a user will be stored, as
well as the time and the user data (if chosen not to be anonymous).
	see something idc






6 Non-Functional Requirements
R13: Must be usable by non-tech users.
{ R13.1(M): The user is not required to download any additional software or plugins.
{ R13.2(M): A UI is used for all necessary functions used to operate the system.
R14: The system must remain responsive throughout an event.
{ R14.1(M): System must continue to work throughout events.
{ R14.2(M): Feedback must appear to host within 5 seconds. This is a small enough time that it is unim-
portant to the host, and long enough that that backend does not get overwhelmed with polling from the
browser.
{ R14.3(S): All requests should have a TTFB server response time of less than 500ms, as recommended by
Firefox [1].
R15: The application requires little to no training for use.
{ R15.1(M): All UI features are self-explanatory or contain inline help.
{ R15.2(S): System stays the same and has few dierences between template styles.
R16: The system should remain up to date (maintainability).
{ R16.1(M): The system will not need any further updates for it to be functional at a given point.
{ R16.2(S): The system can be easily maintained by a development team.
3
{ R16.3(S): The sentiment analysis will be separated by an API (Application Programming Interface) such
that any advances in sentiment analysis tools can be more easily implemented.
{ R16.4(S): The system should have modularity of components and services allowing for easier, rapid
changes.
R17: System performance must be validated using appropriate techniques.
{ R17.1(M): Test cases are made for all functions and should run without errors and return the correct
results.
R18: Compliant with legal requirements.
{ R18.1(M): All software is either developed by the team or is accompanied by appropriate open-source
licences.
{ R18.2(M): Privacy is compliant with GDPR and local privacy constraints.
R19: The system is secure against malicious attacks.
{ R19.1(S): Must be secure against attacks which stop the system from working e.g., SQL injection attacks,
Cross Site Scripting, DoS attacks (out of prototype scope).
R20: The system can be distributed, used, and understood globally.
{ R20.1(C): The system supports localisation and future localisation improvements.
{ R20.2(C): Containers are used to allow for cloud distribution and scalability.
R21: Components of the system can be reused (reusability, utility).
{ R21.1(C): A RESTful API allows for mood analysis in other projects.
"""


if __name__ == "__main__":
	cr1test.runTests()