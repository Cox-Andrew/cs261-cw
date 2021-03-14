import 'package:flutter/material.dart';

import 'package:moodlysis_app/screens/timeline/event/comprehensive_feedback.dart';
import 'package:moodlysis_app/screens/timeline/event/general_feedback.dart';
import 'package:moodlysis_app/utils/events.dart';
import 'package:moodlysis_app/globals.dart' as globals;

class EventScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    bool _live = globals.currentEvent.schedule.start.isBefore(DateTime.now());

    return Scaffold(
      appBar: AppBar(
        title: Text("Event"),
      ),
      body: Padding(
        padding: const EdgeInsets.all(20),
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Padding(
              padding: const EdgeInsets.only(bottom: 10),
              child: RichText(
                text: TextSpan(children: [
                  TextSpan(
                      text: 'Welcome to ',
                      style: TextStyle(fontWeight: FontWeight.normal)),
                  TextSpan(
                      text: globals.currentEvent.title,
                      style: TextStyle(color: Theme.of(context).primaryColor)),
                  TextSpan(
                      text: '!',
                      style: TextStyle(fontWeight: FontWeight.normal)),
                ], style: Theme.of(context).textTheme.headline4),
                maxLines: 3,
                overflow: TextOverflow.ellipsis,
              ),
            ),
            Text(
              globals.currentEvent.description,
              style: Theme.of(context).textTheme.subtitle1,
              maxLines: 3,
              overflow: TextOverflow.ellipsis,
            ),
            Divider(),
            Row(children: [
              Icon(Icons.calendar_today,
                  size: Theme.of(context).textTheme.subtitle1.fontSize),
              Text(' ${globals.currentEvent.schedule.humanReadable}',
                  style: Theme.of(context)
                      .textTheme
                      .subtitle1
                      .copyWith(color: Theme.of(context).errorColor)),
            ]),
            Divider(),
            _live
                ? Expanded(child: FeedbackTabs())
                : Expanded(
                    child: Align(
                        alignment: FractionalOffset(0.5, 0.3),
                        child: Text(
                          "This event hasn't started yet.",
                          style: Theme.of(context).textTheme.headline3,
                          textAlign: TextAlign.center,
                        ))),
          ],
        ),
      ),
    );
  }
}

class FeedbackTabs extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return DefaultTabController(
      length: 2,
      child: Scaffold(
        appBar: TabBar(tabs: [
          Tab(
              icon: Icon(Icons.sentiment_satisfied_alt,
                  color: Theme.of(context).primaryColor)),
          Tab(
              icon:
                  Icon(Icons.feedback, color: Theme.of(context).primaryColor)),
        ]),
        body: TabBarView(children: [
          SingleChildScrollView(child: GeneralFeedbackForm()),
          SingleChildScrollView(child: ComprehensiveFeedbackForm()),
        ]),
      ),
    );
  }
}