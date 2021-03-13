var temp1 = document.getElementById("general");
var temp2 = document.getElementById("feedback");
/*
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
*/
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

function comprepensiveFeedbackSubmissionDisplayFactory(form, responseNumber) {

  var outputNode = document.createElement("div");
  outputNode.setAttribute("class", "filled-form");

  outputNode.appendChild(document.createElement("div")).setAttribute("class", "time-updated");
  /*
  outputNode.appendChild(document.createElement("div")).setAttribute("class", "account-name");

  var form_title_node = outputNode.appendChild(document.createElement("div"));
  form_title_node.setAttribute("class", "form-title");
  setInnerHTMLSanitized(form_title_node, form.data.title);

  var form_description_node = outputNode.appendChild(document.createElement("div"));
  form_description_node.setAttribute("class", "form-description");
  setInnerHTMLSanitized(form_description_node, form.data.description);
  */
  for (quest = 0; quest < form.questionIDs.length; quest++) {
    questionID = form.questionIDs[quest];
    $.ajax({
      type: "GET",
      url: endpointToRealAddress("/questions/" + questionID),
      dataType: "json",
      async: false,
      success: function(question, status, xhr){
        var question_node = document.createElement("div");
        var question_text_node = document.createElement("h3");

        question_node.setAttribute("class", "question-text");
        setInnerHTMLSanitized(question_text_node, question.data.text);
        question_node.appendChild(question_text_node);

        var answers = document.createElement("div");
        answers.setAttribute("class", "answers");
        question_node.appendChild(answers);
          for (responseNo = 0; responseNo < responseNumber; responseNo++) {
            var answer = document.createElement("div");
            answer.className = "answer";
            answer.appendChild(document.createElement("div")).setAttribute("class", "account-name");
            answer.appendChild(document.createElement("div")).setAttribute("class", "time-updated");
            answer.appendChild(document.createElement("div")).setAttribute("class", "response");
            answer.appendChild(document.createElement("div")).setAttribute("class", "is-edited");
            answer.appendChild(document.createElement("div")).setAttribute("class", "mood-value");
            answers.appendChild(answer);
          }
        outputNode.appendChild(question_node);
      }
    });
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
/*
getAllEventData(eventID, function(ev_data) {
  eventData = ev_data;
  updateFeedback();
  setInterval(updateFeedback, 5000);
});
*/
function updateFeedback(eventFormID, formNo) {
  console.log("updating feedback");
  $.getJSON(endpointToRealAddress("/feedback?eventID=" + eventID + "&time-updated-since=" + timeOfLastUpdateUnparsed), function(data) {
    console.log(data);

    var node;

    // TODO check it is not already in the document
    submissions = [];
    names = [];
    totanswers = [];
    questionIDs = [];
    var responseNumber = 0;
    // find the correct eventform
    data.list.forEach(eventForm => {
      if (eventForm.eventFormID == eventFormID) {
        submissions.push(eventForm);
        responseNumber++;
      }
    });
    if (data.list.length != 0) {
      data.list[0].answers.forEach(answer => {
        questionIDs.push(answer["questionID"]);
      });
    }
    //GET QUESTION INFO
    if (submissions.length != 0) {
      formID = submissions[0]["formID"];
      $.ajax({
        type: "GET",
        url: endpointToRealAddress("/forms/" + formID),
        dataType: "json",
        async: false,
        success: function(result, status, xhr){
          node = comprepensiveFeedbackSubmissionDisplayFactory(result, responseNumber);
        }
      });

      for (questNo = 0; questNo < questionIDs.length; questNo++) {
        var ans_node = node.getElementsByClassName("answers")[questNo];
        j=0;
        data.list.forEach(eventForm => {
          if (eventForm.eventFormID == eventFormID) {
            var an_node = ans_node.getElementsByClassName("answer")[j];
            setInnerHTMLSanitized(an_node.getElementsByClassName("account-name")[0], eventForm["account-name"]);
            setInnerHTMLSanitized(an_node.getElementsByClassName("time-updated")[0], "Time Submitted: " + new Date(eventForm.answers[j]["time-updated"]).toLocaleString());
            setInnerHTMLSanitized(an_node.getElementsByClassName("response")[0], eventForm.answers[j].data["response"]);
            if (eventForm.answers[j]["is-edited"]) {
              setInnerHTMLSanitized(an_node.getElementsByClassName("is-edited")[0], "(edited)");
            }
            setInnerHTMLSanitized(an_node.getElementsByClassName("mood-value")[0], "Sentiment: " + eventForm.answers[j]["mood-value"]);
            j++;
          }
        });
      }
      var root = document.getElementById("feedback-container" + formNo);
      root.appendChild(node);
    }
  });
}

var pageForms = [];




function switchForms(newOpenForm) {
  pageForms.forEach(pageForm => {
    pageForm.style.display = "none";
  });
  newOpenForm.style.display = "block";
}

function getEventFormsFromEvent(event) {
  const currentDiv = document.getElementById("heading");
  p=0;
  Array.from(event.eventFormIDs).forEach(eventFormID => {
    $.ajax({
      type: "GET",
      url: endpointToRealAddress("/event-forms/" + eventFormID),
      dataType: "json",
      async: false,
      success: function(result, status, xhr){
        p++;
        formID = result["formID"];
        $.ajax({
          type: "GET",
          url: endpointToRealAddress("/forms/" + formID),
          dataType: "json",
          async: false,
          success: function(result, status, xhr){
            title = result.data["title"];
            var hidden = document.createElement("div");
            hidden.className = "hidden"
            var formName = document.createElement("button");
            formName.className = "formName";
            var temp = document.getElementById("temp" + p);
            $(formName).click(function() {
              switchForms(temp);
            });
            setInnerHTMLSanitized(formName, p + ". " + title);
            currentDiv.appendChild(formName);
            currentDiv.appendChild(hidden);
          }
        });
      }
    });
  });
}

function generatePageData(event) {
  formNo=0;
  const currentDiv = document.getElementById("content");
  Array.from(event.eventFormIDs).forEach(eventFormID => {
    $.ajax({
      type: "GET",
      url: endpointToRealAddress("/event-forms/" + eventFormID),
      dataType: "json",
      async: false,
      success: function(result, status, xhr){
        formNo++;
        compHTML = `<div class = "sub-content">
          <h3>Comprehensive Feedback</h3>
          <div id="donutchart" style="width: 50vw;" class = "left"></div>
          <div id="feedback-container` + formNo +  `" class = "right">
            <h4>Questions</h4>
          </div>
        </div>`;
        genHTML = `<div class = "sub-content">
          <h3>General Feedback</h3>
          <div id="piechart_3d" style="width: 50vw;" class = "left"></div>
            <!-- general feedback is put into here -->
            <div id="feedback-container` + formNo +  `" class = "right">
              <h4>Questions</h4>
            </div>
        </div>`;
        var temp = document.createElement("div");
        if (formNo == 1) {
          temp.className = "general";
          temp.innerHTML = genHTML;
        }
        else {
          temp.className = "feedback";
          temp.innerHTML = compHTML;
        }
        temp.setAttribute("id","temp" + formNo);
        currentDiv.appendChild(temp);
        pageForms.push(temp);

        getAllEventData( eventID , function(ev_data) {
          eventData = ev_data;
          updateFeedback(eventFormID, formNo);
          setInterval(updateFeedback(eventFormID, formNo), 5000);
        });
      }
    });
  });
  getEventFormsFromEvent(event);
}

eventID = getCookie("eventID");
getAllEventData(eventID, generatePageData);

// setInterval(updateFeedback, 5000); // update every 5 secs
