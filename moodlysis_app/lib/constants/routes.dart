import 'package:flutter/widgets.dart';

import 'package:moodlysis_app/constants/constants.dart';
import 'package:moodlysis_app/screens/screens.dart';

final Map<String, WidgetBuilder> routes = <String, WidgetBuilder>{
  splashScreenRoute: (context) => SplashScreen(),
  signInScreenRoute: (context) => SignInScreen(),
  signUpScreenRoute: (context) => SignUpScreen(),
  timelineScreenRoute: (context) => TimelineScreen(),
  eventScreenRoute: (context) => EventScreen(),
  registerEventScreenRoute: (context) => RegisterEventScreen(),
  accountScreenRoute: (context) => AccountScreen(),
};
