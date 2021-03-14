import 'dart:convert';
import 'package:http/http.dart' as http;

import 'package:moodlysis_app/constants/constants.dart';
import 'package:moodlysis_app/models/event.dart';
import 'package:moodlysis_app/models/form.dart';
import 'package:moodlysis_app/models/question.dart';
import 'package:moodlysis_app/models/user.dart';

Future<void> sendMood(http.Client client, double moodValue, int eventID) async {
  final Map<String, dynamic> body = {
    "mood-value": moodValue,
    "eventID": eventID,
  };
  await client.post('$backendURI/moods', body: json.encode(body));

  //TODO: what errors can this throw
}

Future<int> sendFeedback(http.Client client, User user, int eventID,
    int eventFormID, int questionID, String answer, bool isAnonymous) async {
  final Map<String, dynamic> body = {
    'attendeeID': user.id,
    'eventID': eventID,
    'eventFormID': eventFormID,
    'questionID': questionID,
    'data': {
      'response': answer,
      'isAnonymous': isAnonymous,
    }
  };
  final response =
      await client.post('$backendURI/answers', body: json.encode(body));

  return json.decode(response.body)['answerID'];
}

Future<Question> getQuestion(http.Client client, int questionID) async {
  final response = await client.get('$backendURI/questions/$questionID');
  return Question.fromJson(json.decode(response.body));
}

Future<Form> getForm(http.Client client, int formID) async {
  final response = await client.get('$backendURI/forms/$formID');
  return Form.fromJson(json.decode(response.body));
}

Future<Form> getActiveForm(http.Client client, Event event) async {
  // Ignores first as guaranteed to be general feedback form
  for (int eventFormID in event.eventFormIDs.sublist(1)) {
    final response = await client.get('$backendURI/event-forms/$eventFormID');
    final Map<String, dynamic> parsed = json.decode(response.body);

    if (parsed['isActive'] as bool) {
      final Form form = await getForm(client, parsed['formID']);
      form.eventFormID = eventFormID;
      form.questions = await Future.wait(
          form.questionIDs.map((id) => getQuestion(client, id)));

      return form;
    }
  }

  return null;
}
