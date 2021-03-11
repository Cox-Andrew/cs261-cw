import 'package:moodlysis_app/models/user.dart';

Map<String, String> _testCredentials = {"a": "a"};
Map<String, User> _testUsers = {"a": User(0, "A name")};
int _lastId = 0;

Future<User> authenticateUser(String email, String password) async {
  //TODO: API login request
  await Future.delayed(Duration(milliseconds: 500));
  if (_testCredentials.containsKey(email) &&
      _testCredentials[email] == password) {
    return _testUsers[email];
  }
  return null;
}

Future<User> registerUser(String name, String email, String password) async {
  //TODO: API registration request
  _testCredentials[email] = password;
  _testUsers[email] = User(++_lastId, name);
  return authenticateUser(email, password);
}