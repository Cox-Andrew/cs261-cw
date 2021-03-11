import 'package:moodlysis_app/models/event.dart';
import 'package:moodlysis_app/models/user.dart';
import 'package:moodlysis_app/test_data.dart';

final Map<String, int> _testCodeIDs = {
  'ABCDEFGH': 1,
  '12345678': 2,
};

final Map<int, Event> _testIDEvents = {
  1: Event("Alphabet class", "learn ur abcs", DateTime.now().subtract(Duration(minutes: 5)), DateTime.now().add(Duration(minutes: 55))),
  2: Event("Maths", "algebra hours", DateTime.now().add(Duration(days: 5, hours: 2)), DateTime.now().add(Duration(days: 5, hours: 3))),
};

Future<List<Event>> getUserEvents(User user) async {
  //TODO: API events request
  await Future.delayed(Duration(milliseconds: 500));
  List events = generateEvents(10);
  return events;
}

Future<int> registerForEvent(String inviteCode) async {
  //TODO: API event registration
  await Future.delayed(Duration(milliseconds: 500));
  if (_testCodeIDs.containsKey(inviteCode)) return _testCodeIDs[inviteCode];

  return null;
}

Future<Event> getEvent(int eventID) async {
  //TODO: request event by ID
  await Future.delayed(Duration(milliseconds: 500));
  if (_testIDEvents.containsKey(eventID)) return _testIDEvents[eventID];

  return null;
}