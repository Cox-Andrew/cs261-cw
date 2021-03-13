import 'package:flutter/material.dart';
import 'package:intl/intl.dart';

extension HumanReadable on DateTimeRange {
  String get humanReadable {
    String start = DateFormat('E, MMM d HH:mm').format(this.start);
    if (this.start.year != DateTime.now().year) {
      start += ' ' + this.start.year.toString();
    }

    String end = DateFormat('E, MMM d HH:mm').format(this.end);
    if (this.end.year != DateTime.now().year) {
      end += ' ' + this.end.year.toString();
    }

    return '$start - $end';
  }
}

