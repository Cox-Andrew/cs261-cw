import 'dart:io';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

import 'package:moodlysis_app/models/form.dart';
import 'package:moodlysis_app/models/question.dart';
import 'package:moodlysis_app/services/forms.dart';
import 'package:moodlysis_app/globals.dart' as globals;
import 'package:moodlysis_app/utils/error_handlers.dart';

class ComprehensiveFeedbackForm extends StatefulWidget {
  @override
  ComprehensiveFeedbackFormState createState() {
    return ComprehensiveFeedbackFormState();
  }
}

class ComprehensiveFeedbackFormState extends State<ComprehensiveFeedbackForm> {
  final _formKey = GlobalKey<FormState>();
  EventForm _activeForm;

  @override
  void initState() {
    getActiveForm(http.Client(), globals.currentEvent)
        .then((form) => setState(() => _activeForm = form))
        .catchError((e, s) => handleConnectionError(context, e, s),
            test: (e) => e is SocketException)
        .catchError((e, s) => handleError(context, e, s));
    super.initState();
  }

  @override
  Widget build(BuildContext context) {
    return Form(
        key: _formKey,
        child: _activeForm == null
            ? Text("The host has not made any comprehensive forms available at this moment")
            : Column(
                children: _activeForm.questions
                    .map((Question q) => Text('${q.text} (${q.type})'))
                    .toList()));
  }
}