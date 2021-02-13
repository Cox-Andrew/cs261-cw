
<!-- Problems 

Editing forms will cause chaos if attendees have already answered questions. If the form is changed then the question object can still remain, but the original ordering of the form will be lost. Maybe copy every form/question when it is used in an event?


-->









# API Documentation

## Available to all: 
### 1.	Register for account. 

### 2.	Authenticate.
Use OAuth2.0/OpenID?

### 3.	Change name. 

### 4.	Change anonymity. 


## Host functionality (available to authenticated users): 
### 1.	Create/Read/Update/Delete hosted series. 

```GET /v0/series/{seriesID}```\
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

```POST /v0/series```\
Request:
```
{
	"hostID" : 2342341,
	"data": {
		"title’: "Series Title",
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
```PUT /v0/series/{seriesID}```\
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
```DELETE /v0/series/{seriesID}```

### 2.	Create/Read/Update/Delete hosted events. 

Get an event\
```GET /v0/events/{eventID}```\
Response:
```
{
	"eventID": 1243214,
	"seriesID" : 2342341,
	"eventFormIDs": [12423142,4324324,5462354],
	"data": {
		"title": "Event Title",
		"description": "Description of Event."
	}
}
```

Create an event\
```POST /v0/events```\
Request:
```
{
	"seriesID" : 2342341,
	"data": {
		"title": "Event Title",
		"description": "Description of Event.",
		"time-start": "2020-01-22T19:33:05Z",
		"time-end":"2020-01-22T19:33:05Z",
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
```PUT /v0/events/{eventID}```\
Only items in the "data" section may be updated.\
Request:
```
{
	"data": {
		"title": "Event Title",
		"description": "Description of Event.",
		"time-start": "2020-01-22T19:33:05Z",
		"time-end": "2020-01-22T19:33:05Z",
	}
}
```
Delete an event\
```DELETE /v0/events/{eventID}```

Add a form to an event\
```POST /v0/event-forms```\
Request:
```
{
	"formID": 354234,
	"preceding-event-form": 42389 \* May be null to be the first form*\
}
```
Response:
```
{
	"eventFormID": 42142
}
```

Delete a form from an event\
```DELETE /v0/event-forms/{eventFormID}```


### 3.	Create/Read/Update/Delete form templates (hosted series/event). 

Get a list of a user's forms. Users may only access their own templates.\
```GET /v0/forms?hostID={hostID}```\
Response:
```
{
	"formIDs": [534324, 521512, 534524]
}
```

Get a form.\
```GET /v0/forms/{formID}```

Create a form.\
Edit a form.\
Delete a form.


### 4.	Get default forms.

```GET /v0/default-forms```\
Response:
```
{
	"formIDs": [534324, 521512, 534524]
}
```
### 5.	Get invite code (hosted event). 

```GET /v0/events/{eventID}/invite-code```\
Response:
```
{
	"invite-code": "123-123-123"
}
```




### 6.	Get all feedback (hosted event). 

```GET /v0/feedback?eventID={eventID}```\
Note: Answers can be edited, so the client may already have received responses to these forms.\
Response:
```
{
	"eventID": 34414312,
	"time-updated-since": "2020-01-22T19:33:02Z",
	"contains": 2,
	"list": [
		{
			"formID": 349981,
			"submissionID": 4238492,
			"account-name": "John Smith", /* may be null for anonymous responses */
			"time-submitted": "2020-01-22T19:33:05Z",
			"time-updated": "2020-01-22T19:33:05Z",
			"is-edited": false,
			"answers": [
				{
					"questionID": 524753,
					"answerID": 524753,
					"mood-value": 0.432423523, /* may be null. Mood may be accessed in other ways. */
					"is-edited": false,
					"time-updated": "2020-01-22T19:33:05Z",
					"response": "Response to question 1"
				},
				{
					"questionID": 5345342,
					"answerID": 524753,
					"mood-value": null,
					"is-edited": false,
					"time-updated": "2020-01-22T19:33:05Z",
					"response": "3", /* checkbox answer 3 selected */
				}
			]
		},
		{
			"formID": 0, /* general feedback */
			"submissionID": 4238492,
			"account-name": "John Smith", /* may be null for anonymous responses */
			"time-submitted": "2020-01-22T19:33:05Z",
			"time-updated": "2020-01-22T19:33:05Z",
			"is-edited": false,
			"answers": [
				{
					"questionID": 1231421,
					"answerID": 524753,
					"mood-value": 0.432423523,
					"is-edited": false,
					"time-updated": "2020-01-22T19:33:05Z",
					"response": "This is a general feedback response."
				}
			]
		}
	]
}
```

### 7.	Get live feedback since specified time (hosted event).
 ```GET /v0/feedback?eventID={eventID}&time-updated-since={time-updated-since}```\
Time-updated-since is of the form "2020-01-22T19:33:05Z".\
Response is of the same form as getting all feedback.

### 8.	Get live mood since specified time (hosted event). 
```GET /v0/mood?eventID={eventID}&time-updated-since={time-updated-since}```\
Response:
```
{
	"eventID": 34414312,
	"time-submitted-since": "2020-01-22T19:33:02Z",
	"contains": 2,
	"list": [
		{
			"time-submitted": "2020-01-22T19:33:05Z",
			"mood-value": 0.432423523,
			"answerID": 4324322 /* may be null if it was explicit feedback */
		},
		{
			"time-submitted": "2020-01-22T19:33:05Z",
			"mood-value": 0.842384,
			"answerID": null
		}

	]
}
```
### 9.	Get analytics (hosted event). 
## Attendee request (available to authenticated users): 
### 1.	Register for event (with invite code). 
### 2.	Get metadata of registered series/event. 
### 3.	Get all forms for registered event. 
### 4.	Submit completed feedback form (registered event). 
### 5.	Submit general feedback (registered event). 
### 6.	Submit explicit mood (registered event). 
