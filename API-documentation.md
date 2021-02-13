# API Documentation

## Available to all: 
1.	Register for account. 

2.	Authenticate. \
Use OAuth2.0/OpenID?

3.	Change name. 

4.	Change anonymity. 


## Host functionality (available to authenticated users): 
1.	Create/Read/Update/Delete hosted series. 


```GET /v0/series/{seriesID}```\
Response:
```
{
	'seriesID': 1243214,
	'hostID' : 2342341,
	'eventIDs': [12423142,4324324,5462354],
	'data': {
		'title’: 'Series Title',
		'description': 'Description of Series.'
	}
}
```

```POST /v0/series```\
Request:
```
{
	'hostID' : 2342341,
	'data': {
		'title’: 'Series Title',
		'description': 'Description of Series.'
	}
}
```
Response:
```
{
	'seriesID': 1243214
}
```
```PUT /v0/series/{seriesID}```\
Only items in the 'data' section may be updated.\
Request:
```
{
	'data':{
		'title': 'New series title',
		'description': 'New description of Series.'
	}
}
```
```DELETE /v0/series/{seriesID}```



2.	Create/Read/Update/Delete hosted events. 




3.	Create/Read/Update/Delete form templates (hosted series/event). 
4.	Get default forms. 
5.	Get invite code (hosted event). 
6.	Get all feedback (hosted event). 
7.	Get live feedback since specified time (hosted event). 
8.	Get live mood since specified time (hosted event). 
9.	Get analytics (hosted event). 
Attendee request (available to authenticated users): 
1.	Register for event (with invite code). 
2.	Get metadata of registered series/event. 
3.	Get all forms for registered event. 
4.	Submit completed feedback form (registered event). 
5.	Submit general feedback (registered event). 
6.	Submit explicit mood (registered event). 
