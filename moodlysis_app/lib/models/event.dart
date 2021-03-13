import 'package:flutter/material.dart';

//TODO: implement Form and Series classes
class Event {
  int eventID, seriesID;

  String title, description;
  DateTimeRange schedule;

  //TODO: are eventFormIDs actually requried?
  List<int> formIDs, eventFormIDs;

  Event(this.eventID, this.seriesID, this.title, this.description, DateTime start, DateTime end, this.formIDs, this.eventFormIDs) {
    this.schedule = DateTimeRange(start: start, end: end);
  }

  factory Event.fromJson(Map<String, dynamic> json) {
    return Event(
      json['eventID'] as int,
      json['seriesID'] as int,
      json['data']['title'] as String,
      json['data']['description'] as String,
      DateTime.parse(json['data']['time-start']),
      DateTime.parse(json['data']['time-end']),
      List<int>.from(json['formIDs']),
      List<int>.from(json['eventFormIDs']),
    );
  }
}