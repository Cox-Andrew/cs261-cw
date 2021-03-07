import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

import 'package:moodlysis_app/models/event.dart';
import 'package:moodlysis_app/screens/event/arguments.dart';
import 'package:moodlysis_app/test_data.dart';

class Timeline extends StatefulWidget {
  @override
  _TimelineState createState() => _TimelineState();
}

class _TimelineState extends State<Timeline> {
  final List<Event> _testEvents = generateEvents(3);

  @override
  void initState() {
    _testEvents.add(Event("Apple conference about pears", "Hi, this is a 130 character-ish description. We will be examining the differences between our favourite fruits.", DateTime.now(), DateTime.now().add(Duration(hours: 1))));
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Event Timeline"),
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
                        Text("Live events ",
                            style: Theme.of(context).textTheme.headline4),
                        Icon(
                            Icons.live_tv,
                            color: Colors.red,
                            size: Theme.of(context).textTheme.headline4.fontSize
                        )
                      ],
                    ),
                  ),
                  Expanded(
                    child: ListView.builder(
                        scrollDirection: Axis.horizontal,
                        itemCount: _testEvents.length,
                        itemBuilder: (context, i) {
                          Event event = _testEvents[i];
                          return SizedBox(
                              width: MediaQuery.of(context).size.width*3/4,
                              child: _EventCard(event)
                          );
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
                        Text("Upcoming events ",
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
                    child: ListView.builder(
                        itemCount: _testEvents.length,
                        itemBuilder: (context, i) {
                          Event event = _testEvents[i];
                          return SizedBox(
                              height: MediaQuery.of(context).size.height * 1/5,
                              child: _EventCard(event)
                          );
                        }),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
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
            Scaffold.of(context).removeCurrentSnackBar();
            Scaffold.of(context).showSnackBar(SnackBar(
                content: Text("Attending ${event.title}")));
            Future.delayed(Duration(milliseconds: 500), () {
              // 5s over, navigate to a new page
              Navigator.pushNamed(context, "/event",
                  arguments: EventScreenArgs(event));
            });
          }
        },
        child: Padding(
          padding: const EdgeInsets.all(12),
          child: Column(
            crossAxisAlignment: CrossAxisAlignment.start,
            children: [
              Text(event.title, style: Theme.of(context).textTheme.headline5, overflow: TextOverflow.ellipsis, maxLines: 2,),
              Divider(),
              Row(
                children: [
                  Icon(Icons.calendar_today, size: Theme.of(context).textTheme.bodyText1.fontSize,),
                  Text(" ${_humanReadableFormatter(event.schedule.start)} - ${_humanReadableFormatter(event.schedule.end)}", style: TextStyle(color: Theme.of(context).errorColor),),
                ],
              ),
              Divider(),
              Text(event.description, overflow: TextOverflow.ellipsis, maxLines: 3,),
            ],
          ),
        ),
      ),
    );
  }
}

String _humanReadableFormatter(DateTime dateTime) {
  String formatted = DateFormat("E, MMM d hh:mm").format(dateTime);
  if (dateTime.year != DateTime.now().year) {
    formatted += " " + dateTime.year.toString();
  }
  return formatted;
}