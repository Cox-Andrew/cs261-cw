import 'dart:math';
import 'package:flutter/material.dart';

const Map<String, String> _testCredentials = {"a":"a"};

class SignInScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
        appBar: AppBar(
          title: Text("Sign In"),
        ),
        body: Column(
          children: [
            Padding(
              padding: const EdgeInsets.fromLTRB(20, 80, 20, 20),
              child: Text(
                "Welcome back.",
                style: Theme.of(context).textTheme.headline3,
              ),
            ),
            Expanded(
              child: Padding(
                padding: const EdgeInsets.all(20),
                child: Align(
                    alignment: FractionalOffset(0, 0.3), child: SignInForm()),
              ),
            ),
          ],
        ));
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
  Map<String, String> _formData = Map<String, String>();
  FocusNode _focusNode = FocusNode();
  final FocusNode _emailFocus = FocusNode();

  @override
  Widget build(BuildContext context) {
    _focusNode = FocusScope.of(context);

    return Form(
        key: _formKey,
        child: Column(mainAxisSize: MainAxisSize.min, children: <Widget>[
          _buildEmailField,
          _buildPasswordField,
          Align(
            alignment: Alignment.centerRight,
            child: ElevatedButton(
              onPressed: _signIn,
              child: Text('Sign In'),
            ),
          ),
        ])
    );
  }

  Widget get _buildEmailField {
    return TextFormField(
      decoration: const InputDecoration(
          icon: Icon(Icons.email), labelText: "Email"),
      keyboardType: TextInputType.emailAddress,
      autofocus: true,
      focusNode: _emailFocus,
      onEditingComplete: () => _focusNode.nextFocus(),
      onSaved: (String val) => _formData["email"] = val,
      validator: (email) {
        if (email.isEmpty) {
          return 'Required';
        }
        return null;
      },
    );
  }

  Widget get _buildPasswordField {
    return TextFormField(
      obscureText: true,
      decoration: const InputDecoration(
        icon: Icon(Icons.lock),
        labelText: "Password",
      ),
      onEditingComplete: _signIn,
      onSaved: (String val) => _formData["password"] = val,
      validator: (value) {
        if (value.isEmpty) {
          return 'Required';
        }
        return null;
      },
    );
  }

  void _signIn() {
    if (_formKey.currentState.validate()) {
      _formKey.currentState.save();

      int authToken = _authenticateUser(_formData["email"], _formData["password"]);
      if (authToken == null) {
        Scaffold.of(context)
            .showSnackBar(SnackBar(content: Text("Invalid credentials"), backgroundColor: Theme.of(context).errorColor,));
        _formKey.currentState.reset();
        _formData.clear();
        _emailFocus.requestFocus();
        return;
      }

      Scaffold.of(context)
          .showSnackBar(SnackBar(content: Text('Email: ${_formData["email"]}, Passwd: ${_formData["password"]}, Token: $authToken')));

      Future.delayed(Duration(milliseconds: 500), () {
        Navigator.pushNamedAndRemoveUntil(context, "/timeline", (r) => false);
      });
    }
  }

  int _authenticateUser(String email, String password) {
    //TODO: implement asynchronous login
    if (_testCredentials.containsKey(email) && _testCredentials[email] == password) {
      return Random().nextInt(10);
    }
    return null;
  }
}
