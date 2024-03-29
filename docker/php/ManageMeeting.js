// var temp1 = document.getElementById("template1");
// var temp2 = document.getElementById("template2");
// function template1() {
//   if (temp1.style.display === "none" || temp1.style.display === '')
//   {
//     if(temp2.style.display !== "none" || temp2.style.display !== '')
//     {
//       temp2.style.display = "none";
//     }
//     temp1.style.display = "block";
//   } else {
//     temp1.style.display = "none";
//   }
// }
// function template2() {
//   if (temp2.style.display === "none" || temp2.style.display === '')
//   {
//     if(temp1.style.display !== "none" || temp1.style.display !== '')
//     {
//       temp1.style.display = "none";
//     }    
//     temp2.style.display = "block"; 
//   } else {
//     temp2.style.display = "none";
//   }
// }
function showDiv() {
  document.getElementById("msg").style.display= " block";
}
// function submitTemplate1() {
//   var q1 = document.getElementById('q1').value;
//   var q2 = document.getElementById('q2').value;
//   var q3 = document.getElementById('q3').value;
//   alert('Template saved');

//   // Call backend function here
// }
// function submitTemplate2() {
//   var ques1 = document.getElementById('ques1').value;
//   var ques2 = document.getElementById('ques2').value;
//   var ques3 = document.getElementById('ques3').value;
//   alert('Template saved');

//   // Call backend function here
// }






// the large container div elements
var pageForms = [];
var pageBubbles = [];

function switchForms(newOpenForm) {
  pageForms.forEach(pageForm => {
    pageForm.style.display = "none";
  });
  newOpenForm.style.display = "block";
}




// create a place for a form to be displayed
function blankFormFactory() {

  var formhtml = `<div class = "template1">
    <div class = "form">
      <h3 class="template-title"></h3>
      <h4 class="template-description"></h4>
      <p class="is-active"></p>
      <form class="event-form-parameters" >
        <input type="button" class="submitForm" value="Save"></button>
        <!--<label>Start Time:</label><input type="time" class="DateTime time-start"></input><br>
        <label>End Time:</label><input type="time" class="DateTime time-end"></input><br> -->

        <label>Title</label>
        <input type="text" class="form-title"></input>
        <label>Description</label>
        <input type="text" class="form-description"></input>


      </form>
      <div class="question-inputs"></div>
      <div class="extra-controls">
        <input type="button" class="addQuestion" value="+ New Question"></button>
        <select class="questionType" name="questionType">
          <option value="long">Long Answer</option>
          <option value="short">Short Answer</option>
        </select>
        <input type="button" class="deleteForm" value="Delete Form From Event"></button>
      </div>
      
    </div>
  </div>
  `
  //         <input type = "submit" class = "submitForm" value="Save"></input>
  var wrapper = document.createElement("div");
  wrapper.innerHTML = formhtml;
  var newNode = wrapper.firstChild;
  return newNode;
}

// create a place for a question to be displayed
function blankQuestionFactory(hasOptions=false) {
  
  var questionhtml = `<div><label></label><input type="button" class="deleteQuestion" value="Delete"></button><textarea class = "textarea" style='font-size: 16px; font-family: "poppins", sans-serif' rows="2" cols="15"></textarea></div>`;
  // if (hasOptions) questionhtml += '<div class="options"></div>';

  var wrapper = document.createElement("div");
  wrapper.innerHTML = questionhtml;
  return wrapper.childNodes[0];
}

function blankBubbleFactory() {
  var bubblehtml = `<button></button>`;
  var wrapper = document.createElement("div");
  wrapper.innerHTML = bubblehtml;
  return wrapper.childNodes[0];
}

// create a new form in the browser (not in db yet)
function addNewDisplayForm() {

  displayFormNumber = 0;
  return displayFormNumber;
}

// create a new question in the browser (not in db yet)
function addNewDisplayQuestion(displayFormNumber) {


  displayQuestionNumber = 0;
  return displayQuestionNumber;
}

// submits the newly created form to the backend
function submitDisplayForm(displayFormNumber) {


}

function formatTime(dateObject) {
  var hour = dateObject.getHours();
  var min  = dateObject.getMinutes();

  hour = (hour < 10 ? "0" : "") + hour;
  min = (min < 10 ? "0" : "") + min;

  return hour+":"+min;

}

function parseTime(timeObj, timeString) {
  var time = timeString.match("^(..):(..)$");
  timeObj.setHours(time[1])
  timeObj.setMinutes(time[2]);
  return timeObj;
}




function createAndDisplayForm(eventFormID, formID, eventForm, form) {

  // make form title and description
  var templateNode = blankFormFactory();
  $(templateNode.getElementsByClassName("submitForm")).click(function() {
    submitForm(eventFormID);
    return false;
  });
  $(templateNode.getElementsByClassName("addQuestion")).click(function() {
    var questionType = templateNode.getElementsByClassName("questionType")[0].value;
    addQuestionToEventForm(eventFormID, questionType);
    return false;
  });
  $(templateNode.getElementsByClassName("deleteForm")).click(function() {
    deleteEventForm(eventFormID);
    return false;
  });

  templateNode.setAttribute("id", "eventForm" + eventFormID);
  var formNode = templateNode.getElementsByClassName("form")[0];

  setInnerHTMLSanitized(formNode.getElementsByClassName("template-title")[0], form.data.title);
  setInnerHTMLSanitized(formNode.getElementsByClassName("template-description")[0], form.data.description);

  // var timeStartText = "Start time not defined";
  // if (eventForm["time-start"] != null){
  //   formNode.getElementsByClassName("time-start")[0].setAttribute("value", formatTime(new Date(eventForm["time-start"])));
  //   // setInnerHTMLSanitized(formNode.getElementsByClassName("time-start")[0], timeStartText);
  // }

  // // var endTimeText = "End time not defined";
  // if (eventForm["time-end"] != null) {
  //   formNode.getElementsByClassName("time-end")[0].setAttribute("value", formatTime(new Date(eventForm["time-end"]))); 
  //   // endTimeText = new Date(eventForm.timeEnd).toLocaleString();
  // }


  formNode.getElementsByClassName("form-title")[0].setAttribute("value", form.data.title);
  formNode.getElementsByClassName("form-description")[0].setAttribute("value", form.data.description);

  // create questions
  for (var i=0; i<form.questions.length; i++) {
    var question = form.questions[i];
    createQuestion(question, eventFormID, formNode.getElementsByClassName("question-inputs")[0], parseInt(i)+1);
  }

  // add a bubble at the top of the screen
  var bubbleNode = blankBubbleFactory();
  bubbleNode.setAttribute("id", "eventForm" + eventFormID + "bubble");
  setInnerHTMLSanitized(bubbleNode, form.data.title);
  document.getElementById("heading").prepend(bubbleNode);
  $(bubbleNode).click(function() {
    switchForms(templateNode);
  });

  // add the form to the page
  templateNode.style.display = "none";
  pageForms.push(templateNode);
  document.getElementById("page-content").appendChild(templateNode);

  return templateNode;

}

function createQuestion(question, eventFormID, appendTo, questionDisplayNumber) {
  var questionOnPage = blankQuestionFactory(question.data.type == "multi");

  // bind delete button
  $(questionOnPage.getElementsByClassName("deleteQuestion")).click(function() {
    deleteQuestion(eventFormID, question.questionID);
    return false;
  });

  // add text like "Question 1 - multi" in the <label>
  setInnerHTMLSanitized(questionOnPage.getElementsByTagName("label")[0], "Question " + questionDisplayNumber + " - " + question.data.type);

  // insert the current data
  setInnerHTMLSanitized(questionOnPage.getElementsByTagName("textarea")[0], question.data.text);

  // set the id of the form element
  questionOnPage.setAttribute("id", "eventForm" + eventFormID + "question" + question.questionID);

  // add options TODO this doesn't work but we're not implementing multichoice anyway
  if (question.data.type == "multi") {
    question.data.options.forEach(option => {

      // create box to contain the output 
      var optionWrapper = document.createElement("div");
      optionWrapper.innerHTML = `<textarea class = "textarea" style='font-size: 16px; font-family: "poppins", sans-serif' rows="1" cols="15">`;
      var optionBox = optionWrapper.childNodes[0];

      // add option box to the form
      setInnerHTMLSanitized(optionBox, option);
      questionOnPage.getElementsByClassName("options")[0].appendChild(optionBox);
    });
  }

  // append the question to the form
  appendTo.appendChild(questionOnPage);
  // questionOnPage.childNodes.forEach(questionPart => {
  //   appendTo.appendChild(questionPart);
  // });



  
}

function newFormRequestToBackend(callback) {

  // insert placeholder null values in eventForms and forms
  var nextIndex = eventData.eventFormIDs.length;

  eventData.formIDs[nextIndex] = null;
  eventData.eventFormIDs[nextIndex] = null;
  eventData.eventForms[nextIndex] = null;
  eventData.forms[nextIndex] = null;

  var remaining_leaf_requests = 2;
  
  $.post(endpointToRealAddress("/forms"), JSON.stringify({
    "hostID": hostID,
    "data": {
      "title": "Untitled Form",
      "description": ""
    }
  }), function(formResult) {
    var formID = formResult.formID;
    eventData.formIDs[nextIndex] = formID;
    // re-get from database & add to data structure
    $.getJSON(endpointToRealAddress("/forms/" + formID), function(form) {
      eventData.forms[nextIndex] = form;
      form["formID"] = formID; // need to add because it is missing from the response
      form.questions = [];
      if (--remaining_leaf_requests == 0) callback(nextIndex);
    });

    // create eventForm (add form to current event)
    $.post(endpointToRealAddress("/event-forms"), JSON.stringify({
      "eventID": eventID,
      "formID": formID,
      "time-start": eventData.data["time-start"],
      "time-end": eventData.data["time-end"],
      "is-active": false,
      "preceding-eventFormID": 0 // TODO
    }), function(eventFormResult) {
      var eventFormID = eventFormResult.eventFormID;
      eventData.eventFormIDs[nextIndex] = eventFormID;

      // re-get from database & add to data structure
      $.getJSON(endpointToRealAddress("/event-forms/" + eventFormID), function(eventForm) {
        eventData.eventForms[nextIndex] = eventForm;
        if (--remaining_leaf_requests == 0) callback(nextIndex);
      });

    });

  });
}


function newForm() {
  newFormRequestToBackend(function(index) {
    var eventFormID = eventData.eventFormIDs[index];
    var formID = eventData.formIDs[index];
    var form = eventData.forms[index];
    var eventForm = eventData.eventForms[index];
    var newFormNode = createAndDisplayForm(eventFormID, formID, eventForm, form);
    switchForms(newFormNode);

  });
}

function submitForm(eventFormID) {

  var documentForm = document.getElementById("eventForm" + eventFormID).getElementsByClassName("form")[0];

  // update eventForm
  var eventFormParameters = documentForm.getElementsByClassName("event-form-parameters")[0];
  // var textInTimeStartBox = eventFormParameters.getElementsByClassName("time-start")[0].value;
  // var textInTimeEndBox = eventFormParameters.getElementsByClassName("time-end")[0].value;
  
  // // parse the dates
  // var newStartTime;
  // var newEndTime;
  // if (eventData.data["time-start"] != null) {
  //   newStartTime = new Date(eventData.data["time-start"]);
  // } else {
  //   alert("Error: the event date is not set!");
  //   return;
  // }
  // if (eventData.data["time-end"] != null) {
  //   newEndTime = new Date(eventData.data["time-end"]);
  // } else {
  //   alert("Error: the event date is not set!");
  //   return;
  // }


  // var timeInTimeStartBox = parseTime(newStartTime, textInTimeStartBox);
  // var timeInTimeEndBox = parseTime(newStartTime, textInTimeEndBox);




  // timeInTimeStartBox.setMilliseconds(0);
  // timeInTimeEndBox.setMilliseconds(0);

  // n

  var form = eventData.forms[eventData.eventFormIDs.indexOf(eventFormID)];

  var textInTitleBox = document.getElementById("eventForm" + eventFormID).getElementsByClassName("form-title")[0].value;
  var textInDescriptionBox = document.getElementById("eventForm" + eventFormID).getElementsByClassName("form-description")[0].value;
  if (textInTitleBox != form.data.title || textInDescriptionBox != form.data.description) {
    var newData = {
      "title": textInTitleBox,
      "description": textInDescriptionBox
    };
    $.ajax({
      url: endpointToRealAddress("/forms/"+ form.formID),
      type: 'PUT',
      data: JSON.stringify({
        data: newData
      }),
      success: function(result) {
        form.data = newData;
        // change the title in the bubble and form elements on page
        setInnerHTMLSanitized(document.getElementById("eventForm" + eventFormID + "bubble"), textInTitleBox);
        setInnerHTMLSanitized(document.getElementById("eventForm" + eventFormID).getElementsByClassName("template-title")[0], textInTitleBox);
        setInnerHTMLSanitized(document.getElementById("eventForm" + eventFormID).getElementsByClassName("template-description")[0], textInDescriptionBox);

      }
    });
  }


  form.questions.forEach(question => {
    // check if the question has been edited in the webpage
    var textInQuestionBox = document.getElementById("eventForm" + eventFormID + "question" + question.questionID).getElementsByClassName("textarea")[0].value;
    
    if (textInQuestionBox != question.data.text) {
      // change the representation in eventData
      question.data.text = textInQuestionBox;
      // make a copy to get into the right format for the PUT
      var questionInPutFormat = {
        data: question.data
      };
      // update database
      $.ajax({
        url: endpointToRealAddress("/questions/"+ question.questionID),
        type: 'PUT',
        data: JSON.stringify(questionInPutFormat),
        success: function(result) {

        }
      });
    }
  });


  // get all the data from the webpage. compare it to eventData, then update if necessary


}

function addQuestionToEventForm(eventFormID, questionType) {
  var formIndex = eventData.eventFormIDs.indexOf(eventFormID);
  var formID = eventData.formIDs[formIndex];
  var questionIDs = eventData.forms[formIndex].questionIDs;
  var lastQuestionID = questionIDs[questionIDs.length - 1];
  if (lastQuestionID == null) lastQuestionID = 0;
  $.post(endpointToRealAddress("/questions"), JSON.stringify({
    "formID": formID,
    "preceding-questionID": lastQuestionID, 
    "data": {
      "type": questionType,
      "text": "",
      "options": []
    }
  }), function(data) {
    var questionID = data.questionID;
    // retrieve data again
    $.getJSON(endpointToRealAddress("/questions/"+questionID), function(question) {
      var insertQuestionAtIndex = eventData.forms[formIndex].questionIDs.length;
      eventData.forms[formIndex].questions[insertQuestionAtIndex] = question;
      eventData.forms[formIndex].questionIDs[insertQuestionAtIndex] = questionID;
      var formNode = document.getElementById("eventForm" + eventFormID).getElementsByClassName("form")[0];
      createQuestion(question, eventFormID, formNode.getElementsByClassName("question-inputs")[0], insertQuestionAtIndex + 1);
    });
  });
}

function deleteEventForm(eventFormID) {
  var formIndex = eventData.eventFormIDs.indexOf(eventFormID);
  $.ajax({
    url: endpointToRealAddress("/event-forms/"+ eventFormID),
    type: 'DELETE',
    success: function(result) {
      var bubbleToDelete = document.getElementById("eventForm" + eventFormID + "bubble");
      bubbleToDelete.parentElement.removeChild(bubbleToDelete);
      // for (const i in pageBubbles) {
      //   if (pageBubbles[i].id == "eventForm" + eventFormID + "bubble") {
      //     // delete bubble from page
      //     pageBubbles[i].parentNode.removeChild(pageBubbles[i]);

      //     pageBubbles = pageBubbles.splice(i, 1);
      // }}
      for (const i in pageForms) {
        if (pageForms[i].id == "eventForm" + eventFormID) {
          // delete form from page
          pageForms[i].parentNode.removeChild(pageForms[i]);
          pageForms.splice(i, 1);
          break;
      }}


      // delete from eventData
      eventData.forms.splice(formIndex, 1);
      eventData.formIDs.splice(formIndex, 1);
      eventData.eventForms.splice(formIndex, 1);
      eventData.eventFormIDs.splice(formIndex, 1);

    }
  });
}


function deleteQuestion(eventFormID, questionID) {
  $.ajax({
    url: endpointToRealAddress("/questions/"+ questionID),
    type: 'DELETE',
    success: function(result) {
      // var displayedQuestionToDelete = document.getElementById("eventForm" + eventFormID + "question" + question.questionID);
      // bubbleToDelete.parentElement.removeChild(bubbleToDelete);

      // delete from eventData
      var formIndex = eventData.eventFormIDs.indexOf(eventFormID);
      var questionIndex = eventData.forms[formIndex].questionIDs.indexOf(questionID);
      eventData.forms[formIndex].questionIDs.splice(questionIndex, 1);
      eventData.forms[formIndex].questions.splice(questionIndex, 1);
      var form = eventData.forms[formIndex];

      // regenreate form on page
      var formNode = document.getElementById("eventForm" + eventFormID);
      formNode.getElementsByClassName("question-inputs")[0].innerHTML = "";
      for (var i=0; i<form.questions.length; i++) {
        var question = form.questions[i];
        createQuestion(question, eventFormID, formNode.getElementsByClassName("question-inputs")[0], parseInt(i)+1);
      }


    }
  });

}



// get eventID
var eventIDString = getCookie("eventID");
if (eventIDString == "") {
  window.location.href = "/MeetingList.html"
}
var eventID = parseInt(eventIDString);

// get hostID
var hostIDString = getCookie("hostID");
if (hostIDString == "") {
  window.location.href = "/HostSignIn.html"
}
var hostID = parseInt(hostIDString);


// The following items are added to eventData:
// same as the form in formData, but with null for some formIDs that haven't been submitted yet
// and also with "displayFormNumber" field.
// the nested questions also have these two properties, i.e. "displayQuestionNumber"

var eventData;

getAllEventData(eventID, function(e_d) {
  eventData = e_d;

  for (i=0; i<eventData.eventFormIDs.length; i++) {

    var eventFormID = eventData.eventFormIDs[i];
    var formID = eventData.formIDs[i];
    var form = eventData.forms[i];
    var eventForm = eventData.eventForms[i];
    if (formID != 0) {
      createAndDisplayForm(eventFormID, formID, eventForm, form);
    }
  }
});




