import 'package:moodlysis_app/models/question.dart';

class EventForm {
  final int formID;
  final String title, description;
  final List<int> questionIDs;
  int eventFormID;
  List<Question> questions;

  EventForm({
    this.formID,
    this.title,
    this.description,
    this.questionIDs,
  });

  factory EventForm.fromJson(Map<String, dynamic> json) {
    return EventForm(
      formID: json['formID'] as int,
      title: json['data']['title'] as String,
      description: json['data']['description'] as String,
      questionIDs: List<int>.from(json['questionIDs']),
    );
  }
}
