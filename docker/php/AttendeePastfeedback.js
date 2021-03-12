function getComprehensive() {
  const currentDiv = document.getElementById("form");
  eventID = getCookie("eventID");
  attendeeID = getCookie("attendeeID");
  eventFormID = getCookie("eventFormID");
  formID = getCookie("formID");
  $.ajax({
    type: "GET",
    url: endpointToRealAddress("/feedback?eventID=" + eventID + "&attendeeID=" + attendeeID),
    dataType: "json",
    async: false,
    success: function(result, status, xhr){
      list = result["list"];
      Array.from(list).forEach(element => {
        eventFormIDCheck = element["eventFormID"];
        if (element["eventFormID"] == eventFormID) {
          answers = element["answers"];
          Array.from(answers).forEach(answer => {
            questionID = answer["questionID"];
            answerID = answer["questionID"];
            mood = answer["mood-value"];
            isEdited = answer["is-edited"];
  					dateUpdated = new Date(answer["time-updated"]);
            response = answer.data["response"];
            //setCookie("questionID" + i, questionID , 1);
            $.ajax({
              type: "GET",
              url: endpointToRealAddress("/questions/" + questionID),
              dataType: "json",
              async: false,
              success: function(result, status, xhr){
                type = result.data["type"];
                text = result.data["text"];
                options = result.data["options"];
                var br = document.createElement("br");
                var questionDiv = document.createElement("div");
                questionDiv.className = "question1";
                var questionLabel = document.createElement("Label");
                questionLabel.setAttribute("id", "formLabel");
                setInnerHTMLSanitized(questionLabel, text );
                questionDiv.appendChild(questionLabel);
                if (type == "long") {
                  var questionEntry = document.createElement("div");
                  questionEntry.className = "answer1";
                  setInnerHTMLSanitized(questionEntry, response);
                  currentDiv.appendChild(questionDiv);
                  currentDiv.appendChild(br);
                  currentDiv.appendChild(questionEntry);
                  var br = document.createElement("br");
                  currentDiv.appendChild(br);
                  var sentiment = document.createElement("div");
                  sentiment.className = "answer1";
                  setInnerHTMLSanitized(sentiment, "Sentiment: " + mood);
                  currentDiv.appendChild(sentiment);
                  var br = document.createElement("br");
                  currentDiv.appendChild(br);
                }
                else if (type == "short") {
                  //TODO only enter two words + make distinguishable
                  var questionEntry = document.createElement("div");
                  questionEntry.className = "answer1";
                  setInnerHTMLSanitized(questionEntry, response);
                  currentDiv.appendChild(questionDiv);
                  currentDiv.appendChild(br);
                  currentDiv.appendChild(questionEntry);
                  var br = document.createElement("br");
                  currentDiv.appendChild(br);
                  var sentiment = document.createElement("div");
                  sentiment.className = "answer1";
                  setInnerHTMLSanitized(sentiment, "Sentiment: " + mood);
                  currentDiv.appendChild(sentiment);
                  var br = document.createElement("br");
                  currentDiv.appendChild(br);
                }
                else if (type == "options") {
                  //TODO
                }
                else if (type == "rating") {
                  //TODO
                }
              }
            });
          });
        }
      });
      var br = document.createElement("br");
      currentDiv.appendChild(br);
    }
  });
}
getComprehensive();
