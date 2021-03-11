import 'package:flutter/material.dart';

import 'package:moodlysis_app/components/navigation.dart';

class AddEventScreen extends StatelessWidget {
  static const route = "/add-event";

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: Text('Event Registration'),),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.center,
        children: [
          Material(
            color: Theme.of(context).primaryColor,
            child: Text("Enter Invite Code", style: Theme.of(context).textTheme.headline2,),
          ),
        ],
      ),
      bottomNavigationBar: MoodlysisBottomNavigationBar(AddEventScreen.route),
    );
  }
}