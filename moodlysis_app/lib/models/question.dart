import 'package:flutter/foundation.dart';

//TODO: multi & rating are currently unimplemented
enum QuestionType { short, long, multi, rating }

class Question {
  final int id;
  final String text;
  final QuestionType type;

  Question({this.id, this.text, this.type});

  factory Question.fromJson(Map<String, dynamic> json) {
    return Question(
      id: json['questionID'] as int,
      text: json['data']['text'] as String,
      type: QuestionType.values
          .firstWhere((e) => describeEnum(e) == json['data']['type']),
    );
  }
}
