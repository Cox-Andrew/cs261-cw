var temp1 = document.getElementById("general");
var temp2 = document.getElementById("feedback");
function general() {
  if (temp1.style.display === "none" || temp1.style.display === '')
  {
    if(temp2.style.display !== "none" || temp2.style.display !== '')
    {
      temp2.style.display = "none";
    }
    temp1.style.display = "block";
  } else {
    temp1.style.display = "none";
  }
}
function feedback() {
  if (temp2.style.display === "none" || temp2.style.display === '')
  {
    if(temp1.style.display !== "none" || temp1.style.display !== '')
    {
      temp1.style.display = "none";
    }    
    temp2.style.display = "block"; 
  } else {
    temp2.style.display = "none";
  }
}
function showDiv() {
  document.getElementById("msg").style.display= " block";
}

//general tab
google.charts.load("current", {packages:["corechart"]});
google.charts.setOnLoadCallback(drawChart1);
function drawChart1() {
  // example values
  var Excellent = 10; //assign excellent to a mood value from backend
  var VGood = 5;
  var Good = 4;
  var NGood = 3;
  var Bad = 1;

  var data = google.visualization.arrayToDataTable([
    ['Mood', 'Mood value'],
    ['Excellent', Excellent],
    ['Very good', VGood],
    ['Good', Good],
    ['Not good', NGood],
    ['Bad', Bad]
  ]);

  var options = {
    title: 'Mood analysis',
    is3D: true,
  };

  var chart = new google.visualization.PieChart(document.getElementById('piechart_3d'));
  chart.draw(data, options);
}

//comprehensive tab
google.charts.load("current", {packages:["corechart"]});
google.charts.setOnLoadCallback(drawChart2);
function drawChart2() {
  //example values
  var q1 = 10; //assign q1 to a mood value from backend
  var q2 = 5;
  var q3 = 2;
  var data = google.visualization.arrayToDataTable([
    ['Question', 'Mood value'],
    ['Q1', q1],
    ['Q2', q2],
    ['Q3', q3]
  ]);

  var options = {
    title: 'Mood analysis',
    pieHole: 0.4,
  };

  var chart = new google.visualization.PieChart(document.getElementById('donutchart'));
  chart.draw(data, options);
}




function generalFeedbackSubmissionDisplayFactory(id) {
  

    // <div class="submission", id="generalFeedbackAnswer324">
  //   <div class="time-updated"></div>
  //   <div class="account-name"></div>
  //   <div class="response"></div>
  //   <div class="is-edited"></div>
  //   <div class="mood-value"></div>
  // </div>

  var timeUpdated = document.createElement("div");
  timeUpdated.className = "time-updated";
  var accountName = document.createElement("div");
  accountName.className = "account-name";
  var response = document.createElement("div");
  response.className = "response";
  var isEdited = document.createElement("div");
  isEdited.className = "is-edited";
  var moodValue = document.createElement("div");
  moodValue.className = "mood-value";

  var submissionNode = document.createElement("div");
  submissionNode.className = "submission";
  submissionNode.id = answerBase + id;
  submissionNode.appendChild(timeUpdated);
  submissionNode.appendChild(accountName);
  submissionNode.appendChild(response);
  submissionNode.appendChild(isEdited);
  submissionNode.appendChild(moodValue);


  return submissionNode;
}

function comprepensiveFeedbackSubmissionDisplayFactory(form) {

  // <div class="filled-form" id="eventform1312-attendee-34">
  //   <div class="form-title"></div>
  //   <div class="form-description"></div>
  //   <div class="answer", id="answer321">
  //     <div class="question-title">Question title</div>
  //     <div class="question-description">question description</div>
  //     <div class="time-updated"></div>
  //     <div class="account-name"></div>
  //     <div class="response"></div>
  //     <div class="is-edited"></div>
  //     <div class="mood-value"></div>
  //   </div>
  //   <div class="answer", id="answer321">
  //     <div class="question-title">Question title</div>
  //     <div class="question-description">question description</div>
  //     <div class="time-updated"></div>
  //     <div class="account-name"></div>
  //     <div class="response"></div>
  //     <div class="is-edited"></div>
  //     <div class="mood-value"></div>
  //   </div>
  // </div>

  var outputNode = document.createElement("div");
  outputNode.setAttribute("class", "filled-form");

  outputNode.appendChild(document.createElement("div")).setAttribute("class", "time-updated");
  outputNode.appendChild(document.createElement("div")).setAttribute("class", "account-name");

  var form_title_node = outputNode.appendChild(document.createElement("div"));
  form_title_node.setAttribute("class", "form-title");
  setInnerHTMLSanitized(form_title_node, form.data.title);

  var form_description_node = outputNode.appendChild(document.createElement("div"));
  form_description_node.setAttribute("class", "form-description");
  setInnerHTMLSanitized(form_description_node, form.data.description);

  for (var question_number in form.questions) {
    var question = form.questions[question_number];
    var answer = document.createElement("div");
    answer.setAttribute("class", "answer");

    var question_text_node = document.createElement("div");
    question_text_node.setAttribute("class", "question-text");
    setInnerHTMLSanitized(question_text_node, question.data.text);
    answer.appendChild(question_text_node);
    answer.appendChild(document.createElement("div")).setAttribute("class", "time-updated");
    answer.appendChild(document.createElement("div")).setAttribute("class", "response");
    answer.appendChild(document.createElement("div")).setAttribute("class", "is-edited");
    answer.appendChild(document.createElement("div")).setAttribute("class", "mood-value");
    outputNode.appendChild(answer);
  }

  return outputNode;

}


function getGeneralFeedbackSubmission(id) {
return document.getElementById(answerBase + id);
}



// var base = " http://backend.mood-net/v0";
// var base = " http://localhost:8001/v0";

var timeOfLastUpdateUnparsed = "2001-01-22T19:33:02";
var timeOfLastUpdate = Date.parse(timeOfLastUpdateUnparsed);
var eventID = 1; // TODO retrieve this from somewhere
var answerBase = "answer";

var formsCache = []; // stores data like this:
// {
//   "formID": 42343
// 	"hostID": 234212,
// 	"data": {
// 		"title": "Form title",
// 		"description": "Form description"
// 	},
// 	"questionIDs": [312321, 4354325]
//   "questions": [
//     {
//       "questionID": 312321,
//       "formID": 432423,
//       "data": {
//         "type": "longanswer",
//         "text": "How did you find today's presentation?",
//         "options": null
//       }
//     }, 
//     {
//       "questionID": 312321,
//       "formID": 432423,
//       "data": {
//         "type": "longanswer",
//         "text": "How did you find today's presentation?",
//         "options": null
//       }
//     }, 
//   ]
// }




var eventData = null;

// download event data
function getAllEventData(eventID) {

  $.getJSON(endpointToRealAddress("/events/" + eventID), function (event) {
    eventData = event;
    event["forms"] = [];

    var uncompleted_forms = event.formIDs.length;
    event.formIDs.forEach(formID => {
      $.getJSON(endpointToRealAddress("/forms/" + formID), function(form) {
        // need to add this, the form GET doesn't return this, it's only in the request
        form["formID"] = formID;
        form["questions"] = [];
        event["forms"][event.formIDs.indexOf(formID)] = form;

        var uncompleted_questions = form.questionIDs.length;
        form.questionIDs.forEach(questionID => {
          $.getJSON(endpointToRealAddress("/questions/" + questionID), function(question) {
            // insert into questions
            form.questions[form.questionIDs.indexOf(questionID)] = question;

            if (--uncompleted_questions == 0) {
              if (--uncompleted_forms == 0) {
                // we have finished getting event data - now start filling in feedback

                console.log(JSON.stringify(eventData));
                console.log(eventData);
                updateFeedback();
                setInterval(updateFeedback, 5000);
              }
            }

          });
        });
      });
    });
  });
}


getAllEventData(eventID);


function updateFeedback() {
  console.log("updating feedback");
  $.getJSON(endpointToRealAddress("/feedback?eventID=" + eventID + "&time-updated-since=" + timeOfLastUpdateUnparsed), function(data) {

    console.log(data);
    data.list.forEach(submission => {
      
    // for (var i = 0; i < data.list.length; i++) {
      // submission = data.list[i];
      
      // need to store the newest time-submitted sent by the server so that we are working on the server's clock, not the client's.
      // store the parsed and unparsed versions so that the unparsed can be sent next time - easier than converting back to string
      var thisTime = Date.parse(submission["time-updated"]);
      if (thisTime > timeOfLastUpdate) {
        timeOfLastUpdate = thisTime;
        timeOfLastUpdateUnparsed = submission["time-updated"];
      }





      if (submission.formID == 0 /* general feedback form */) {

        var node;

        // check if the answer has already been output to the page before
        var oldResponse = document.getElementById(answerBase + submission.answers[0].answerID);
        if (oldResponse != null){
          node = oldResponse;
        } else {
          // create a new submission element in the document to place the data.
          node = generalFeedbackSubmissionDisplayFactory(submission.answers[0].answerID);
        }


        // populate
        setInnerHTMLSanitized(node.getElementsByClassName("time-updated")[0], submission.answers[0]["time-updated"]);

        if (submission["account-name"] == null){
          setInnerHTMLSanitized(node.getElementsByClassName("account-name")[0], "(anonymous user)");
        } else {
          setInnerHTMLSanitized(node.getElementsByClassName("account-name")[0], submission["account-name"]);
        }
        setInnerHTMLSanitized(node.getElementsByClassName("response")[0], submission.answers[0].data.response);
        setInnerHTMLSanitized(node.getElementsByClassName("is-edited")[0], submission["is-edited"]);
        setInnerHTMLSanitized(node.getElementsByClassName("mood-value")[0], submission.answers[0]["mood-value"]);

        // display
        document.getElementById("general-feedback-container").prepend(node);


      }



      else { // comprepensive feedback


        // TODO check it is not already in the document

        // find the correct form
        var formIndex = eventData.formIDs.indexOf(submission.formID);
        if (formIndex == null) return;
        var form = eventData.forms[formIndex];

        var node = comprepensiveFeedbackSubmissionDisplayFactory(form);

        setInnerHTMLSanitized(node.getElementsByClassName("time-updated")[0], submission["time-updated"]);
        if (submission["account-name" == null])
          setInnerHTMLSanitized(node.getElementsByClassName("account-name")[0], "(anonymous user)");
        else
          setInnerHTMLSanitized(node.getElementsByClassName("account-name")[0], submission["account-name"]);

        var node_answers = node.getElementsByClassName("answer");
        for (answer_num in submission.answers) {
          var answer = submission.answers[answer_num];
          // find where to put it in the node
          var position = form.questionIDs.indexOf(answer.questionID);
          var ans_node = node_answers[position];
          setInnerHTMLSanitized(ans_node.getElementsByClassName("time-updated")[0], answer["time-updated"]);
          setInnerHTMLSanitized(ans_node.getElementsByClassName("response")[0], answer.data["response"]);
          setInnerHTMLSanitized(ans_node.getElementsByClassName("is-edited")[0], answer["is-edited"]);
          setInnerHTMLSanitized(ans_node.getElementsByClassName("mood-value")[0], answer["mood-value"]);
        }





        document.getElementById("comprehensive-feedback-container").prepend(node);




        

      }

    });
  });

}

// setInterval(updateFeedback, 5000); // update every 5 secs
