import 'package:flutter/material.dart';

import 'package:moodlysis_app/components/navigation.dart';

class AccountScreen extends StatelessWidget {
  static const route = "/account";

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Account'),),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Material(
            color: Theme.of(context).primaryColor,
            child: Text("Manage Account", style: Theme.of(context).textTheme.headline2,),
          ),
        ],
      ),
      bottomNavigationBar: MoodlysisBottomNavigationBar(AccountScreen.route),
    );
  }
}