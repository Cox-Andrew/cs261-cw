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


var base = "http://localhost:8001/v0";
var timeOfLastUpdate = "2020-01-22T19:33:02";
var eventID = 1; // TODO retrieve this from somewhere
var generalFeedbackIDBase = "generalFeedbackAnswer"

function updateFeedback() {
  console.log("updating feedback");
  $.getJSON(base + "/feedback?eventID=" + eventID + "&time-updated-since=" + timeOfLastUpdate, function(data) {

    console.log(data);
    data.list.forEach(submission => {
      

      if (submission.formID == 1 /* general feedback form */) {

        // create a new submission element in the document to place the data.
        var node = generalFeedbackSubmissionDisplayFactory(eventID);

        // populate
        node.getElementsByClassName("time-updated")[0].innerHTML = submission.answers[0]["time-updated"];
        if (submission["account_name"] == null){
          node.getElementsByClassName("account-name")[0].innerHTML = "<Anonymous user>";
        } else {
          node.getElementsByClassName("account-name")[0].innerHTML = submission["account_name"];
        }
        node.getElementsByClassName("response")[0].innerHTML = submission.answers[0].data.response;
        node.getElementsByClassName("is-edited")[0].innerHTML = submission["is-edited"];
        node.getElementsByClassName("mood-value")[0].innerHTML = submission.answers[0]["mood-value"];


      }

    });
  });

}

updateFeedback();
