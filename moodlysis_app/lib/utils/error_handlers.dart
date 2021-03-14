import 'package:flutter/material.dart';

void handleConnectionError(BuildContext context, dynamic error, StackTrace stackTrace) {
  // print("Error: $error");
  // print("StackTrace: $stackTrace");

  Scaffold.of(context).showSnackBar(SnackBar(
    content: RichText(
        text: TextSpan(children: [
          TextSpan(text: 'Connection failed', style: TextStyle(fontWeight: FontWeight.bold)),
          TextSpan(text: ', please try again.'),
        ])),
    backgroundColor: Theme.of(context).errorColor,
  ));
}

void handleError(BuildContext context, dynamic error, StackTrace stackTrace) {
  print("Error: $error");
  print("StackTrace: $stackTrace");

  Scaffold.of(context).showSnackBar(SnackBar(
    content: RichText(
        text: TextSpan(children: [
          TextSpan(text: 'Something went wrong', style: TextStyle(fontWeight: FontWeight.bold)),
          TextSpan(text: ', please try again.'),
        ])),
    backgroundColor: Theme.of(context).errorColor,
  ));
}