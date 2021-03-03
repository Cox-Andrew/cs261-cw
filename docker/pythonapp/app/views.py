from app import app
import flask
from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer
analyser = SentimentIntensityAnalyzer()

def sentiment_analyzer_scores(sentence):
    score = analyser.polarity_scores(sentence)
    return ("{:-<40} {}".format(sentence, str(score)))

@app.route('/v0/analyse', methods=["GET"])
def analyse():
    json_data = flask.request.json
    sentence = json_data["sentence"]
    return sentiment_analyzer_scores(sentence)

@app.route('/')
def home():
    return 'test'


@app.route("/test", methods=["GET"])

def starting_url():

    json_data = flask.request.json

    a_value = json_data["a_key"]

    return "JSON value sent: " + a_value
