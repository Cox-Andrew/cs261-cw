import 'package:flutter/material.dart';
import 'dart:math';
import 'package:lipsum/lipsum.dart' as lipsum;

import 'package:moodlysis_app/models/event.dart';

List<Event> generateEvents(int count) {
  List<Event> events = [];
  DateTimeRange validRange = DateTimeRange(start: DateTime(2021, 1, 1, 12), end: DateTime.now());
  Random r = Random();

  for (int i=0; i<count; i++) {
    Duration startOffset = validRange.duration*r.nextDouble();
    DateTime start = validRange.start.add(startOffset);
    DateTime end = start.add((validRange.duration - startOffset)*r.nextDouble());
    events.add(Event(lipsum.createWord(numWords: 5), lipsum.createWord(numWords: 15), start, end));
  }

  return events;
}