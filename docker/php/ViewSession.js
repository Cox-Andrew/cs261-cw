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
submissionNode.id = generalFeedbackIDBase + id;
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
var generalFeedbackIDBase = "generalFeedbackAnswer";

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


      if (submission.formID == 1 /* general feedback form */) {

        // create a new submission element in the document to place the data.
        var node = generalFeedbackSubmissionDisplayFactory(submission.answers[0].answerID);

        // populate
        setInnerHTMLSanitized(node.getElementsByClassName("time-updated")[0], submission.answers[0]["time-updated"]);

        if (submission["account_name"] == null){
          setInnerHTMLSanitized(node.getElementsByClassName("account-name")[0], "(Anonymous user)")
        } else {
          setInnerHTMLSanitized(node.getElementsByClassName("account-name")[0], submission["account_name"])
        }
        setInnerHTMLSanitized(node.getElementsByClassName("response")[0], submission.answers[0].data.response)
        setInnerHTMLSanitized(node.getElementsByClassName("is-edited")[0], submission["is-edited"])
        setInnerHTMLSanitized(node.getElementsByClassName("mood-value")[0], submission.answers[0]["mood-value"])


      }

    }
  });

}

updateFeedback();
setInterval(updateFeedback, 5000); // update every 5 secs
