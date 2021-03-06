import 'package:flutter/material.dart';

import 'package:moodlysis_app/screens/event/arguments.dart';

final List<String> testEvents = ["A", "B", "C", "D"];

class Timeline extends StatefulWidget {
  @override
  _TimelineState createState() => _TimelineState();
}

class _TimelineState extends State<Timeline> {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Event Timeline"),
      ),
      body: SafeArea(
        child: Column(
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
                  itemCount: testEvents.length,
                  itemBuilder: (context, i) {
                    String event = testEvents[i];
                    return SizedBox(
                      width: 250,
                      child: Card(
                        child: InkWell(
                          onTap: () {
                            Scaffold.of(context).removeCurrentSnackBar();
                            Scaffold.of(context).showSnackBar(SnackBar(
                                content: Text("attending event " + event)));
                            Future.delayed(Duration(milliseconds: 500), () {
                              // 5s over, navigate to a new page
                              Navigator.pushNamed(context, "/event",
                                  arguments: EventScreenArgs(event));
                            });
                          },
                          child: Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: Text(
                              event,
                              style: Theme.of(context).textTheme.headline5,
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
                  itemCount: testEvents.length,
                  itemBuilder: (context, i) {
                    String event = testEvents[i];
                    return SizedBox(
                      height: 200,
                      child: Card(
                        child: InkWell(
                          onTap: () {
                            Scaffold.of(context).removeCurrentSnackBar();
                            Scaffold.of(context).showSnackBar(SnackBar(
                                content: Text("attending event " + event)));
                          },
                          child: Padding(
                            padding: const EdgeInsets.all(8.0),
                            child: Text(
                              event,
                              style: Theme.of(context).textTheme.headline5,
                            ),
                          ),
                        ),
                      ),
                    );
                  }),
            ),
          ],
        ),
      ),
    );
  }
}
