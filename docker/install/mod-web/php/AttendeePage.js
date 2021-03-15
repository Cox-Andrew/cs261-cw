var gen = document.getElementById("general");
var feed = document.getElementById("feedback");
var refreshCheckID = 0;
function general() {
  if (gen.style.display === "none" || gen.style.display === '')
  {
    if(feed.style.display !== "none" || feed.style.display !== '')
    {
      feed.style.display = "none";
    }
    gen.style.display = "block";
  } else {
    gen.style.display = "none";
  }
}
function feedback() {
  if (feed.style.display === "none" || feed.style.display === '')
  {
    if(gen.style.display !== "none" || gen.style.display !== '')
    {
      gen.style.display = "none";
    }
    feed.style.display = "block";
    getComprehensive();
  } else {
    feed.style.display = "none";
  }
}
function showDiv() {
  document.getElementById("msg").style.display= " block";
}


// feedbackIsOpen(function(isOpen, timeStart, timeEnd) {
  
// });
function feedbackIsOpen(callback) {

  var serverTime;
  var timeStart;
  var timeEnd;
  function isOpen() {
    var isOpen = timeStart < serverTime && serverTime < timeEnd;
    callback(isOpen, timeStart, timeEnd);
  }

  var waiting = 2;
  $.getJSON(endpointToRealAddress("/server-time"), function(s_t) {
    serverTime = new Date(s_t["server-time"]);
    if (--waiting == 0) isOpen();
  });
  $.getJSON(endpointToRealAddress("/events/" + getCookie("eventID")), function(event) {
    timeStart = new Date(event.data["time-start"]);
    timeEnd = new Date(event.data["time-end"]);
    if (--waiting == 0) isOpen();
  });
}



function submitGeneral() {
  var emoji = document.getElementsByName('emoji');
  var emojiValue = null;
  for(i = 0; i < emoji.length; i++)
  {
    if(emoji[i].checked)
      emojiValue = emoji[i].value;
  }
  //emojiValue is the name of selected emoji
  var comments = document.getElementById('comments').value;

  // Call backend function here
  
  // check to see that the time is valid
  feedbackIsOpen(function(isOpen, timeStart, timeEnd) {
    if (isOpen) {
      if (emojiValue != null) moodInsert(emojiValue);
      generalInsert(comments);
    } else {
      alert("This event is not in progress. It is/was scheduled for between "+timeStart.toLocaleString()+ " and " + timeEnd.toLocaleString() + ".");
    }
  });

  return false;

}

function moodInsert(emojiValue) {
  mood = {};
  mood["mood-value"] = emojiValue;
  mood["eventID"] = getCookie("eventID");
  $.ajax({
    type: "POST",
    url: endpointToRealAddress("/moods"),
    dataType: "json",
    contentType: "application/json",
    data: JSON.stringify(mood),
    async: false,
    success: function(result, status, xhr){
      //moodID = result.moodID;
      //TODO
    }
  });
}

function generalInsert(comments) {
  answer = {};
  answer["attendeeID"] = getCookie("attendeeID");
  eventID = getCookie("eventID");
  answer["eventID"] = eventID;
  answer["questionID"] = 0;
  answer.data = {};
  answer.data["response"] = comments;
  answer.data["isAnonymous"] = document.getElementById("AnonymousGeneral").checked;
  $.ajax({
    type: "GET",
    url: endpointToRealAddress("/events/" + eventID),
    dataType: "json",
    async: false,
    success: function(result, status, xhr){
      eventFormIDs = result.eventFormIDs
    }
  });
  Array.from(eventFormIDs).forEach(eventFormID => {
    $.ajax({
      type: "GET",
      url: endpointToRealAddress("/event-forms/" + eventFormID),
      dataType: "json",
      async: false,
      success: function(result, status, xhr){
        if (result.formID == 0) {
          returnEventFormID = eventFormID;
          answer["eventFormID"] = returnEventFormID;
          $.ajax({
            type: "POST",
            url: endpointToRealAddress("/answers"),
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify(answer),
            async: false,
            success: function(result, status, xhr){
              //answerID = result.answerID;
              //TODO
              return;
            }
          });
        }
      }
    });
  });

}

var noForm = document.createElement("div");
noForm.setAttribute("id","noForm");
setInnerHTMLSanitized(noForm, "There is no feedback form right now" );

var intervalID = window.setInterval(getComprehensive, 5000);

// function getComprehensive() {
//   const currentDiv = document.getElementById("CForm");
//   boolform = true;
//   eventID = getCookie("eventID");
//   $.ajax({
//     type: "GET",
//     url: endpointToRealAddress("/events/" + eventID),
//     dataType: "json",
//     async: false,
//     success: function(result, status, xhr){
//       eventFormIDs = result.eventFormIDs
//     }
//   });
//   Array.from(eventFormIDs).forEach(eventFormID => {
//     $.ajax({
//       type: "GET",
//       url: endpointToRealAddress("/event-forms/" + eventFormID),
//       dataType: "json",
//       async: false,
//       success: function(result, status, xhr){
//         activity = result["isActive"];
//         formID = result["formID"];
//         if (activity && formID != 0) {
//           boolform = false;
//           if (removeForm(eventFormID)) {
//             if (("" + getCookie("eventFormID") + "a" + getCookie("attendeeID")) != getCookie("submittedEventFormID")) {
//               form = document.getElementById("CForm");
//               form.setAttribute("onsubmit","submitComprehensive(); return false");
//             }
//             else {
//               form = document.getElementById("CForm");
//               form.setAttribute("onsubmit","editComprehensive(); return false");
//             }
//             var remNoForm = document.getElementById("noForm");
//             if (remNoForm != null) {
//               remNoForm.remove();
//             }
//             setCookie("eventFormID", eventFormID , 1);
//             $.ajax({
//               type: "GET",
//               url: endpointToRealAddress("/forms/" + formID),
//               dataType: "json",
//               async: false,
//               success: function(result, status, xhr){
//                 questionIDs = result["questionIDs"];
//                 i = 1;
//                 Array.from(questionIDs).forEach(questionID => {
//                   $.ajax({
//                     type: "GET",
//                     url: endpointToRealAddress("/questions/" + questionID),
//                     dataType: "json",
//                     async: false,
//                     success: function(result, status, xhr){
//                       setCookie("questionID" + i, questionID , 1);
//                       type = result.data["type"];
//                       text = result.data["text"];
//                       options = result.data["options"];
//                       var br = document.createElement("br");
//                       br.setAttribute("id","br");
//                       var questionDiv = document.createElement("div");
//                       questionDiv.className = "question1";
//                       var questionLabel = document.createElement("Label");
//                       questionLabel.setAttribute("id", "formLabel");
//                       setInnerHTMLSanitized(questionLabel, text );
//                       questionDiv.appendChild(questionLabel);
//                       if (type == "long") {
//                         var questionEntry = document.createElement("textarea");
//                         questionEntry.className = "textarea";
//                         questionEntry.setAttribute("style",'font-size: 16px; font-family: "poppins", sans-serif');
//                         questionEntry.setAttribute("id","ques" + i);
//                         questionEntry.setAttribute("rows","2");
//                         questionEntry.setAttribute("cols","15");
//                         questionEntry.setAttribute("placeholder"," ");
//                         currentDiv.appendChild(questionDiv);
//                         currentDiv.appendChild(br);
//                         currentDiv.appendChild(questionEntry);
//                         var br = document.createElement("br");
//                         br.setAttribute("id","br");
//                         currentDiv.appendChild(br);
//                       }
//                       else if (type == "short") {
//                         //TODO only enter two words + make distinguishable
//                         var questionEntry = document.createElement("textarea");
//                         questionEntry.className = "textarea";
//                         questionEntry.setAttribute("style",'font-size: 16px; font-family: "poppins", sans-serif');
//                         questionEntry.setAttribute("id","ques" + i);
//                         questionEntry.setAttribute("rows","2");
//                         questionEntry.setAttribute("cols","15");
//                         questionEntry.setAttribute("placeholder"," ");
//                         currentDiv.appendChild(questionDiv);
//                         currentDiv.appendChild(br);
//                         currentDiv.appendChild(questionEntry);
//                         var br = document.createElement("br");
//                         br.setAttribute("id","br");
//                         currentDiv.appendChild(br);
//                       }
//                       else if (type == "options") {
//                         //TODO
//                       }
//                       else if (type == "rating") {
//                         //TODO
//                       }
//                     }
//                   });
//                   i++;
//                 });
//                 var checkbox = document.createElement("div");
//                 checkbox.className = "checkbox";
//                 checkbox.setAttribute("id","AnonComp");
//                 var checkinput = document.createElement("input");
//                 var checktext = document.createTextNode(" Anonymous");
//                 checkinput.setAttribute("type","checkbox");
//                 checkinput.setAttribute("name","anonymous");
//                 checkinput.setAttribute("id","AnonymousComp");
//                 checkinput.setAttribute("placeholder","Anonymous");
//                 checkbox.appendChild(checkinput);
//                 checkbox.appendChild(checktext);
//                 currentDiv.appendChild(checkbox);

//                 var br = document.createElement("br");
//                 currentDiv.appendChild(br);

//                 var onlySubmitOnce = document.createElement("div");
//                 onlySubmitOnce.setAttribute("id","msgFeed");
//                 setInnerHTMLSanitized(onlySubmitOnce, "You can edit your feedback in 5 seconds." );
//                 currentDiv.appendChild(onlySubmitOnce);
//                 var submitButton = document.createElement("input");
//                 submitButton.setAttribute("id","btnFeed");
//                 submitButton.setAttribute("class","submitFeed");
//                 submitButton.setAttribute("type","submit");
//                 if (("" + getCookie("eventFormID") + "a" + getCookie("attendeeID")) != getCookie("submittedEventFormID")) {
//                   submitButton.setAttribute("value","Submit");
//                 } else {
//                   submitButton.setAttribute("value","Edit Submission");
//                 }
//                 currentDiv.appendChild(submitButton);
//                 return;
//               }
//             });
//           }
//         }
//       }
//     });
//   });
//   if (boolform) {
//     removeForm(0);
//     currentDiv.appendChild(noForm);
//   }
//   return;

// }
var getComprehensiveIsBusy = false;

function getComprehensive() {

  if (getComprehensiveIsBusy) return;
  getComprehensiveIsBusy = true;

  const currentDiv = document.getElementById("CForm");
  boolform = true;
  eventID = getCookie("eventID");

  function callOnceDone() {
    if (boolform) {
      removeForm(0);
      currentDiv.appendChild(noForm);
    }
    getComprehensiveIsBusy = false;
  }

  $.ajax({
    type: "GET",
    url: endpointToRealAddress("/events/" + eventID),
    dataType: "json",
    success: function(result, status, xhr){
      eventFormIDs = result.eventFormIDs;

      var remainingEventFormIDsToProcess = eventFormIDs.length; //  so that the "if (boolform) .." section doesn't get run until we are done
      if (remainingEventFormIDsToProcess == 0) {
        callOnceDone();
      }

      Array.from(eventFormIDs).forEach(eventFormID => {
        $.ajax({
          type: "GET",
          url: endpointToRealAddress("/event-forms/" + eventFormID),
          dataType: "json",
          success: function(result, status, xhr){
            activity = result["isActive"];
            formID = result["formID"];
            if (activity && formID != 0) {
              boolform = false;
              if (removeForm(eventFormID)) {
                if (("" + getCookie("eventFormID") + "a" + getCookie("attendeeID")) != getCookie("submittedEventFormID")) {
                  form = document.getElementById("CForm");
                  form.setAttribute("onsubmit","submitComprehensive(); return false");
                }
                else {
                  form = document.getElementById("CForm");
                  form.setAttribute("onsubmit","editComprehensive(); return false");
                }
                var remNoForm = document.getElementById("noForm");
                if (remNoForm != null) {
                  remNoForm.remove();
                }
                setCookie("eventFormID", eventFormID , 1);
                $.ajax({
                  type: "GET",
                  url: endpointToRealAddress("/forms/" + formID),
                  dataType: "json",
                  success: function(result, status, xhr){

                    var checkbox = document.createElement("div");
                    checkbox.className = "checkbox";
                    checkbox.setAttribute("id","AnonComp");
                    var checkinput = document.createElement("input");
                    var checktext = document.createTextNode(" Anonymous");
                    checkinput.setAttribute("type","checkbox");
                    checkinput.setAttribute("name","anonymous");
                    checkinput.setAttribute("id","AnonymousComp");
                    checkinput.setAttribute("placeholder","Anonymous");
                    checkbox.appendChild(checkinput);
                    checkbox.appendChild(checktext);
                    currentDiv.appendChild(checkbox);
    
                    var br = document.createElement("br");
                    currentDiv.appendChild(br);
    
                    var onlySubmitOnce = document.createElement("div");
                    onlySubmitOnce.setAttribute("id","msgFeed");
                    setInnerHTMLSanitized(onlySubmitOnce, "You can edit your feedback in 5 seconds." );
                    currentDiv.appendChild(onlySubmitOnce);
                    var submitButton = document.createElement("input");
                    submitButton.setAttribute("id","btnFeed");
                    submitButton.setAttribute("class","submitFeed");
                    submitButton.setAttribute("type","submit");
                    if (("" + getCookie("eventFormID") + "a" + getCookie("attendeeID")) != getCookie("submittedEventFormID")) {
                      submitButton.setAttribute("value","Submit");
                    } else {
                      submitButton.setAttribute("value","Edit Submission");
                    }
                    currentDiv.appendChild(submitButton);
                    
                    
                    questionIDs = result["questionIDs"];

                    var remainingQuestionIDsToProcess = questionIDs.length;
                    if (remainingQuestionIDsToProcess == 0) {
                      if (--remainingEventFormIDsToProcess == 0) {
                        callOnceDone();
                      }
                    }

                    // need to make sure the questions are inserted in the correct order - insert them into this list first, then loop through in turn and add to currentDiv
                    var elsWhoseChildrenWillBeInsertedToCurrentDiv = [];

                    Array.from(questionIDs).forEach(function(questionID, j) {
                      var i = j+1;
                      $.ajax({
                        type: "GET",
                        url: endpointToRealAddress("/questions/" + questionID),
                        dataType: "json",
                        success: function(result, status, xhr){
                          setCookie("questionID" + i, questionID , 1);
                          type = result.data["type"];
                          text = result.data["text"];
                          options = result.data["options"];
                          var br = document.createElement("br");
                          br.setAttribute("class","br");
                          var questionDiv = document.createElement("div");
                          questionDiv.className = "question1";
                          var questionLabel = document.createElement("Label");
                          questionLabel.setAttribute("id", "formLabel");
                          setInnerHTMLSanitized(questionLabel, text );
                          questionDiv.appendChild(questionLabel);

                          elsWhoseChildrenWillBeInsertedToCurrentDiv[j] = document.createElement("div");

                          if (type == "long") {
                            var questionEntry = document.createElement("textarea");
                            questionEntry.className = "textarea";
                            questionEntry.setAttribute("style",'font-size: 16px; font-family: "poppins", sans-serif');
                            questionEntry.setAttribute("id","ques" + i);
                            questionEntry.setAttribute("rows","2");
                            questionEntry.setAttribute("cols","15");
                            questionEntry.setAttribute("placeholder"," ");
                            elsWhoseChildrenWillBeInsertedToCurrentDiv[j].appendChild(questionDiv);
                            elsWhoseChildrenWillBeInsertedToCurrentDiv[j].appendChild(br);
                            elsWhoseChildrenWillBeInsertedToCurrentDiv[j].appendChild(questionEntry);
                            var br = document.createElement("br");
                            br.setAttribute("class","br");
                            elsWhoseChildrenWillBeInsertedToCurrentDiv[j].appendChild(br);
                          }
                          else if (type == "short") {
                            //TODO only enter two words + make distinguishable
                            var questionEntry = document.createElement("textarea");
                            questionEntry.className = "textarea";
                            questionEntry.setAttribute("style",'font-size: 16px; font-family: "poppins", sans-serif');
                            questionEntry.setAttribute("id","ques" + i);
                            questionEntry.setAttribute("rows","2");
                            questionEntry.setAttribute("cols","15");
                            questionEntry.setAttribute("placeholder"," ");
                            elsWhoseChildrenWillBeInsertedToCurrentDiv[j].appendChild(questionDiv);
                            elsWhoseChildrenWillBeInsertedToCurrentDiv[j].appendChild(br);
                            elsWhoseChildrenWillBeInsertedToCurrentDiv[j].appendChild(questionEntry);
                            var br = document.createElement("br");
                            br.setAttribute("class","br");
                            elsWhoseChildrenWillBeInsertedToCurrentDiv[j].appendChild(br);
                          }
                          else if (type == "options") {
                            //TODO
                          }
                          else if (type == "rating") {
                            //TODO
                          }

                          if (--remainingQuestionIDsToProcess == 0) {
                            // now all questions are downloaded, so now we can insert them in the correct order
                            elsWhoseChildrenWillBeInsertedToCurrentDiv.forEach(questionItems => {
                              questionItems.childNodes.forEach(insertToDoc => {
                                currentDiv.appendChild(insertToDoc);
                              });
                            });
                            if (--remainingEventFormIDsToProcess == 0) {
                              callOnceDone();
                            }
                          }

                        }
                      });
                    });
                    return;
                  }
                });
              } else if (--remainingEventFormIDsToProcess == 0) {
                callOnceDone();
              } 
            } else if (--remainingEventFormIDsToProcess == 0) {
              callOnceDone();
            } 
          }
        });
      });
    }
  });

  return;

}











function editComprehensive() {
  i = 1;
  while(document.getElementById("ques" + i)) {
    answer = document.getElementById("ques" + i).value;
    editAnswer(answer, i);
    i++;
  }
}

function editAnswer(answer, numInForm) {
  answers = {};
  answers.data = {};
  answers.data["response"] = answer;
  answers.data["isAnonymous"] = document.getElementById("AnonymousComp").checked;

  $.ajax({
    type: "PUT",
    url: endpointToRealAddress("/answers/" + getCookie("answerID" + i)),
    dataType: "json",
    data: JSON.stringify(answers),
    async: false,
    success: function(result, status, xhr){
      //answerID = result.answerID;
      //TODO
    }
  });
}

function removeForm(eventFormID) {
  i = 1;
  check = false;
  if (refreshCheckID != eventFormID) {
    Array.from(document.getElementsByClassName("br")).forEach(br => {
      br.remove();
    })
    while(document.getElementById("ques" + i)) {
      formQ = document.getElementById("ques" + i);
      formQ.remove();
      // br = document.getElementById("br");
      // br.remove();
      // br = document.getElementById("br");
      // br.remove();
      labelQ = document.getElementById("formLabel");
      labelQ.remove();
      i++;
    }
    if (document.getElementById("AnonymousComp")) {
      rem = document.getElementById("AnonymousComp");
      rem.remove();
    }
    if (document.getElementById("AnonComp")) {
      rem = document.getElementById("AnonComp");
      rem.remove();
    }
    if (document.getElementById("br")) {
      rem = document.getElementById("br");
      rem.remove();
    }
    if (document.getElementById("btnFeed")) {
      submitButton = document.getElementById("btnFeed");
      submitButton.remove();
    }
    if (document.getElementById("msgFeed")) {
      onlySubmitOnce = document.getElementById("msgFeed");
      onlySubmitOnce.remove();
    }
    check = true;
  }
  refreshCheckID = eventFormID;
  return check;
}

function submitComprehensive() {
  i = 1;
  while(document.getElementById("ques" + i)) {
    answer = document.getElementById("ques" + i).value;
    submitAnswer(answer, i);
    i++;
  }
  form = document.getElementById("CForm");
  form.setAttribute("onsubmit","editComprehensive(); return false");
  newButton = document.getElementById("btnFeed");
  newButton.setAttribute("value","Edit Submission");
  setCookie("submittedEventFormID", ("" + getCookie("eventFormID") + "a" + getCookie("attendeeID")), 1);
}

function submitAnswer(answer, numInForm) {
  answers = {};
  answers.data = {};
  answers.data["response"] = answer;
  answers.data["isAnonymous"] = document.getElementById("AnonymousComp").checked;
  answers["attendeeID"] = getCookie("attendeeID");
  answers["eventID"] = getCookie("eventID");
  answers["eventFormID"] = getCookie("eventFormID");
  answers["questionID"] = getCookie("questionID" + numInForm);

  $.ajax({
    type: "POST",
    url: endpointToRealAddress("/answers"),
    dataType: "json",
    contentType: "application/json",
    data: JSON.stringify(answers),
    async: false,
    success: function(result, status, xhr){
      setCookie("answerID"+numInForm, result.answerID,1);
      //TODO
    }
  });
}
