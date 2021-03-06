import 'package:flutter/widgets.dart';

import 'package:moodlysis_app/screens/screens.dart';

final Map<String, WidgetBuilder> routes = <String, WidgetBuilder>{
  "/": (BuildContext context) => SplashScreen(),
  "/signin": (context) => SignInScreen(),
  "/signup": (context) => SignUpScreen(),
  "/timeline": (context) => Timeline(),
  "/event": (context) => EventScreen(),
};
