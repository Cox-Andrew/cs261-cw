import 'dart:io';

import 'package:flutter/material.dart';
import 'package:http/http.dart' as http;

import 'package:moodlysis_app/components/forms.dart';
import 'package:moodlysis_app/constants/constants.dart';
import 'package:moodlysis_app/services/forms.dart';
import 'package:moodlysis_app/utils/error_handlers.dart';
import 'package:moodlysis_app/globals.dart' as globals;

class GeneralFeedbackForm extends StatefulWidget {
  @override
  GeneralFeedbackFormState createState() {
    return GeneralFeedbackFormState();
  }
}

class GeneralFeedbackFormState extends State<GeneralFeedbackForm> {
  final _formKey = GlobalKey<FormState>();
  final _moodKey = GlobalKey<MoodSelectorState>();
  final Map<String, dynamic> _formData = Map<String, dynamic>();
  bool _loading = false;

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
          MoodSelector(
            label: 'Choose an emoji which represents your mood (optional)',
            key: _moodKey,
          ),
          _commentField,
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
        ],
      ),
    );
  }

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
      onPressed: _loading ? null : _submitGeneralFeedback,
      child: Text(
        'Submit',
        style:
        TextStyle(fontSize: 30, fontWeight: FontWeight.normal),
      ),
    );
  }

  TextFormField get _commentField {
    return TextFormField(
      decoration: const InputDecoration(
        labelText: "Comment (optional)",
        border: OutlineInputBorder(),
      ),
      maxLength: 100,
      maxLengthEnforced: true,
      keyboardType: TextInputType.multiline,
      maxLines: 4,
      onSaved: (String val) => _formData['comment'] = val,
      validator: _validateComment,
    );
  }

  String _validateComment(String comment) {
    if (comment.length > 100) return 'Max length 100 characters';
    return null;
  }

  void _submitGeneralFeedback() {
    if (_formKey.currentState.validate()) {
      _formKey.currentState.save();
      //TODO: FormField extension to allow save
      _formData['mood'] = _moodKey.currentState.value;

      if (_formData['mood'] == null && _formData['comment'] == '') return;

      setState(() => _loading = true);
      if (_formData['mood'] != null) {
        sendMood(http.Client(), sentimentValues[_formData['mood']],
                globals.currentEvent.eventID)
            .then((v) => _handleMoodSuccess())
            .catchError((e, s) => handleConnectionError(context, e, s),
                test: (e) => e is SocketException)
            .catchError((e, s) => handleError(context, e, s));
      }
      if (_formData['comment'] != '') {
        sendFeedback(
                http.Client(),
                globals.currentUser,
                globals.currentEvent.eventID,
                globals.currentEvent.eventFormIDs[0],
                0,
                _formData['comment'],
                _formData['isAnonymous'])
            .then((int answerID) => _handleFeedbackSuccess(answerID))
            .catchError((e, s) => handleConnectionError(context, e, s),
                test: (e) => e is SocketException)
            .catchError((e, s) => handleError(context, e, s));
      }
      setState(() => _loading = false);
    }
  }

  void _handleMoodSuccess() {
    Scaffold.of(context).showSnackBar(SnackBar(
      content: Text('Mood successfully submitted'),
      backgroundColor: Colors.green,
    ));
  }

  void _handleFeedbackSuccess(int answerID) {
    Scaffold.of(context).showSnackBar(SnackBar(
      content: Text('Feedback successfully submitted answerID: $answerID'),
      backgroundColor: Colors.green,
    ));
  }
}

class MoodSelector extends StatefulWidget {
  final String label;

  MoodSelector({this.label, Key key}) : super(key: key);

  @override
  MoodSelectorState createState() => MoodSelectorState();
}

class MoodSelectorState extends State<MoodSelector> {
  final List<bool> _isSelected = [false, false, false, false, false];
  Sentiment value;

  @override
  Widget build(BuildContext context) {
    return Column(
      children: [
        this.widget.label != null
            ? Text(
                this.widget.label,
                style: Theme.of(context).inputDecorationTheme.labelStyle,
              )
            : null,
        //TODO: lots of pain has gone into trying to make this a form field, should still be done
        Padding(
          padding: const EdgeInsets.only(top: 8, bottom: 8),
          child: ToggleButtons(
            borderRadius: BorderRadius.all(Radius.circular(4)),
            borderColor: Theme.of(context).hintColor,
            selectedBorderColor: Theme.of(context).primaryColor,
            constraints: BoxConstraints(
                minWidth: (MediaQuery.of(context).size.width - 46) / 5,
                minHeight: (MediaQuery.of(context).size.width - 46) / 10),
            children: [
              Icon(
                Icons.sentiment_very_dissatisfied,
                color: Colors.red,
              ),
              Icon(
                Icons.sentiment_dissatisfied,
                color: Colors.orange,
              ),
              Icon(
                Icons.sentiment_neutral,
                color: Colors.yellow,
              ),
              Icon(
                Icons.sentiment_satisfied,
                color: Colors.lightGreen,
              ),
              Icon(
                Icons.sentiment_very_satisfied,
                color: Colors.green,
              ),
            ],
            onPressed: (int selectedIndex) {
              setState(() {
                for (int i = 0; i < _isSelected.length; i++) {
                  _isSelected[i] = i == selectedIndex ? !_isSelected[i] : false;
                }
                value = _isSelected[selectedIndex]
                    ? Sentiment.values[selectedIndex]
                    : null;
              });
            },
            isSelected: _isSelected,
          ),
        ),
      ],
    );
  }
}
