import 'dart:convert';
import 'package:http/http.dart' as http;

import 'package:moodlysis_app/models/user.dart';
import 'package:moodlysis_app/constants.dart';

class AuthenticationException implements Exception {}

Future<int> authenticateUser(http.Client client, String email, String password) async {
  final Map<String, String> body = {"email": email, "pass": password};
  //TODO: change to final endpoint
  final response = await client.post('$backendURI/attendee-temp-sign-in', body: json.encode(body));

  if (response.statusCode == 404) throw AuthenticationException();

  return json.decode(response.body)["attendeeID"];
}

//TODO endpoint not fully implemented can register with already in use email
Future<int> registerUser(http.Client client, String name, String email, String password) async {
  final Map<String, String> body = {"account-name": name, "email": email, "pass": password};
  final response = await client.post('$backendURI/attendees', body: json.encode(body));
  
  return json.decode(response.body)['attendeeID'];
}

Future<User> getUser(http.Client client, int id) async {
  final response = await client.get('$backendURI/attendees/$id');
  final Map<String, dynamic> data = json.decode(response.body)['data'];
  
  return User(id, data['email'], data['account-name']);
}