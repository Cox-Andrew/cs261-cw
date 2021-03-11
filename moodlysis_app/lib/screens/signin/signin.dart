import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'package:moodlysis_app/services/authentication.dart';

class SignInScreen extends StatelessWidget {
  static const route = "/signin";

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
      authenticateUser(_formData["email"], _formData["password"])
          .then((authToken) {
        if (authToken == null) {
          Scaffold.of(context).removeCurrentSnackBar();
          Scaffold.of(context).showSnackBar(SnackBar(
            content: Text("Invalid credentials"),
            backgroundColor: Theme.of(context).errorColor,
          ));
          return;
        }

        Scaffold.of(context).removeCurrentSnackBar();
        Scaffold.of(context).showSnackBar(SnackBar(
          content: Text(
              'Email: ${_formData["email"]}, Passwd: ${_formData["password"]}, Token: $authToken'),
          backgroundColor: Colors.green,
        ));

        _focusNode.unfocus();
        Navigator.pushNamedAndRemoveUntil(context, "/timeline", (r) => false);
      }).whenComplete(() => setState(() => _loading = false));
    }
  }
}
