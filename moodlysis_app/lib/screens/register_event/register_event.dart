import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:http/http.dart' as http;

import 'package:moodlysis_app/components/navigation.dart';
import 'package:moodlysis_app/models/event.dart';
import 'package:moodlysis_app/services/events.dart';
import 'package:moodlysis_app/globals.dart' as globals;
import 'package:moodlysis_app/services/exceptions.dart';

class RegisterEventScreen extends StatelessWidget {
  static const route = "/register_event";

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text('Event Registration'),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            SizedBox(
              width: MediaQuery.of(context).size.width * 3 / 5,
              height: MediaQuery.of(context).size.height * 1 / 5,
              child: Card(
                child: Padding(
                  padding: EdgeInsets.all(16),
                  child: JoinForm(),
                ),
              ),
            ),
          ],
        ),
      ),
      bottomNavigationBar:
          MoodlysisBottomNavigationBar(RegisterEventScreen.route),
    );
  }
}

class JoinForm extends StatefulWidget {
  @override
  _JoinFormState createState() => _JoinFormState();
}

class _JoinFormState extends State<JoinForm> {
  final _formKey = GlobalKey<FormState>();
  String _inviteCode;
  bool _loading = false;

  @override
  Widget build(BuildContext context) {
    return Form(
      key: _formKey,
      child: Column(
        children: [
          Padding(
            padding: const EdgeInsets.only(bottom: 8.0),
            child: _inviteCodeField,
          ),
          Expanded(
            child: _submitButton,
          ),
        ],
      ),
    );
  }

  Widget get _inviteCodeField {
    return TextFormField(
      decoration: InputDecoration(
        hintText: "Invite Code",
      ),
      style: TextStyle(
        fontSize: 40,
        fontWeight: FontWeight.w800,
      ),
      textAlign: TextAlign.center,
      textCapitalization: TextCapitalization.characters,
      // autofocus: true,
      //TODO: doesn't limit due to composing text issue
      maxLength: 8,
      maxLengthEnforced: true,
      inputFormatters: [
        FilteringTextInputFormatter(RegExp(r'[A-Z, 0-9]'), allow: true),
        FilteringTextInputFormatter(RegExp(r'\s'), allow: false)
      ],
      onEditingComplete: _submitRegistration,
      validator: _validateInviteCode,
      autovalidateMode: AutovalidateMode.onUserInteraction,
      onSaved: (code) => _inviteCode = code,
    );
  }

  String _validateInviteCode(String inviteCode) {
    if (inviteCode.length != 8) return "Must be 8 characters";
    return null;
  }

  Widget get _submitButton {
    return SizedBox(
      width: double.infinity,
      child: ElevatedButton(
        onPressed: _loading ? null : _submitRegistration,
        child: Text(
          "Register",
          style: TextStyle(fontSize: 30),
        ),
      ),
    );
  }

  void _submitRegistration() {
    if (_formKey.currentState.validate()) {
      _formKey.currentState.save();

      setState(() => _loading = true);
      registerForEvent(http.Client(), _inviteCode, globals.currentUser)
          .then((eventID) => getEvent(http.Client(), eventID))
          .then((event) => _handleRegistrationSuccess(event))
          .catchError((e, s) => _handleInvalidCode(e, s),
              test: (e) => e is ResultNotFoundException)
          .catchError((e, s) => _handleError(e, s))
          .whenComplete(() => setState(() => _loading = false));
    }
  }

  void _handleRegistrationSuccess(Event event) {
    FocusScope.of(context).unfocus();
    _formKey.currentState.reset();

    Scaffold.of(context).removeCurrentSnackBar();
    Scaffold.of(context).showSnackBar(SnackBar(
      content: RichText(
          text: TextSpan(children: [
        TextSpan(text: 'Successfully registered for '),
        TextSpan(
            text: event.title, style: TextStyle(fontWeight: FontWeight.bold)),
      ])),
      backgroundColor: Colors.green,
    ));
  }

  void _handleInvalidCode(dynamic error, StackTrace stackTrace) {
    print("Error: $error");
    print("StackTrace: $stackTrace");

    Scaffold.of(context).removeCurrentSnackBar();
    Scaffold.of(context).showSnackBar(SnackBar(
      content: RichText(
          text: TextSpan(children: [
        TextSpan(
            text: 'Invalid code',
            style: TextStyle(fontWeight: FontWeight.bold)),
        TextSpan(text: ', please try again.'),
      ])),
      backgroundColor: Theme.of(context).errorColor,
    ));
  }

  void _handleError(dynamic error, StackTrace stackTrace) {
    print("Error: $error");
    print("StackTrace: $stackTrace");

    Scaffold.of(context).removeCurrentSnackBar();
    Scaffold.of(context).showSnackBar(SnackBar(
      content: RichText(
          text: TextSpan(children: [
        TextSpan(
            text: 'Something went wrong',
            style: TextStyle(fontWeight: FontWeight.bold)),
        TextSpan(text: ', please try again.'),
      ])),
      backgroundColor: Theme.of(context).errorColor,
    ));
  }
}
