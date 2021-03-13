import 'package:flutter/material.dart';

import 'package:moodlysis_app/screens/timeline/event/arguments.dart';
import 'package:moodlysis_app/utils/events.dart';

class EventScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    final EventScreenArgs args = ModalRoute.of(context).settings.arguments;
    bool _live = args.event.schedule.start.isBefore(DateTime.now());

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
                      text: args.event.title,
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
              args.event.description,
              style: Theme.of(context).textTheme.subtitle1,
              maxLines: 3,
              overflow: TextOverflow.ellipsis,
            ),
            Divider(),
            Row(children: [
              Icon(Icons.calendar_today,
                  size: Theme.of(context).textTheme.subtitle1.fontSize),
              Text(' ${args.event.schedule.humanReadable}',
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
          GeneralFeedbackForm(),
          ComprehensiveFeedbackForm(),
        ]),
      ),
    );
  }
}

class ComprehensiveFeedbackForm extends StatefulWidget {
  @override
  ComprehensiveFeedbackFormState createState() {
    return ComprehensiveFeedbackFormState();
  }
}

class ComprehensiveFeedbackFormState extends State<ComprehensiveFeedbackForm> {
  final _formKey = GlobalKey<FormState>();

  @override
  Widget build(BuildContext context) {
    return Form(
      key: _formKey,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Padding(
            padding: const EdgeInsets.only(top: 8.0),
            child: Text("Comprehensive feedback",
                style: Theme.of(context).textTheme.headline5),
          ),
          Divider(),
          Column(
            children: [
              Text(
                  "This is a short question This is a short question This is a short question This is a short question"),
              TextFormField(
                decoration: const InputDecoration(
                  labelText: "Response",
                  border: OutlineInputBorder(),
                ),
              ),
            ],
          ),
          ElevatedButton(
            onPressed: () {},
            child: Text('Submit'),
          ),
        ],
      ),
    );
  }
}

class GeneralFeedbackForm extends StatefulWidget {
  @override
  GeneralFeedbackFormState createState() {
    return GeneralFeedbackFormState();
  }
}

class GeneralFeedbackFormState extends State<GeneralFeedbackForm> {
  final _formKey = GlobalKey<FormState>();

  @override
  Widget build(BuildContext context) {
    return Form(
      key: _formKey,
      child: Column(
        crossAxisAlignment: CrossAxisAlignment.start,
        children: [
          Padding(
            padding: const EdgeInsets.only(top: 8.0),
            child: Text("General feedback",
                style: Theme.of(context).textTheme.headline5),
          ),
          Divider(),
          Column(
            children: [
              Text(
                  "This is a short question This is a short question This is a short question This is a short question"),
              TextFormField(
                decoration: const InputDecoration(
                  labelText: "Response",
                  border: OutlineInputBorder(),
                ),
              ),
            ],
          ),
          ElevatedButton(
            onPressed: () {},
            child: Text('Submit'),
          ),
        ],
      ),
    );
  }
}
