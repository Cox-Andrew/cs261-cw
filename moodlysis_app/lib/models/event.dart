import 'package:flutter/material.dart';

class Event {
  String title, description;
  DateTimeRange schedule;

  Event(this.title, this.description, DateTime start, DateTime end) {
    this.schedule = DateTimeRange(start: start, end: end);
  }
}