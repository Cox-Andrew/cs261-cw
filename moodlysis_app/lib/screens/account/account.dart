import 'package:flutter/material.dart';

import 'package:moodlysis_app/components/navigation.dart';
import 'package:moodlysis_app/globals.dart' as globals;

class AccountScreen extends StatelessWidget {
  static const route = "/account";

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Account'),),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: [
            Card(
              child: Padding(
                padding: const EdgeInsets.all(8.0),
                child: Column(
                  crossAxisAlignment: CrossAxisAlignment.start,
                  children: [
                    Text("Account details", style: TextStyle(fontSize: 40, fontWeight: FontWeight.bold),),
                    RichText(text: TextSpan(children: [
                      WidgetSpan(child: Icon(Icons.tag)),
                      TextSpan(text: globals.currentUser.id.toString(), style: TextStyle(color: Colors.black))
                    ])),
                    RichText(text: TextSpan(children: [
                      WidgetSpan(child: Icon(Icons.person)),
                      TextSpan(text: globals.currentUser.name, style: TextStyle(color: Colors.black))
                    ])),
                    RichText(text: TextSpan(children: [
                      WidgetSpan(child: Icon(Icons.email)),
                      TextSpan(text: globals.currentUser.email, style: TextStyle(color: Colors.black))
                    ])),
                  ],
                ),
              ),
            ),
          ],
        ),
      ),
      bottomNavigationBar: MoodlysisBottomNavigationBar(AccountScreen.route),
    );
  }
}