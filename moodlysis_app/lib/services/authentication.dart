import 'dart:convert';
import 'package:http/http.dart' as http;

import 'package:moodlysis_app/models/user.dart';
import 'file:///C:/Users/valsp/source/repos/cs261-cw/moodlysis_app/lib/constants/constants.dart';
import 'package:moodlysis_app/services/exceptions.dart';

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

  if (response.statusCode == 400) throw RegistrationException();
  
  return json.decode(response.body)['attendeeID'];
}

Future<User> getUser(http.Client client, int userID) async {
  final response = await client.get('$backendURI/attendees/$userID');

  if (response.statusCode == 404) throw ResultNotFoundException();
  final Map<String, dynamic> data = json.decode(response.body)['data'];

  return User(userID, data['email'], data['account-name']);
}