import 'dart:convert';
import 'package:http/http.dart' as http;

import 'package:moodlysis_app/models/event.dart';
import 'package:moodlysis_app/models/user.dart';
import 'package:moodlysis_app/constants.dart';
import 'package:moodlysis_app/services/exceptions.dart';

Future<List<Event>> getUserEvents(http.Client client, User user) async {
  final response = await client.get('$backendURI/register-event?attendeeID=${user.id}');

  if (response.statusCode == 404) return List<Event>();
  final List<dynamic> eventIDs = json.decode(response.body)['eventIDs'];

  return Future.wait(eventIDs.map((eventID) => getEvent(client, eventID)));
}

Future<int> registerForEvent(http.Client client, String inviteCode, User user) async {
  final Map<String, dynamic> body = {"invite-code": inviteCode, "attendeeID": user.id,};
  final response = await client.post('$backendURI/register-event', body: json.encode(body));

  if (response.statusCode == 404) throw ResultNotFoundException();

  return json.decode(response.body)['eventID'];
}

Future<Event> getEvent(http.Client client, int eventID) async {
  final response = await client.get('$backendURI/events/$eventID');

  if (response.statusCode == 404) throw ResultNotFoundException();

  return Event.fromJson(json.decode(response.body));
}