import 'dart:io';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;
import 'package:moodlysis_app/components/forms.dart';

import 'package:moodlysis_app/models/form.dart';
import 'package:moodlysis_app/models/question.dart';
import 'package:moodlysis_app/services/events.dart';
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
  final Map<String, dynamic> _formData = Map<String, dynamic>();
  final Map<int, String> _answers = Map<int, String>();
  EventForm _activeForm;
  bool _loading = false;

  @override
  void initState() {
    http.Client client = http.Client();
    getEvent(client, globals.currentEvent.eventID)
        .then((event) => globals.currentEvent = event)
        .then((_) => getActiveForm(client, globals.currentEvent))
        .then((form) => setState(() => _activeForm = form))
        .catchError((e, s) => handleConnectionError(context, e, s),
            test: (e) => e is SocketException)
        .catchError((e, s) => handleError(context, e, s));
    super.initState();
  }

  @override
  Widget build(BuildContext context) =>
      _activeForm == null ? _noForm : _buildForm;

  Widget get _noForm {
    return Text(
        "The host has not made any comprehensive forms available at this moment");
  }

  Widget get _buildForm {
    return Form(
        key: _formKey,
        child: Column(children: [
          _formTitle,
          _formDescription,
          Divider(),
          Column(children: _buildQuestionFields),
          Row(
            crossAxisAlignment: CrossAxisAlignment.baseline,
            children: [
              Expanded(
                flex: 4,
                child: _submitButton,
              ),
              Expanded(
                flex: 5,
                child: _anonymityButton,
              ),
            ],
          ),
        ]));
  }

  Widget get _formTitle {
    return Text(
      _activeForm.title,
      textAlign: TextAlign.left,
      style: Theme.of(context).textTheme.headline6,
    );
  }

  Widget get _formDescription {
    return Text(
      _activeForm.description,
      textAlign: TextAlign.left,
      style: Theme.of(context).textTheme.subtitle1,
    );
  }

  List<Widget> get _buildQuestionFields {
    return _activeForm.questions
        .map((Question q) => Card(
              child: Padding(
                padding: const EdgeInsets.all(8.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Padding(
                      padding: const EdgeInsets.only(bottom: 8.0),
                      child: Text(q.text, style: TextStyle(fontSize: 15)),
                    ),
                    TextFormField(
                      decoration: const InputDecoration(
                        hintText: 'Your answer',
                        border: OutlineInputBorder(),
                      ),
                      maxLength: 100,
                      maxLengthEnforced: true,
                      keyboardType: q.type == QuestionType.long
                          ? TextInputType.multiline
                          : TextInputType.text,
                      maxLines: q.type == QuestionType.long ? 4 : 1,
                      validator: _validateRequired,
                      autovalidateMode: AutovalidateMode.onUserInteraction,
                      onSaved: (String answer) => _answers[q.id] = answer,
                    ),
                  ],
                ),
              ),
            ))
        .toList();
  }

  //TODO: reduce code duplication
  CheckboxFormField get _anonymityButton {
    return CheckboxFormField(
      title: const Text(
        'Anonymous',
        style: TextStyle(fontSize: 20),
        textAlign: TextAlign.right,
      ),
      onSaved: (bool val) => _formData['isAnonymous'] = val,
    );
  }

  ElevatedButton get _submitButton {
    return ElevatedButton(
      onPressed: _loading ? null : _submit,
      child: Text(
        'Submit',
        style: TextStyle(fontSize: 30, fontWeight: FontWeight.normal),
      ),
    );
  }

  String _validateRequired(String val) => val.isEmpty ? 'Required' : null;

  void _submit() {
    if (_formKey.currentState.validate()) {
      setState(() => _loading = true);
      _formKey.currentState.save();

      http.Client client = http.Client();
      Future.wait(_answers.entries
              .map((questionIDAnswers) => sendFeedback(
                  client,
                  globals.currentUser,
                  globals.currentEvent.eventID,
                  _activeForm.eventFormID,
                  questionIDAnswers.key,
                  questionIDAnswers.value,
                  _formData['isAnonymous'])))
          .then((answerIDs) => _handleFeedbackSuccess(answerIDs))
          .catchError((e, s) => handleConnectionError(context, e, s),
              test: (e) => e is SocketException)
          .catchError((e, s) => handleError(context, e, s))
          .whenComplete(() => setState(() => _loading = false));
      _formKey.currentState.reset();
    }
  }

  void _handleFeedbackSuccess(List<int> answerIDs) {
    Scaffold.of(context).showSnackBar(SnackBar(
      content: Text('Feedback successfully submitted answerID: $answerIDs'),
      backgroundColor: Colors.green,
    ));
  }
}
