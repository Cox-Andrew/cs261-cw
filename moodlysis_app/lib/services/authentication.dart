import 'dart:convert';
import 'package:http/http.dart' as http;

import 'package:moodlysis_app/models/user.dart';

const backendURI = 'http://192.168.1.171:8001/v0';
class AuthenticationException implements Exception {}

Future<int> authenticateUser(http.Client client, String email, String password) async {
  final Map<String, String> body = {"email": email, "pass": password};
  final response = await client.post('$backendURI/attendee-temp-sign-in', body: json.encode(body));

  //TODO: ask whether to use MoodlysisNotFound or account not found
  if (response.body.contains('account not found')) throw AuthenticationException();

  final int id = json.decode(response.body)["attendeeID"];

  return id;
}

//TODO endpoint not fully implemented can register with already in use email
Future<int> registerUser(http.Client client, String name, String email, String password) async {
  final Map<String, String> body = {"name": name, "email": email, "pass": password};
  final response = await client.post('$backendURI/attendees', body: json.encode(body));
  
  return json.decode(response.body).cast['data']['attendeeID'];
}

Future<User> getUser(http.Client client, int id) async {
  final response = await client.get('$backendURI/attendees/$id');
  final Map<String, dynamic> data = json.decode(response.body)['data'];
  
  return User(id, data['email'], data['account-name']);
}