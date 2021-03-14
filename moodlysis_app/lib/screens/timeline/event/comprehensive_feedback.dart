import 'package:flutter/material.dart';

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