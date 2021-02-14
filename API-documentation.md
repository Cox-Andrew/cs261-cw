
<!-- Problems 

Editing forms will cause chaos if attendees have already answered questions. If the form is changed then the Question entry can still remain, but the original ordering of the form will be lost. Maybe copy every form/question when it is used in an event?

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

Get a series\
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

Create a new series\
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

Edit a series\
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
Delete a series\
```DELETE /v0/series/{seriesID}```


### 2.	Create/Read/Update/Delete hosted events. 

Get an event\
```GET /v0/events/{eventID}```\
Response:
```
{
	"eventID": 1243214,
	"seriesID" : 2342341,
	"formIDs": [12423142,4324324,5462354],
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
```POST /v0/events/{eventID}/forms```\
Request:
```
{
	"formID": 354234,
	"preceding-formID": 42389 \* May be null to be the first form*\
}
```


Delete a form from an event\
```DELETE /v0/events/{eventID}/forms/{formID}```


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
```GET /v0/forms/{formID}```\
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
```POST /v0/forms```\
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
```POST /v0/forms/{formID}```\
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
```DELETE /v0/forms/{formID}```

Get a question.\
```GET /v0/questions/{questionID}```\
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
```POST /v0/forms```\
Request:
```
{
	"formID": 234212,
	"preceding-questionID": 2432432, /* may be null to denote the first question */
	"data": {
		"type": "multichoice",
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
```PUT /v0/questions/{questionID}```\
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
```PUT /v0/questions/{questionID}```\
Request
```
{
	"preceding-questionID": 2432432 /* may be null to denote the first question */
}
```

Delete a question.\
```DELETE /v0/questions/{questionID}```



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
					"data": {
						"response": "Response to question 1"
					}
				},
				{
					"questionID": 5345342,
					"answerID": 524753,
					"mood-value": null,
					"is-edited": false,
					"time-updated": "2020-01-22T19:33:05Z",
					"data": {
						"response": "3", /* checkbox answer 3 selected */
					}
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
 ```GET /v0/feedback?eventID={eventID}&time-updated-since={time-updated-since}```\
Time-updated-since is of the form "2020-01-22T19:33:05Z".\
Response is of the same kind as getting all feedback.

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
```GET /v0/event/{eventID}/analytics```\
Response:\
TODO
## Attendee request (available to authenticated users): 
### 1.	Register for event (with invite code). 
```POST /v0/register-event```\
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
```POST /v0/submissions```\
Request:
```
{
	"eventID": 3424323
	"formID": 4234324,
	/* time-submitted is not included - this is added by the server */
	"list": [
		{
			"questionID": 4234324,
			"data": {
				"response": "This is long answer question resonse."
			}
		},
		{
			"questionID": 423523,
			"data": {
				"response": "3", /* checkbox answer 3 selected */
			}
		}
	]
}
``` 
Response:
```
{
	"submissionID": 423432,
	"answerIDs": [7865347, 4231432]
}
```
### 5.	Submit general feedback (registered event). 
```POST /v0/submissions```\
Some request info as above. Use the formID ```0```, which corresponds to the general feedback form.
### 6.	Submit explicit mood (registered event). 
```POST /v0/mood```\
Request:
```
{
	"eventID": 4242343,
	"mood-value": 0.432423523
}
```
