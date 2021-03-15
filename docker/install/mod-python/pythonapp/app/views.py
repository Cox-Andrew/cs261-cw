from app import app
import flask
import json
from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer
analyser = SentimentIntensityAnalyzer()

def sentiment_analyzer_scores(sentence):
    score = analyser.polarity_scores(sentence)
    return ("{}".format(json.dumps(score)))

@app.route('/v0/analyse', methods=["GET"])
def analyse():
    sentence = flask.request.args.get('text')
    return sentiment_analyzer_scores(sentence)

@app.route('/')
def home():
    return 'test'


@app.route('/test', methods=["GET"])
def starting_url():
    json_data = flask.request.json
    a_value = json_data["a_key"]
    return 'JSON value sent: ' + a_value
