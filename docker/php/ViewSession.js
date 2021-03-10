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

var outerElement = document.getElementById("general-feedback-container");
outerElement.prepend(submissionNode);

return submissionNode;

}

function getGeneralFeedbackSubmission(id) {
return document.getElementById(base + id);
}

function setInnerHTMLSanitized(element, unsanitized) {
  element.innerHTML = DOMPurify.sanitize(unsanitized);
}


var base = "http://localhost:8001/v0";
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
$.getJSON(base + "/events/" + eventID, function (event) {
  eventData = event;
  event["forms"] = [];
  event.formIDs.forEach(formID => {
    $.getJSON(base + "/forms/" + formID, function(form) {
      // need to add this, the form GET doesn't return this, it's only in the request
      form["formID"] = formID;
      form["questions"] = [];
      event["forms"][event.formIDs.indexOf(formID)] = form;
      form.questionIDs.forEach(questionID => {
        $.getJSON(base + "/questions/" + questionID, function(question) {
          // insert into questions
          form.questions[form.questionIDs.indexOf(questionID)] = question;
        });
      });
    });
  });
});



function updateFeedback() {
  console.log("updating feedback");
  $.getJSON(base + "/feedback?eventID=" + eventID + "&time-updated-since=" + timeOfLastUpdateUnparsed, function(data) {

    console.log(data);
    for (var i = 0; i < data.list.length; i++) {
      submission = data.list[i];
      
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
          setInnerHTMLSanitized(node.getElementsByClassName("account-name")[0], "(Anonymous user)");
        } else {
          setInnerHTMLSanitized(node.getElementsByClassName("account-name")[0], submission["account-name"]);
        }
        setInnerHTMLSanitized(node.getElementsByClassName("response")[0], submission.answers[0].data.response);
        setInnerHTMLSanitized(node.getElementsByClassName("is-edited")[0], submission["is-edited"]);
        setInnerHTMLSanitized(node.getElementsByClassName("mood-value")[0], submission.answers[0]["mood-value"]);


      }



      else { // comprepensive feedback

        // check to see if the form has already been downloaded
        var form;
        for (i=0; i<formsCache.length;i++) {
          if (formsCache[i].formID == submission.formID) {
            form = formsCache[i].formID;
            break;
          }
        }
        // if not, need to get the form, and all the questions
        

      }

    }
  });

}

updateFeedback();
setInterval(updateFeedback, 5000); // update every 5 secs
