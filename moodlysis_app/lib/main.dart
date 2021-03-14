import 'package:flutter/material.dart';

import 'package:moodlysis_app/constants/routes.dart';
import 'package:moodlysis_app/constants/theme.dart';
import 'package:moodlysis_app/constants/constants.dart';

void main() {
  runApp(MoodlysisApp());
}

class MoodlysisApp extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      onTap: () {
        WidgetsBinding.instance.focusManager.primaryFocus?.unfocus();
      },
      child: MaterialApp(
        title: 'Moodlysis',
        theme: appTheme,
        initialRoute: splashScreenRoute,
        routes: routes,
        builder: (context, child) => SafeArea(child: child),
      ),
    );
  }
}
