import 'package:flutter/material.dart';

class SignUpScreen extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Register"),
      ),
      body: Column(
        children: [
          Padding(
            padding:
                const EdgeInsets.fromLTRB(20, 80, 20, 20),
            child: Text(
              "Create an account.",
              style: Theme.of(context).textTheme.headline3,
            ),
          ),
          Expanded(
            child: Padding(
              padding: const EdgeInsets.all(20),
              child: Align(
                  alignment: FractionalOffset(0, 0.3), child: SignUpForm()),
            ),
          ),
        ],
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

  // From HTML5 standard
  static final RegExp _emailRegex = RegExp(
      r"^[a-zA-Z0-9.!#$%&'*+\/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$");

  static bool _validEmail(String email) => _emailRegex.hasMatch(email);

  @override
  Widget build(BuildContext context) {
    final focusNode = FocusScope.of(context);

    return Form(
        key: _formKey,
        child: Column(mainAxisSize: MainAxisSize.min, children: <Widget>[
          Row(
            children: [
              Expanded(
                child: TextFormField(
                  decoration: const InputDecoration(
                    icon: Icon(Icons.person),
                    labelText: "First Name",
                  ),
                  keyboardType: TextInputType.name,
                  autofocus: true,
                  autovalidateMode: AutovalidateMode.onUserInteraction,
                  onEditingComplete: () {
                    focusNode.nextFocus();
                  },
                  validator: (value) {
                    if (value.isEmpty) {
                      return 'Required';
                    }
                    return null;
                  },
                ),
              ),
              Expanded(
                child: TextFormField(
                  decoration: const InputDecoration(
                    labelText: "Last Name",
                  ),
                  keyboardType: TextInputType.name,
                  autovalidateMode: AutovalidateMode.onUserInteraction,
                  onEditingComplete: () => focusNode.nextFocus(),
                  validator: (value) {
                    if (value.isEmpty) {
                      return 'Required';
                    }
                    return null;
                  },
                ),
              )
            ],
          ),
          TextFormField(
            decoration: const InputDecoration(
                icon: Icon(Icons.email), labelText: "Email"),
            keyboardType: TextInputType.emailAddress,
            autovalidateMode: AutovalidateMode.onUserInteraction,
            onEditingComplete: () => focusNode.nextFocus(),
            validator: (email) {
              if (email.isEmpty) {
                return 'Required';
              }

              if (!_validEmail(email)) {
                return 'Invalid email provided';
              }

              return null;
            },
          ),
          Row(
            children: [
              Expanded(
                  child: TextFormField(
                obscureText: true,
                decoration: const InputDecoration(
                  icon: Icon(Icons.lock),
                  labelText: "Password",
                ),
                autovalidateMode: AutovalidateMode.onUserInteraction,
                onEditingComplete: () => focusNode.nextFocus(),
                validator: (value) {
                  if (value.isEmpty) {
                    return 'Required';
                  }
                  return null;
                },
              )),
              Expanded(
                  child: TextFormField(
                obscureText: true,
                decoration: const InputDecoration(
                  labelText: "Confirm password",
                ),
                autovalidateMode: AutovalidateMode.onUserInteraction,
                onEditingComplete: () => focusNode.nextFocus(),
                validator: (value) {
                  if (value.isEmpty) {
                    return 'Required';
                  }
                  return null;
                },
              )),
            ],
          ),
          ElevatedButton(
            onPressed: () {
              if (_formKey.currentState.validate()) {
                Scaffold.of(context)
                    .showSnackBar(SnackBar(content: Text('Processing Data')));
                Future.delayed(Duration(milliseconds: 500), () {
                  Navigator.pushNamedAndRemoveUntil(
                      context, "/timeline", (r) => false);
                });
              }
            },
            child: Text('Register'),
          ),
        ]));
  }
}
