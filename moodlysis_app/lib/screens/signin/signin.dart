import 'package:flutter/material.dart';

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

  void _signInAction() {
    if (_formKey.currentState.validate()) {
      Scaffold.of(context)
          .showSnackBar(SnackBar(content: Text('Processing Data')));
      Future.delayed(Duration(milliseconds: 500), () {
        Navigator.pushNamedAndRemoveUntil(context, "/timeline", (r) => false);
      });
    }
  }

  @override
  Widget build(BuildContext context) {
    final focusNode = FocusScope.of(context);

    return Form(
        key: _formKey,
        child: Column(mainAxisSize: MainAxisSize.min, children: <Widget>[
          TextFormField(
            decoration: const InputDecoration(
                icon: Icon(Icons.email), labelText: "Email"),
            keyboardType: TextInputType.emailAddress,
            autovalidateMode: AutovalidateMode.onUserInteraction,
            autofocus: true,
            onEditingComplete: () => focusNode.nextFocus(),
            validator: (email) {
              if (email.isEmpty) {
                return 'Required';
              }

              return null;
            },
          ),
          TextFormField(
            obscureText: true,
            decoration: const InputDecoration(
              icon: Icon(Icons.lock),
              labelText: "Password",
            ),
            autovalidateMode: AutovalidateMode.onUserInteraction,
            onEditingComplete: _signInAction,
            validator: (value) {
              if (value.isEmpty) {
                return 'Required';
              }
              return null;
            },
          ),
          ElevatedButton(
            onPressed: _signInAction,
            child: Text('Sign In'),
          ),
        ]));
  }
}
