import 'dart:math';

import 'package:flutter/material.dart';
import 'package:moodlysis_app/models/event.dart';
import 'package:english_words/english_words.dart';

List<Event> generateEvents(int count) {
  List<Event> events = [];
  Iterable<WordPair> titles = generateWordPairs().take(count);
  DateTimeRange validRange = DateTimeRange(start: DateTime(2021, 1, 1, 12), end: DateTime.now());
  Random r = Random();

  for (WordPair title in titles) {
    Duration startOffset = validRange.duration*r.nextDouble();
    DateTime start = validRange.start.add(startOffset);
    DateTime end = start.add((validRange.duration - startOffset)*r.nextDouble());
    events.add(Event(title.asPascalCase, "A description", start, end));
  }

  return events;
}