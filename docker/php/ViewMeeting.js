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
  for (var quest = 0; quest < form.questionIDs.length; quest++) {
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
        setInnerHTMLSanitized(question_text_node, (quest+1) + ". " + question.data.text);
        question_node.appendChild(question_text_node);

        var answers = document.createElement("div");
        answers.setAttribute("class", "answers");
        question_node.appendChild(answers);
        for (responseNo = 0; responseNo < responseNumber; responseNo++) {
          var answer = document.createElement("div");
          answer.className = "answer";
          answer.appendChild(document.createElement("div")).setAttribute("class", "account-name");
          var responseBlock = document.createElement("div");
          answer.appendChild(responseBlock).setAttribute("class","responseBlock")
          var respDiv = document.createElement("div");
          respDiv.className = "respDiv";
          respDiv.appendChild(document.createElement("span")).setAttribute("class", "response");
          respDiv.appendChild(document.createElement("span")).setAttribute("class", "is-edited");
          responseBlock.appendChild(respDiv);
          responseBlock.appendChild(document.createElement("div")).setAttribute("class", "mood-value");
          responseBlock.appendChild(document.createElement("br"));
          responseBlock.appendChild(document.createElement("div")).setAttribute("class", "time-updated");
          answers.appendChild(answer);
        }
        question_node.appendChild(document.createElement("br"));
        question_node.appendChild(document.createElement("br"));
        outputNode.appendChild(question_node);
      }
    });
  }

  return outputNode;

}

function addToComprehensiveFeedbackDisplay(form) {
  var elements = 0;

  while (document.getElementsByClassName("answers")[0].getElementsByClassName("answer")[elements]) {
    elements++;
  }

  for (var quest = 0; quest < form.questionIDs.length; quest++) {
    questionID = form.questionIDs[quest];
    var answers = document.getElementsByClassName("answers")[quest];
    var answer = document.createElement("div");
    answer.className = "answer";
    answer.appendChild(document.createElement("div")).setAttribute("class", "account-name");
    var responseBlock = document.createElement("div");
    answer.appendChild(responseBlock).setAttribute("class","responseBlock")
    var respDiv = document.createElement("div");
    respDiv.className = "respDiv";
    respDiv.appendChild(document.createElement("span")).setAttribute("class", "response");
    respDiv.appendChild(document.createElement("span")).setAttribute("class", "is-edited");
    responseBlock.appendChild(respDiv);
    responseBlock.appendChild(document.createElement("div")).setAttribute("class", "mood-value");
    responseBlock.appendChild(document.createElement("br"));
    responseBlock.appendChild(document.createElement("div")).setAttribute("class", "time-updated");
    answers.appendChild(answer);
  }


  return elements;
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
function pageLoadFeedback(eventFormID, formID, formNo) {
  $.getJSON(endpointToRealAddress("/feedback?eventID=" + eventID), function(data) {

    // TODO check it is not already in the document
    var responseNumber = 0;
    // find the correct eventform
    data.list.forEach(eventForm => {
      if (eventForm.eventFormID == eventFormID) {
        responseNumber++;
      }
      if (timeOfLastUpdateUnparsed < eventForm["time-updated"]) {
        timeOfLastUpdateUnparsed = eventForm["time-updated"];
      }
    });
    var node;
    var totquestions;
    $.ajax({
      type: "GET",
      url: endpointToRealAddress("/forms/" + formID),
      dataType: "json",
      async: false,
      success: function(result, status, xhr){
        node = comprepensiveFeedbackSubmissionDisplayFactory(result, responseNumber);
        totquestions = result["questionIDs"].length;
      }
    });
    var root = document.getElementById("feedback-container" + formNo);
    root.appendChild(node);

    if (responseNumber != 0) {
      for (var questNo = 0; questNo < totquestions; questNo++) {
        var ans_node = node.getElementsByClassName("answers")[questNo];
        var j=0;
        data.list.forEach(eventForm => {
          if (eventForm.eventFormID == eventFormID) {
            var an_node = ans_node.getElementsByClassName("answer")[j];
            setInnerHTMLSanitized(an_node.getElementsByClassName("account-name")[0], eventForm["account-name"] + ": ");
            setInnerHTMLSanitized(an_node.getElementsByClassName("time-updated")[0], "Time Submitted: " + new Date(eventForm.answers[questNo]["time-updated"]).toLocaleString());
            var resp_node = an_node.getElementsByClassName("respDiv")[0];
            setInnerHTMLSanitized(resp_node.getElementsByClassName("response")[0], eventForm.answers[questNo].data["response"] + " ");
            if (eventForm.answers[questNo]["is-edited"]) {
              setInnerHTMLSanitized(resp_node.getElementsByClassName("is-edited")[0], "(edited) ");
            }
            moodValue = eventForm.answers[questNo]["mood-value"];
            if (moodValue > 0.5) {
              emojiHTML = `<span for="emoji5"><img src="emoji5.png" height="20px" width="20px"/> </span>`
              an_node.getElementsByClassName("mood-value")[0].innerHTML = emojiHTML;
            }
            else if (moodValue > 0.25) {
              emojiHTML = `<span for="emoji4"><img src="emoji4.png" height="20px" width="20px"/> </span>`
              an_node.getElementsByClassName("mood-value")[0].innerHTML = emojiHTML;
            }
            else if (moodValue > -0.25) {
              emojiHTML = `<span for="emoji3"><img src="emoji3.png" height="20px" width="20px"/> </span>`
              an_node.getElementsByClassName("mood-value")[0].innerHTML = emojiHTML;
            }
            else if (moodValue > -0.5) {
              emojiHTML = `<span for="emoji2"><img src="emoji2.png" height="20px" width="20px"/> </span>`
              an_node.getElementsByClassName("mood-value")[0].innerHTML = emojiHTML;
            }
            else if (moodValue > -1.0) {
              emojiHTML = `<span for="emoji1"><img src="emoji1.png" height="20px" width="20px"/> </span>`
              an_node.getElementsByClassName("mood-value")[0].innerHTML = emojiHTML;
            }
            j++;
          }
        });
      }
    }
  });
}

function updateFeedback() {
  $.getJSON(endpointToRealAddress("/feedback?eventID=" + eventID + "&time-updated-since=" + timeOfLastUpdateUnparsed), function(data) {

    //TODO UPDATE TIME
    console.log(timeOfLastUpdateUnparsed);

    // find the correct eventform
    data.list.forEach(eventForm => {
      if (timeOfLastUpdateUnparsed < eventForm["time-updated"]) {
        timeOfLastUpdateUnparsed = eventForm["time-updated"];
      }
      var thisForm = getCookie("eventForm" + eventForm.eventFormID);
      var formID = getCookie("eventForm" + thisForm + "FormID");
      var j = 0;
      var node = document.getElementById("feedback-container" + thisForm);
      $.ajax({
        type: "GET",
        url: endpointToRealAddress("/forms/" + formID),
        dataType: "json",
        async: false,
        success: function(result, status, xhr){
          elements = addToComprehensiveFeedbackDisplay(result);
          totquestions = result["questionIDs"].length;
          for (var questNo = 0; questNo < totquestions; questNo++) {
            var ans_node = node.getElementsByClassName("answers")[questNo];

            var an_node = ans_node.getElementsByClassName("answer")[j + elements];
            setInnerHTMLSanitized(an_node.getElementsByClassName("account-name")[0], eventForm["account-name"] + ": ");
            setInnerHTMLSanitized(an_node.getElementsByClassName("time-updated")[0], "Time Submitted: " + new Date(eventForm.answers[questNo]["time-updated"]).toLocaleString());
            var resp_node = an_node.getElementsByClassName("respDiv")[0];
            setInnerHTMLSanitized(resp_node.getElementsByClassName("response")[0], eventForm.answers[questNo].data["response"] + " ");
            if (eventForm.answers[questNo]["is-edited"]) {
              setInnerHTMLSanitized(resp_node.getElementsByClassName("is-edited")[0], "(edited) ");
            }
            moodValue = eventForm.answers[questNo]["mood-value"];
            if (moodValue > 0.5) {
              emojiHTML = `<span for="emoji5"><img src="emoji5.png" height="20px" width="20px"/> </span>`
              an_node.getElementsByClassName("mood-value")[0].innerHTML = emojiHTML;
            }
            else if (moodValue > 0.25) {
              emojiHTML = `<span for="emoji4"><img src="emoji4.png" height="20px" width="20px"/> </span>`
              an_node.getElementsByClassName("mood-value")[0].innerHTML = emojiHTML;
            }
            else if (moodValue > -0.25) {
              emojiHTML = `<span for="emoji3"><img src="emoji3.png" height="20px" width="20px"/> </span>`
              an_node.getElementsByClassName("mood-value")[0].innerHTML = emojiHTML;
            }
            else if (moodValue > -0.5) {
              emojiHTML = `<span for="emoji2"><img src="emoji2.png" height="20px" width="20px"/> </span>`
              an_node.getElementsByClassName("mood-value")[0].innerHTML = emojiHTML;
            }
            else if (moodValue > -1.0) {
              emojiHTML = `<span for="emoji1"><img src="emoji1.png" height="20px" width="20px"/> </span>`
              an_node.getElementsByClassName("mood-value")[0].innerHTML = emojiHTML;
            }
          }
        }
      });
      j++;
    });
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
  var p=0;
  Array.from(event.eventFormIDs).forEach(eventFormID => {
    $.ajax({
      type: "GET",
      url: endpointToRealAddress("/event-forms/" + eventFormID),
      dataType: "json",
      async: false,
      success: function(result, status, xhr){
        p++;
        var formID = result["formID"];
        $.ajax({
          type: "GET",
          url: endpointToRealAddress("/forms/" + formID),
          dataType: "json",
          async: false,
          success: function(result, status, xhr){
            var title = result.data["title"];
            var hidden = document.createElement("div");
            hidden.className = "hidden"
            var br = document.createElement("br");
            hidden.appendChild(br);
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
  var formNo=0;
  const currentDiv = document.getElementById("content");
  Array.from(event.eventFormIDs).forEach(eventFormID => {
    $.ajax({
      type: "GET",
      url: endpointToRealAddress("/event-forms/" + eventFormID),
      dataType: "json",
      async: false,
      success: function(result, status, xhr){
        formNo++;
        var compHTML = `<div class = "sub-content">
          <h3>Comprehensive Feedback</h3>
          <div id="feedback-container` + formNo +  `" class = "fLeft">
            <h4>Questions</h4>
          </div>
          <button type = "button" class = "activateForm" id = "btnFeed` + formNo + `" onclick="test(` + formNo +`,` + eventFormID + `)">Activate</button>
        </div>
        `;

        var genHTML = `<div class = "sub-content">
          <h3>General Feedback</h3>
            <div id="feedback-container` + formNo +  `" class = "fLeft">
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
        var formID = result["formID"];
        pageLoadFeedback(eventFormID,formID,formNo);
        setCookie("eventForm" + eventFormID, formNo, 1);
        setCookie("eventForm" + formNo + "FormID", formID, 1);
      }
    });
  });
  var intervalID = window.setInterval(function() {updateFeedback()}, 5000);
  getEventFormsFromEvent(event);
}

function test(formNo, eventFormID) {

  eventFormPut = {};
  eventFormPut["isActive"] = true;
  eventFormPut["preceding-eventFormID"] = -2;

  $.ajax({
    type: "PUT",
    url: endpointToRealAddress("/event-forms/" + eventFormID),
    dataType: "json",
    contentType: "application/json",
    data: JSON.stringify(eventFormPut),
    async: false,
    success: function(result, status, xhr2){

    }
  });
  $("#btnFeed" + formNo).attr("disabled", true);
  setInnerHTMLSanitized(document.getElementById("btnFeed" + formNo) ,"Form is now active");
  $("#btnFeed" + formNo).css("cursor","no-drop");
  $("#btnFeed" + formNo).css("background-color","rgb(122, 120, 120)");
  setTimeout(function(){
    $("#btnFeed" + formNo).attr("disabled", false);
    $("#btnFeed" + formNo).attr("onclick", "test2(" + formNo + "," + eventFormID + ")");
    $("#btnFeed" + formNo).css("background-color","transparent");
    $("#btnFeed" + formNo).css("cursor","pointer");
    setInnerHTMLSanitized(document.getElementById("btnFeed" + formNo) ,"End form");
  }, 5000);

}

function test2(formNo, eventFormID) {

  eventFormPut = {};
  eventFormPut["isActive"] = false;
  eventFormPut["preceding-eventFormID"] = -2;

  $.ajax({
    type: "PUT",
    url: endpointToRealAddress("/event-forms/" + eventFormID),
    dataType: "json",
    contentType: "application/json",
    data: JSON.stringify(eventFormPut),
    async: false,
    success: function(result, status, xhr2){

    }
  });
  $("#btnFeed" + formNo).attr("disabled", true);
  setInnerHTMLSanitized(document.getElementById("btnFeed" + formNo) ,"Form has ended");
  $("#btnFeed" + formNo).css("cursor","no-drop");
  $("#btnFeed" + formNo).css("background-color","rgb(122, 120, 120)");


}

var eventID = getCookie("eventID");
getAllEventData(eventID, generatePageData);

// setInterval(updateFeedback, 5000); // update every 5 secs
