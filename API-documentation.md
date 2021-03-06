
<!-- Problems 

Editing forms will cause chaos if attendees have already answered questions. If the form is changed then the Question entry can still remain, but the original ordering of the form will be lost. Maybe copy every form/question when it is used in an event?

-->









# API Documentation

For all endpoints where data is restricted, the user must have the appropriate tokens/etc I don't really know atm how this will work (TODO)

## Available to all: 
### 1.	Register for account. 
```POST /v0/attendees``` Working\
```POST /v0/hosts``` Working\
Request
```
{
	"email": "example@gmail.com",
	"pass": "mypassword",
	"account-name": "Joe Bloggs"
}
```
Response:
```
{
	"hostID": 32423423
}
```
```
{
	"attendeeID": 42342343
}
```

### 2.	Authenticate.
Use OAuth2.0/OpenID?\
Returns a token/something, TODO.

### 3.	Change name. 
```PUT /v0/attendees/{attendeeID}``` Working\
```PUT /v0/hosts/{hostID}```Working\
Request:
```
{
	"account-name": "John Smith"
}
```
Users may also change their emails and passwords using the same endpoint\
Request:
```
{
	"account-name": "John Smith",
	"email": "new@email.com",
	"pass": "newPassword"
}
```

### 4.	Change anonymity. 
<!-- For anonymous submissions, the request should leave attendeeID blank/null.\
TODO: problem - the current system would not be able to authenticate attendees' requests for the mood of this feedback, as the attendeeID is not stored alongside anonymous requests. -->
If a user is creating an anonymous response, the line "isAnonymous": true should be included in the answer post request.

### 5. Get account details
Only available to the user that is currently signed in.\
```GET /v0/attendees/{attendeeID}``` Working\
```GET /v0/hosts/{hostID}``` Working \
Response:
```
{
	"data": {
		"email": "example@gmail.com",
		"account-name": "Joe Bloggs"
	}
}
```

### 6. Delete account
```DELETE /v0/attendees/{attendeeID}``` Working\
```DELETE /v0/hosts/{hostID}``` Working


## Host functionality (available to authenticated users): 
### 1.	Create/Read/Update/Delete hosted series. 

Get a series\
```GET /v0/series/{seriesID}``` Working\
Response:
```
{
	"seriesID": 1243214,
	"hostID" : 2342341,
	"eventIDs": [12423142,4324324,5462354],
	"data": {
		"title": "Series Title",
		"description": "Description of Series."
	}
}
```

Create a new series\
```POST /v0/series``` Working - output a bit messy\
Request:
```
{
	"hostID" : 2342341,
	"data": {
		"title": "Series Title",
		"description": "Description of Series."
	}
}
```
Response:
```
{
	"seriesID": 1243214
}
```

Edit a series\
```PUT /v0/series/{seriesID}``` Working\
Only items in the "data" section may be updated.\
Request:
```
{
	"data":{
		"title": "New series title",
		"description": "New description of Series."
	}
}
```
Delete a series\
```DELETE /v0/series/{seriesID}``` Working


### 2.	Create/Read/Update/Delete hosted events. 

Get an event\
```GET /v0/events/{eventID}``` Working\
Response:
<!-- eventFormIDs is only intended for the host to use to edit the form -->
<!-- maybe it shouldn't appear when attendees send this command? -->
```
{
	"eventID": 1243214,
	"seriesID" : 2342341,
	"formIDs": [12423142,4324324,5462354],
	"eventFormIDs": [312312,312312,2352234], 
	"data": {
		"title": "Event Title",
		"description": "Description of Event.",
		"time-start": "2020-01-22T19:33:05",
		"time-end": "2020-01-22T19:33:05",
	}
}
```

Create an event\
```POST /v0/events``` Working\
Request:
```
{
	"seriesID" : 2342341,
	"data": {
		"title": "Event Title",
		"description": "Description of Event.",
		"time-start": "2020-01-22T19:33:05",
		"time-end":"2020-01-22T19:33:05",
	}
}
```
Response:
```
{
	"eventID": 1243214
}
```
Edit an event\
```PUT /v0/events/{eventID}``` Working\
Only items in the "data" section may be updated.\
Request:
```
{
	"data": {
		"title": "Event Title",
		"description": "Description of Event.",
		"time-start": "2020-01-22T19:33:05",
		"time-end": "2020-01-22T19:33:05",
	}
}
```
Delete an event\
```DELETE /v0/events/{eventID}``` Working

Add a form to an event\
```POST /v0/event-forms``` Working, but always inserts at the end of an event regardless of preceding-eventFormID\
Request:
```
{
	"eventID": 432423,
	"formID": 354234,
	"time-start": null,
	"time-end": null,
	"is-active": false,
	"preceding-eventFormID": 42389 \* May be null to be the first form*\
}
```
Response:
```
{
	"eventFormID": 3423423
}
```

Activate or change start and and times of an EventForm\
`PUT /v0/event-forms/{eventFormID}` org.postgresql.util.PSQLException: ERROR: new row for relation "eventforms" violates check constraint "eventforms_numinevent_check"\

Request:
```
{
	"time-start": null,
	"time-end": null,
	"is-active": true,
	"preceding-eventFormID": 42389 \* May be null to be the first form*\
}
```

Delete a form from an event
```DELETE /v0/event-forms/{eventFormID}``` Working



### 3.	Create/Read/Update/Delete form templates (hosted series/event). 

Get a list of a user's forms. Users may only access their own templates.\
```GET /v0/forms?hostID={hostID}``` Working\
Response:
```
{
	"formIDs": [534324, 521512, 534524]
}
```

Get a form.\
```GET /v0/forms/{formID}``` Working\
Response:
```
{
	"hostID": 234212,
	"data": {
		"title": "Form title",
		"description": "Form description"
	},
	"questionIDs": [312321, 4354325, 5435235]
}
```


Create a form.\
```POST /v0/forms``` Working\
Request:
```
{
	"hostID": 234212,
	"data": {
		"title": "Form title",
		"description": "Form description"
	}
}
```
Response:
```
{
	"formID": 4322234
}
```
Edit a form.\
```PUT /v0/forms/{formID}``` Working \
Request:
```
{
	"data": {
		"title": "Form title",
		"description": "Form description"
	}
}
```

Delete a form.\
```DELETE /v0/forms/{formID}``` Working

Get a question.\
```GET /v0/questions/{questionID}``` Working \
Response:
```
{
	"questionID": 3423432,
	"formID": 432423,
	"data": {
		"type": "longanswer",
		"text": "How did you find today's presentation?",
		"options": null
	}
}
```

Create a question.\
```POST /v0/questions``` Working, but seems to ignore preceding-questionID\
Request:
```
{
	"formID": 234212,
	"preceding-questionID": 2432432, /* may be null to denote the first question */
	"data": {
		"type": "multi",
		"text": "Choose one of the options",
		"options": [
			"Option 1",
			"Option 2",
			"Option 3"
		]
	}
}
```
Response:
```
{
	"questionID": 4322234
}
```

Edit a question.\
```PUT /v0/questions/{questionID}``` Working.\
Request:
```
{
	"data": {
		"type": "rating",
		"text": "How well was this topic explained?",
		"options": null
	}
}
```
Edit the position of a question. The question that originally followed preceding-questionID is moved to after questionID:\
```PUT /v0/questions/{questionID}``` Working\
Request
```
{
	"preceding-questionID": 2432432 /* may be null to denote the first question */
}
```

Delete a question.\
```DELETE /v0/questions/{questionID}``` Working



### 4.	Get default forms.

Get the forms of host with hostID `0`.

### 5.	Get invite code (hosted event). 

<!-- OLD VERSION: ```GET /v0/events/{eventID}/invite-code```\ -->
```GET /v0/invite-code?eventID={eventID} ```404 Not Found\
Response:
```
{
	"invite-code": "123-123-123"
}
```
Note: the format of the invite codes is not yet defined




### 6.	Get all feedback (hosted event). 

```GET /v0/feedback?eventID={eventID}``` 501 Not Implemented\
```attendees may also get their own feedback in this way by adding "&attendeeID={attendeeID}" ```\
Note: Answers can be edited, so the client may already have received responses to these forms.\
Response:
```
{
	"eventID": 34414312,
	"time-updated-since": "2020-01-22T19:33:02",
	"contains": 2,
	"list": [
		{
			"formID": 349981,
			"eventFormID": 2432423,
			// "submissionID": 4238492,
			// submission ID will no longer included, but still packaged like this to make it easier for the front end
			"account-name": "John Smith", /* may be null for anonymous responses */
			"attendeeID": 3424234 /* always included so users may be blocked. */
			// "time-submitted": "2020-01-22T19:33:05",
			"time-updated": "2020-01-22T19:33:05",
			// time-updated specified the latest of the time-updated of the answers
			"is-edited": false,
			"answers": [
				{
					"questionID": 524753,
					"answerID": 524753,
					"mood-value": 0.432423523, /* may be null. Mood may be accessed in other ways. */
					"is-edited": false,
					"time-updated": "2020-01-22T19:33:05",
					"data": {
						"response": "Response to question 1"
					}
				},
				{
					"questionID": 5345342,
					"answerID": 524753,
					"mood-value": null,
					"is-edited": false,
					"time-updated": "2020-01-22T19:33:05",
					"data": {
						"response": "3", /* checkbox answer 3 selected */
					}
				}
			]
		},
		{
			"formID": 0, /* general feedback */
			"eventFormID": 4243424
			// "submissionID": 4238492,
			"account-name": "John Smith", /* may be null for anonymous responses */
			// "time-submitted": "2020-01-22T19:33:05",
			"time-updated": "2020-01-22T19:33:05Z",
			"is-edited": false,
			"answers": [
				{
					"questionID": 1231421,
					"answerID": 524753,
					"mood-value": 0.432423523,
					"is-edited": false,
					"time-updated": "2020-01-22T19:33:05",
					"data": {
						"response": "This is a general feedback response."
					}
				}
			]
		}
	]
}
```

### 7.	Get live feedback since specified time (hosted event).
 ```GET /v0/feedback?eventID={eventID}&time-updated-since={time-updated-since}``` java.lang.NullPointerException: Cannot read field "eventID" because "info" is null\
 ```attendees may also get their own feedback in this way by adding "&attendeeID={attendeeID}" ``` 501 Not Implemented\
Time-updated-since is of the form "2020-01-22T19:33:05".\
Response is of the same kind as getting all feedback.

### 8.	Get live mood since specified time (hosted event). 
```GET /v0/moods?eventID={eventID}&time-updated-since={time-updated-since}``` 501 Not Implemented\
```attendees may also get their own feedback in this way by adding "&attendeeID={attendeeID}" ``` 501 Not Implemented\
Response:
```
{
	"eventID": 34414312,
	"time-submitted-since": "2020-01-22T19:33:02",
	"contains": 2,
	"list": [
		{
			"time-submitted": "2020-01-22T19:33:05",
			"mood-value": 0.432423523,
			"answerID": 4324322 /* may be null if it was explicit feedback */
		},
		{
			"time-submitted": "2020-01-22T19:33:05",
			"mood-value": 0.842384,
			"answerID": null,
			"attendeeID": 3432423 /* only included if it is an explicit mood, for the purpose of kicking attendees */
		}
	]
}
```
### 9.	Get analytics (hosted event).
<!-- OLD VERSION: ```GET /v0/event/{eventID}/analytics```\ -->
```GET /v0/analytics?eventID={eventID}```\
Response:\
TODO

### 10. Kick an attendee, deleting all their feedback from the session
```DELETE /v0/event-attendees?eventID={eventID}&attendeeID={attendeeID}``` 405 Method Not Allowed

### 11. Get the feedback profile for an answer
Get the attendee ID and account-name for a response.\
```GET /v0/feedback-profile?answerID={answerID}``` Not implemented yet\
Response:
```
{
	"attendeeID": 5452454,
	"isAnonymous": false,
	"account-name": 4324324 /* null if anonymous */
}
```

### 12. Get all eventIDs in a series
`GET /v0/events?seriesID={seriesID}` java.lang.NullPointerException: Cannot invoke "String.split(String)" because the return value of "javax.servlet.http.HttpServletRequest.getPathInfo()" is null \
Response:
```
{
	"eventIDs": [4324, 543534, 3453534]
}
```

## Attendee request (available to authenticated users): 
### 1.	Register for event (with invite code). 
```POST /v0/register-event``` Invite codes are not implemented yet\
Request:
```
{
	"invite-code": "123-123-123"
}
```
Response:
```
{
	"eventID": 342432
}
```
### 2.	Get metadata of registered series/event. 
Use the ```GET``` methods defined in the host session.
### 3.	Get all forms for registered event. 
Use the ```GET``` methods defined in the host session.
### 4.	Submit completed feedback form (registered event).

Attendees can also submit single Answers:\
```POST /v0/answers``` Working, although time might be a bit messed up\
Request:
```
{
	"attendeeID": 3423423,
	"eventID": 4242342,
	"eventFormID": 34234324,
	"questionID": 4234324,
	"data": {
		"response": "This is long answer question resonse.",
		"isAnonymous": false
	}
}
```
Response:
```
{
	"answerID": 7865347
}
```

```PUT /v0/answers/{answerID}``` 200 OK, but nothing happens in the database! \
Request:
```
"data": {
	"response": "This is an edited response to a question.",
	"isAnonymous": false
}
```
### 5.	Submit general feedback (registered event). 
```POST /v0/answers``` \
Use the questionID corersponding to question in the general feedback form, which has formID ```0```.
### 6.	Submit explicit mood (registered event). 
```POST /v0/moods``` 405 Method Not Allowed\
Request:
```
{
	"eventID": 4242343,
	"mood-value": 0.432423523
}
```
### 7. Register to an event using an invite code
```GET /v0/invite-code/{invite-code}``` invite codes are not implemented\
Response:
```
{
	"eventID": 2847823
}
```
Also returns a cookie that is needed to access event-related endpoints, eg 
```GET /v0/questions/{questionID}``` is only allowed if the user has registered to an event that contains that question.

### 8. Other
```GET /v0/series/{seriesID}```\
```GET /v0/events/{eventID}```\
```GET /v0/forms/{formID}```\
```GET /v0/questions/{questionID}```\
```GET /v0/feedback?eventID={eventID}&attendeeID={attendeeID}```\
 ```GET /v0/feedback?eventID={eventID}&time-updated-since={time-updated-since}&attendeeID={attendeeID}```\
 ```GET /v0/moods?eventID={eventID}&time-updated-since={time-updated-since}&attendeeID={attendeeID}```\


