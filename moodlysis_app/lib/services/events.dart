import 'dart:convert';
import 'package:http/http.dart' as http;

import 'package:moodlysis_app/models/event.dart';
import 'package:moodlysis_app/models/user.dart';
import 'package:moodlysis_app/test_data.dart';
import 'package:moodlysis_app/constants.dart';

class InvalidCodeException implements Exception {}

Future<List<Event>> getUserEvents(User user) async {
  //TODO: API events request
  await Future.delayed(Duration(milliseconds: 500));
  List events = generateEvents(10);
  return events;
}

Future<int> registerForEvent(http.Client client, String inviteCode, int attendeeID) async {
  final Map<String, dynamic> body = {"invite-code": inviteCode, "attendeeID": attendeeID,};
  final response = await client.post('$backendURI/register-event', body: json.encode(body));

  if (response.statusCode == 404) throw InvalidCodeException();

  return json.decode(response.body)['eventID'];
}

Future<Event> getEvent(int eventID) async {
  //TODO: request event by ID
  return Event("Placeholder", "A placeholder event before API integration", DateTime.now(), DateTime.now());
}