import 'package:flutter/material.dart';

ThemeData appTheme() {
  return ThemeData(
    primarySwatch: Colors.indigo,
    textTheme: TextTheme(
      headline4: TextStyle(
        color: Colors.black,
        fontWeight: FontWeight.bold,
      ),
      headline1: TextStyle(
        fontSize: 80,
        fontWeight: FontWeight.bold,
        color: Colors.black
      )
    ),
  );
}