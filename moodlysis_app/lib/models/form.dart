import 'package:moodlysis_app/models/question.dart';

class Form {
  final int formID;
  final String title, description;
  final List<int> questionIDs;
  int eventFormID;
  List<Question> questions;

  Form({
    this.formID,
    this.title,
    this.description,
    this.questionIDs,
  });

  factory Form.fromJson(Map<String, dynamic> json) {
    return Form(
      formID: json['formID'] as int,
      title: json['data']['title'] as String,
      description: json['data']['description'] as String,
      questionIDs: List<int>.from(json['questionIDs']),
    );
  }
}
