import 'package:flutter/material.dart';

import 'package:moodlysis_app/models/event.dart';
import 'package:moodlysis_app/screens/event/arguments.dart';
import 'package:moodlysis_app/test_data.dart';

class Timeline extends StatefulWidget {
  @override
  _TimelineState createState() => _TimelineState();
}

class _TimelineState extends State<Timeline> {
  final List<Event> _testEvents = generateEvents(100);

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Event Timeline"),
      ),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Row(
              children: [
                Text("Live events",
                    style: Theme.of(context).textTheme.headline4),
                SizedBox(width: 10),
                Icon(
                  Icons.live_tv,
                  color: Colors.red,
                  size: Theme.of(context).textTheme.headline4.fontSize
                )
              ],
            ),
          ),
          SizedBox(
            height: 200,
            width: 800,
            child: ListView.builder(
                padding: EdgeInsets.all(16),
                scrollDirection: Axis.horizontal,
                itemCount: _testEvents.length,
                itemBuilder: (context, i) {
                  Event event = _testEvents[i];
                  return SizedBox(
                    width: 250,
                    child: Card(
                      child: InkWell(
                        onTap: () {
                          Scaffold.of(context).removeCurrentSnackBar();
                          Scaffold.of(context).showSnackBar(SnackBar(
                              content: Text("Attending ${event.title}")));
                          Future.delayed(Duration(milliseconds: 500), () {
                            // 5s over, navigate to a new page
                            Navigator.pushNamed(context, "/event",
                                arguments: EventScreenArgs(event));
                          });
                        },
                        child: Padding(
                          padding: const EdgeInsets.all(8.0),
                          child: Column(
                            children: [
                              Text(event.title, style: Theme.of(context).textTheme.headline6,),
                              Text(event.description),
                              Text("Start: ${event.schedule.start}"),
                              Text("End: ${event.schedule.end}")
                            ],
                          ),
                        ),
                      ),
                    ),
                  );
                }),
          ),
          Padding(
            padding: const EdgeInsets.all(8.0),
            child: Row(
              children: [
                Text("Upcoming events",
                    style: Theme.of(context).textTheme.headline4),
                SizedBox(width: 10),
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
                padding: EdgeInsets.all(16),
                itemCount: _testEvents.length,
                itemBuilder: (context, i) {
                  Event event = _testEvents[i];
                  return SizedBox(
                    height: 200,
                    child: Card(
                      child: InkWell(
                        onTap: () {
                          Scaffold.of(context).removeCurrentSnackBar();
                          Scaffold.of(context).showSnackBar(SnackBar(
                              content: Text("attending event " + event.toString())));
                        },
                        child: Padding(
                          padding: const EdgeInsets.all(8.0),
                          child: Column(
                            children: [
                              Text(event.title, style: Theme.of(context).textTheme.headline6,),
                              Text(event.description),
                              Text("Start: ${event.schedule.start}"),
                              Text("End: ${event.schedule.end}")
                            ],
                          ),
                        ),
                      ),
                    ),
                  );
                }),
          ),
        ],
      ),
    );
  }
}
