import 'package:flutter/widgets.dart';

import 'package:moodlysis_app/screens/screens.dart';

final Map<String, WidgetBuilder> routes = <String, WidgetBuilder>{
  SplashScreen.route: (context) => SplashScreen(),
  SignInScreen.route: (context) => SignInScreen(),
  SignUpScreen.route: (context) => SignUpScreen(),
  TimelineScreen.route: (context) => TimelineScreen(),
  EventScreen.route: (context) => EventScreen(),
  RegisterEventScreen.route: (context) => RegisterEventScreen(),
  AccountScreen.route: (context) => AccountScreen(),
};
