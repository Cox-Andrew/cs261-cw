import 'dart:io';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:http/http.dart' as http;
import 'package:moodlysis_app/models/user.dart';

import 'package:moodlysis_app/services/authentication.dart';
import 'package:moodlysis_app/globals.dart' as globals;

class SignUpScreen extends StatelessWidget {
  static const route = "/signup";

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Register"),
      ),
      body: Padding(
        padding: const EdgeInsets.all(20),
        child: Column(
          children: [
            Padding(
              padding: const EdgeInsets.only(top: 60, bottom: 20),
              child: Text(
                "Create an account.",
                style: Theme.of(context).textTheme.headline3,
              ),
            ),
            Expanded(
              child: Align(
                  alignment: FractionalOffset(0, 0.3), child: SignUpForm()),
            ),
          ],
        ),
      ),
    );
  }
}

class SignUpForm extends StatefulWidget {
  @override
  SignUpFormState createState() {
    return SignUpFormState();
  }
}

class SignUpFormState extends State<SignUpForm> {
  // Create a global key that uniquely identifies the Form widget
  // and allows validation of the form.
  final _formKey = GlobalKey<FormState>();
  final Map<String, String> _formData = Map<String, String>();
  FocusNode _focusNode;
  bool _loading = false;

  final TextEditingController _passwordController = TextEditingController();

  // From HTML5 standard
  static final RegExp _emailRegex = RegExp(
      r"^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$");

  @override
  Widget build(BuildContext context) {
    _focusNode = FocusScope.of(context);

    return Form(
      key: _formKey,
      child: Column(mainAxisSize: MainAxisSize.min, children: [
        _buildNameField,
        _buildEmailField,
        Row(
          children: [
            Expanded(child: _buildPasswordField),
            Expanded(child: _buildPasswordConfirmationField),
          ],
        ),
        Padding(
          padding: const EdgeInsets.only(top: 40),
          child: ElevatedButton(
            onPressed: _loading ? null : _submitRegistration,
            style: ElevatedButton.styleFrom(
              padding: EdgeInsets.all(10),
              shape: RoundedRectangleBorder(
                  borderRadius: BorderRadius.all(Radius.circular(25))),
              minimumSize: Size(300, 50),
            ),
            child: Text("Register", style: TextStyle(fontSize: 30)),
          ),
        ),
      ]),
    );
  }

  Widget get _buildNameField {
    return TextFormField(
      decoration: const InputDecoration(
        icon: Icon(Icons.person),
        labelText: "Name",
      ),
      keyboardType: TextInputType.name,
      autofocus: true,
      autovalidateMode: AutovalidateMode.onUserInteraction,
      onEditingComplete: () {
        _focusNode.nextFocus();
      },
      onSaved: (String val) => _formData["name"] = val.trim(),
      validator: _validateName,
    );
  }

  String _validateName(String name) {
    name = name.trim();
    if (name.isEmpty) return 'Required';
    if (name.length > 32) return '32 character limit';
    return null;
  }

  Widget get _buildEmailField {
    return TextFormField(
      decoration:
          const InputDecoration(icon: Icon(Icons.email), labelText: "Email"),
      keyboardType: TextInputType.emailAddress,
      autovalidateMode: AutovalidateMode.onUserInteraction,
      inputFormatters: [
        FilteringTextInputFormatter(RegExp(r'\s'), allow: false)
      ],
      onEditingComplete: () => _focusNode.nextFocus(),
      onSaved: (String val) => _formData["email"] = val,
      validator: _validateEmail,
    );
  }

  String _validateEmail(String email) {
    if (email.isEmpty) return 'Required';
    if (email.length > 50) return '50 character limit';
    if (!_emailRegex.hasMatch(email)) return 'Invalid email provided';
    return null;
  }

  Widget get _buildPasswordField {
    return TextFormField(
      obscureText: true,
      decoration: const InputDecoration(
        icon: Icon(Icons.lock),
        labelText: "Password",
      ),
      controller: _passwordController,
      autovalidateMode: AutovalidateMode.onUserInteraction,
      onEditingComplete: () => _focusNode.nextFocus(),
      onSaved: (String val) => _formData["password"] = val,
      validator: _validatePassword,
    );
  }

  String _validatePassword(String password) {
    if (password.isEmpty) return 'Required';
    if (password.length > 32) return '32 character limit';
    if (password.length < 8) return 'At least 8 characters';
    return null;
  }

  Widget get _buildPasswordConfirmationField {
    return TextFormField(
      obscureText: true,
      decoration: const InputDecoration(
        labelText: "Confirm password",
      ),
      autovalidateMode: AutovalidateMode.onUserInteraction,
      onEditingComplete: _submitRegistration,
      validator: _validatePasswordConfirmation,
    );
  }

  String _validatePasswordConfirmation(String confirmedPassword) {
    if (confirmedPassword.isEmpty) return 'Required';
    if (_passwordController.text != confirmedPassword) return 'Must match';
    return null;
  }

  void _submitRegistration() {
    if (_formKey.currentState.validate()) {
      _formKey.currentState.save();

      setState(() => _loading = true);
      registerUser(http.Client(), _formData["name"], _formData["email"],
              _formData["password"])
          .then((int id) => getUser(http.Client(), id))
          .then((User user) => _handleAuthenticationSuccess(user))
          .catchError((e, s) => _handleConnectionError(e, s),
              test: (e) => e is SocketException)
          .catchError((error, stackTrace) => _handleError(error, stackTrace))
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
        Duration(milliseconds: 1500),
        () => Navigator.pushNamedAndRemoveUntil(
            context, "/timeline", (r) => false));
  }

  void _handleConnectionError(dynamic error, StackTrace stackTrace) {
    print("Error: $error");
    print("StackTrace: $stackTrace");

    Scaffold.of(context).removeCurrentSnackBar();
    Scaffold.of(context).showSnackBar(SnackBar(
      content: RichText(
          text: TextSpan(children: [
        TextSpan(
            text: 'Connection failed',
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
