import 'package:flutter/material.dart';

import 'package:moodlysis_app/screens/timeline/event/arguments.dart';

class EventScreen extends StatelessWidget {
  static const route = "/event";

  @override
  Widget build(BuildContext context) {
    final EventScreenArgs args = ModalRoute.of(context).settings.arguments;

    return Scaffold(
      appBar: AppBar(
        title: Text("Event"),
      ),
      body: Padding(
        padding: const EdgeInsets.all(16.0),
        child: Column(
          children: [
            Text(
              "Hey! Welcome to ${args.event.title}",
              style: TextStyle(fontWeight: FontWeight.bold, fontSize: 30),
            ),
            SizedBox(
              height: 10,
            ),
            FeedbackForm(),
          ],
        ),
      ),
    );
  }
}

class FeedbackForm extends StatefulWidget {
  @override
  FeedbackFormState createState() {
    return FeedbackFormState();
  }
}

class FeedbackFormState extends State<FeedbackForm> {
  // Create a global key that uniquely identifies the Form widget
  // and allows validation of the form.
  final _formKey = GlobalKey<FormState>();

  @override
  Widget build(BuildContext context) {
    return Form(
        key: _formKey,
        child: Column(children: <Widget>[
          Column(
            children: [
              Text(
                  "This is a short question This is a short question This is a short question This is a short question"),
              TextFormField(
                decoration: const InputDecoration(
                  labelText: "Response",
                  border: OutlineInputBorder(),
                ),
                keyboardType: TextInputType.text,
                validator: (value) {
                  if (value.isEmpty) {
                    return 'Required';
                  }
                  return null;
                },
              ),
            ],
          ),
          TextFormField(
            decoration: const InputDecoration(
              labelText: "Response",
            ),
            maxLines: 3,
            keyboardType: TextInputType.multiline,
            validator: (value) {
              if (value.isEmpty) {
                return 'Required';
              }
              return null;
            },
          ),
          ElevatedButton(
            onPressed: () {
              if (_formKey.currentState.validate()) {
                Scaffold.of(context)
                    .showSnackBar(SnackBar(content: Text('Processing Data')));
              }
            },
            child: Text('Submit'),
          ),
        ]));
  }
}
