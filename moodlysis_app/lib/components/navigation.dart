import 'package:flutter/material.dart';
import 'file:///C:/Users/valsp/source/repos/cs261-cw/moodlysis_app/lib/constants/constants.dart';

const List<String> _routes = [timelineScreenRoute, registerEventScreenRoute, accountScreenRoute];

class MoodlysisBottomNavigationBar extends StatelessWidget {
  final int _currentIndex;

  MoodlysisBottomNavigationBar(String _currentRoute):
    this._currentIndex = _routes.indexOf(_currentRoute);

  @override
  Widget build(BuildContext context) {
    return BottomNavigationBar(
      //TODO: how to handle invalid screen passed?
      currentIndex: _currentIndex,
      onTap: (index) => index == _currentIndex ? null : Navigator.pushReplacementNamed(context, _routes[index]),
      items: [
        BottomNavigationBarItem(icon: Icon(Icons.timeline), label: "Timeline"),
        BottomNavigationBarItem(icon: Icon(Icons.add), label: "Add event"),
        BottomNavigationBarItem(icon: Icon(Icons.person), label: "Account"),
      ],
    );
  }
}