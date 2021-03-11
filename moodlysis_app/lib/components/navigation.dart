import 'package:flutter/material.dart';

const List<String> _routes = ["/timeline", "/add-event", "/account"];

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