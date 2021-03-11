import 'package:moodlysis_app/models/event.dart';
import 'package:moodlysis_app/models/user.dart';
import 'package:moodlysis_app/test_data.dart';

Future<List<Event>> getUserEvents(User user) async {
  //TODO: API events request
  await Future.delayed(Duration(milliseconds: 500));
  List events = generateEvents(10);
  return events;
}