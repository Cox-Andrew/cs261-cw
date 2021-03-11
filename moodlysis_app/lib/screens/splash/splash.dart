import 'package:flutter/material.dart';

class SplashScreen extends StatelessWidget {
  static const route = "/";

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("Welcome!"),
      ),
      body: Padding(
        padding: const EdgeInsets.all(20),
        child: Column(
          children: [
            Expanded(
              child: Align(
                alignment: Alignment.bottomCenter,
                child: Row(
                  crossAxisAlignment: CrossAxisAlignment.baseline,
                  children: [
                    Text("Mood",
                        style: Theme.of(context)
                            .textTheme
                            .headline1
                            .copyWith(color: Theme.of(context).accentColor)),
                    Text("lysis", style: Theme.of(context).textTheme.headline1),
                    Text(
                      ".",
                      style: Theme.of(context).textTheme.headline1.copyWith(
                            color: Theme.of(context).accentColor,
                            fontSize:
                                Theme.of(context).textTheme.headline1.fontSize *
                                    1.5,
                            height: 0.3,
                          ),
                    ),
                  ],
                ),
              ),
            ),
            Expanded(
              child: Align(
                alignment: Alignment.bottomCenter,
                child: ElevatedButton(
                  onPressed: () {
                    Navigator.of(context).pushNamed("/signup");
                  },
                  style: ElevatedButton.styleFrom(
                      padding: EdgeInsets.all(10),
                      shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.all(Radius.circular(25))),
                    minimumSize: Size(300, 50),
                  ),
                  child: Text("Register",
                      style: TextStyle(
                        fontSize: 30,
                      )),
                ),
              ),
            ),
            Expanded(
              flex: 1,
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                children: [
                  Text("Already have an account?", style: Theme.of(context).textTheme.subtitle1,),
                  OutlineButton(
                    onPressed: () {
                      Navigator.of(context).pushNamed("/signin");
                    },
                    // padding: EdgeInsets.all(10),
                    shape: RoundedRectangleBorder(
                        borderRadius: BorderRadius.all(Radius.circular(25))),
                    child: Text("Sign in", style: TextStyle(
                        fontSize: 25,
                        color: Colors.black54
                    )),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }
}
