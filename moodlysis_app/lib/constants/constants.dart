const String backendURI = 'http://192.168.1.171:8001/v0';

const String splashScreenRoute = '/';
const String signInScreenRoute = '/signin';
const String signUpScreenRoute = '/signup';
const String timelineScreenRoute = '/timeline';
const String eventScreenRoute = '/timeline/event';
const String registerEventScreenRoute = '/register_event';
const String accountScreenRoute = '/account';

enum Sentiment {
  veryDissatisfied,
  dissatisfied,
  neutral,
  satisfied,
  verySatisfied
}

final Map<Sentiment, double> sentimentValues =  {
  Sentiment.veryDissatisfied: -1,
  Sentiment.dissatisfied: -0.5,
  Sentiment.neutral: 0,
  Sentiment.satisfied: 0.5,
  Sentiment.verySatisfied: 1,
};