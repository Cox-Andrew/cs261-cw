import 'package:flutter/material.dart';
import 'package:intl/intl.dart';
import 'package:http/http.dart' as http;

import 'package:moodlysis_app/components/navigation.dart';
import 'package:moodlysis_app/constants.dart';
import 'package:moodlysis_app/models/event.dart';
import 'package:moodlysis_app/screens/timeline/event/arguments.dart';
import 'package:moodlysis_app/services/events.dart';
import 'package:moodlysis_app/globals.dart' as globals;

class TimelineScreen extends StatefulWidget {
  @override
  _TimelineScreenState createState() => _TimelineScreenState();
}

class _TimelineScreenState extends State<TimelineScreen> {
  List<Event> _liveEvents;
  List<Event> _upcomingEvents;

  //TODO: switch to FutureBuilder
  void _updateEvents() {
    //TODO: error catching e.g. connection failed
    getUserEvents(http.Client(), globals.currentUser).then((events) {
      List<Event> liveEvents = List<Event>();
      List<Event> upcomingEvents = List<Event>();

      for (Event event in events) {
        //TODO: implement past events archive
        if (event.schedule.end.isBefore(DateTime.now())) continue;

        if (event.schedule.start.isAfter(DateTime.now())) {
          upcomingEvents.add(event);
        } else {
          liveEvents.add(event);
        }
      }

      setState(() {
        _liveEvents = liveEvents;
        _upcomingEvents = upcomingEvents;
      });
    });
  }

  @override
  void initState() {
    _updateEvents();

    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Event Timeline'),
        actions: [
          IconButton(icon: Icon(Icons.refresh), onPressed: _updateEvents)
        ],
      ),
      body: Padding(
        padding: const EdgeInsets.all(20),
        child: Column(
          children: [
            Expanded(
              flex: 2,
              child: Column(
                children: [
                  Padding(
                    padding: const EdgeInsets.only(bottom: 4),
                    child: Row(
                      children: [
                        Text('Live events ',
                            style: Theme.of(context).textTheme.headline4),
                        Icon(Icons.live_tv,
                            color: Colors.red,
                            size:
                                Theme.of(context).textTheme.headline4.fontSize)
                      ],
                    ),
                  ),
                  Expanded(
                    child: _liveEvents == null || _liveEvents.isEmpty
                        ? NoEventsMessage(isLive: true)
                        : ListView.builder(
                            scrollDirection: Axis.horizontal,
                            itemCount: _liveEvents.length,
                            itemBuilder: (context, i) {
                              Event event = _liveEvents[i];
                              return SizedBox(
                                  width:
                                      MediaQuery.of(context).size.width * 3 / 4,
                                  child: _EventCard(event));
                            }),
                  ),
                ],
              ),
            ),
            Expanded(
              flex: 4,
              child: Column(
                children: [
                  Padding(
                    padding: const EdgeInsets.only(top: 14, bottom: 4),
                    child: Row(
                      children: [
                        Text('Upcoming events ',
                            style: Theme.of(context).textTheme.headline4),
                        Icon(
                          Icons.lock_clock,
                          color: Colors.grey,
                          size: Theme.of(context).textTheme.headline4.fontSize,
                        )
                      ],
                    ),
                  ),
                  Expanded(
                    child: _upcomingEvents == null || _upcomingEvents.isEmpty
                        ? NoEventsMessage(isLive: false)
                        : ListView.builder(
                            itemCount: _upcomingEvents.length,
                            itemBuilder: (context, i) {
                              Event event = _upcomingEvents[i];
                              return SizedBox(
                                  height:
                                      MediaQuery.of(context).size.height / 5,
                                  child: _EventCard(event));
                            }),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
      bottomNavigationBar: MoodlysisBottomNavigationBar(timelineScreenRoute),
    );
  }
}

class _EventCard extends StatelessWidget {
  final Event event;

  _EventCard(this.event);

  @override
  Widget build(BuildContext context) {
    return Card(
      child: InkWell(
        onTap: () {
          if (DateTime.now().isAfter(event.schedule.start)) {
            Navigator.pushNamed(context, eventScreenRoute, arguments: EventScreenArgs(event));
          }
        },
        child: Padding(
          padding: const EdgeInsets.all(12),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(
                event.title,
                style: Theme.of(context).textTheme.headline5,
                overflow: TextOverflow.ellipsis,
                maxLines: 2,
              ),
              Divider(),
              Row(
                children: [
                  Icon(
                    Icons.calendar_today,
                    size: Theme.of(context).textTheme.bodyText1.fontSize,
                  ),
                  Text(
                    ' ${_humanReadableFormatter(event.schedule.start)} - ${_humanReadableFormatter(event.schedule.end)}',
                    style: TextStyle(color: Theme.of(context).errorColor),
                  ),
                ],
              ),
              Divider(),
              Text(
                event.description,
                overflow: TextOverflow.ellipsis,
                maxLines: 3,
              ),
            ],
          ),
        ),
      ),
    );
  }
}

class NoEventsMessage extends StatelessWidget {
  final bool _isLive;

  NoEventsMessage({@required isLive}) : _isLive = isLive;

  @override
  Widget build(BuildContext context) {
    return Column(
      mainAxisAlignment: MainAxisAlignment.center,
      children: [
        Text(
          _isLive
              ? 'None of your events are live currently, please come back later...'
              : "You have no upcoming events...",
          style: Theme.of(context).textTheme.subtitle1.copyWith(fontSize: 20),
          textAlign: TextAlign.center,
        ),
        Row(
          mainAxisAlignment: MainAxisAlignment.center,
          crossAxisAlignment: CrossAxisAlignment.baseline,
          children: [
            Text(
              _isLive ? 'or ' : 'please ',
              style: Theme.of(context)
                  .textTheme
                  .subtitle1
                  .copyWith(fontSize: 20, fontStyle: FontStyle.italic),
            ),
            InkWell(
              onTap: () => Navigator.pushReplacementNamed(
                  context, registerEventScreenRoute),
              child: Text(
                'register for a new event.',
                style: Theme.of(context).textTheme.subtitle1.copyWith(
                      color: Theme.of(context).accentColor,
                      fontSize: 20,
                      fontStyle: FontStyle.italic,
                    ),
              ),
            ),
          ],
        ),
      ],
    );
  }
}

String _humanReadableFormatter(DateTime dateTime) {
  String formatted = DateFormat('E, MMM d HH:mm').format(dateTime);
  if (dateTime.year != DateTime.now().year) {
    formatted += ' ' + dateTime.year.toString();
  }
  return formatted;
}
