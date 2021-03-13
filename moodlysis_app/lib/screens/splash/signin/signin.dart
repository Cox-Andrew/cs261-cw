import 'dart:async';
import 'dart:io';
import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:http/http.dart' as http;
import 'package:moodlysis_app/constants.dart';

import 'package:moodlysis_app/models/user.dart';
import 'package:moodlysis_app/services/authentication.dart';
import 'package:moodlysis_app/globals.dart' as globals;
import 'package:moodlysis_app/services/exceptions.dart';

class SignInScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Sign In"),
      ),
      body: Padding(
        padding: const EdgeInsets.all(20),
        child: Column(
          children: [
            Padding(
              padding: const EdgeInsets.only(top: 60, bottom: 20),
              child: Text(
                "Welcome back.",
                style: Theme.of(context).textTheme.headline3,
              ),
            ),
            Expanded(
              child: Align(
                  alignment: FractionalOffset(0, 0.3), child: SignInForm()),
            ),
          ],
        ),
      ),
    );
  }
}

class SignInForm extends StatefulWidget {
  @override
  SignInFormState createState() {
    return SignInFormState();
  }
}

class SignInFormState extends State<SignInForm> {
  // Create a global key that uniquely identifies the Form widget
  // and allows validation of the form.
  final _formKey = GlobalKey<FormState>();
  final Map<String, String> _formData = Map<String, String>();
  FocusNode _focusNode;
  bool _loading = false;

  @override
  Widget build(BuildContext context) {
    _focusNode = FocusScope.of(context);

    return Form(
      key: _formKey,
      child: Column(mainAxisSize: MainAxisSize.min, children: [
        _buildEmailField,
        _buildPasswordField,
        Padding(
          padding: const EdgeInsets.only(top: 40),
          child: ElevatedButton(
            onPressed: _loading ? null : _submitSignIn,
            style: ElevatedButton.styleFrom(
              padding: EdgeInsets.all(10),
              shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.all(Radius.circular(25))),
              minimumSize: Size(300, 50),
            ),
            child: Text("Sign in", style: TextStyle(fontSize: 30)),
          ),
        ),
      ]),
    );
  }

  Widget get _buildEmailField {
    return TextFormField(
      decoration:
          const InputDecoration(icon: Icon(Icons.email), labelText: "Email"),
      keyboardType: TextInputType.emailAddress,
      autofocus: true,
      onEditingComplete: () => _focusNode.nextFocus(),
      onSaved: (String val) => _formData["email"] = val,
      inputFormatters: [
        FilteringTextInputFormatter(RegExp(r'\s'), allow: false)
      ],
      validator: _validateRequired,
    );
  }

  Widget get _buildPasswordField {
    return TextFormField(
      obscureText: true,
      decoration: const InputDecoration(
        icon: Icon(Icons.lock),
        labelText: "Password",
      ),
      onEditingComplete: _submitSignIn,
      onSaved: (String val) => _formData["password"] = val,
      validator: _validateRequired,
    );
  }

  String _validateRequired(String value) => value.isEmpty ? 'Required' : null;

  void _submitSignIn() {
    if (_formKey.currentState.validate()) {
      _formKey.currentState.save();

      setState(() => _loading = true);
      authenticateUser(http.Client(), _formData["email"], _formData["password"])
          .then((int id) => getUser(http.Client(), id))
          .then((User user) => _handleAuthenticationSuccess(user))
          .catchError((e, s) => _handleAuthenticationError(e, s),
              test: (e) => e is AuthenticationException)
          .catchError((e, s) => _handleConnectionError(e, s),
              test: (e) => e is SocketException)
          .catchError((e, s) => _handleError(e, s))
          .whenComplete(() => setState(() => _loading = false));
    }
  }

  //TODO: code duplication restructure project and resolve
  void _handleAuthenticationSuccess(User user) {
    globals.currentUser = user;

    Scaffold.of(context).removeCurrentSnackBar();
    Scaffold.of(context).showSnackBar(SnackBar(
      content: Text(
          'Authenticated as ${user.name}, id: ${user.id}, email: ${user.email}'),
      backgroundColor: Colors.green,
    ));

    _focusNode.unfocus();
    Future.delayed(
        Duration(milliseconds: 500),
        () => Navigator.pushNamedAndRemoveUntil(
            context, timelineScreenRoute, (r) => false));
  }

  void _handleAuthenticationError(dynamic error, StackTrace stackTrace) {
    // print("Error: $error");
    // print("StackTrace: $stackTrace");

    Scaffold.of(context).removeCurrentSnackBar();
    Scaffold.of(context).showSnackBar(SnackBar(
      content: RichText(
          text: TextSpan(children: [
        TextSpan(text: 'Invalid credentials', style: TextStyle(fontWeight: FontWeight.bold)),
        TextSpan(text: ', please try again.'),
      ])),
      backgroundColor: Theme.of(context).errorColor,
    ));
  }

  void _handleConnectionError(dynamic error, StackTrace stackTrace) {
    // print("Error: $error");
    // print("StackTrace: $stackTrace");

    Scaffold.of(context).removeCurrentSnackBar();
    Scaffold.of(context).showSnackBar(SnackBar(
      content: RichText(
          text: TextSpan(children: [
            TextSpan(text: 'Connection failed', style: TextStyle(fontWeight: FontWeight.bold)),
            TextSpan(text: ', please try again.'),
          ])),
      backgroundColor: Theme.of(context).errorColor,
    ));
  }

  //TODO: implement specific error handling for failed connection etc.
  void _handleError(dynamic error, StackTrace stackTrace) {
    print("Error: $error");
    print("StackTrace: $stackTrace");

    Scaffold.of(context).removeCurrentSnackBar();
    Scaffold.of(context).showSnackBar(SnackBar(
      content: RichText(
          text: TextSpan(children: [
            TextSpan(text: 'Something went wrong', style: TextStyle(fontWeight: FontWeight.bold)),
            TextSpan(text: ', please try again.'),
          ])),
      backgroundColor: Theme.of(context).errorColor,
    ));
  }
}
