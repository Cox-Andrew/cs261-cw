import 'dart:math';

Map<String, String> _testCredentials = {"a": "a"};

Future<int> authenticateUser(String email, String password) async {
  //TODO: API login request
  await Future.delayed(Duration(milliseconds: 500));
  if (_testCredentials.containsKey(email) &&
      _testCredentials[email] == password) {
    return Random().nextInt(10);
  }
  return null;
}

Future<int> registerUser(String name, String email, String password) async {
  //TODO: API registration request
  _testCredentials[email] = password;
  return authenticateUser(email, password);
}