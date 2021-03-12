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


var eventIDString = getCookie("eventID");
if (eventIDString == "") {
  window.location.href = "/MeetingList.html"
}
var eventID = parseInt(eventIDString);



function generalFeedbackSubmissionDisplayFactory(id) {

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
    answer.setAttribute("class", "answer " + question.data.type);

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


var timeOfLastUpdateUnparsed = "2000-01-01T00:00:00";
var timeOfLastUpdate = Date.parse(timeOfLastUpdateUnparsed);
var answerBase = "answer";
var eventData;

getAllEventData(eventID, function(ev_data) {
  eventData = ev_data;
  updateFeedback();
  setInterval(updateFeedback, 5000);
});


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

          // set the id of the answer
          ans_node.setAttribute("id", "answer" + answer.answerID);
        }





        document.getElementById("comprehensive-feedback-container").prepend(node);




        

      }

    });
  });

}

// setInterval(updateFeedback, 5000); // update every 5 secs
