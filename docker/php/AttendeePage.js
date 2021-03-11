var gen = document.getElementById("general");
var feed = document.getElementById("feedback");
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
  } else {
    feed.style.display = "none";
  }
}
function showDiv() {
  document.getElementById("msg").style.display= " block";
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
  moodInsert(emojiValue);
  generalInsert(comments);
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
  answer.data["isAnonymous"] = getCookie("isAnonymous");
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

function getComprehensive() {
  const currentDiv = document.getElementById("form");
  eventID = getCookie("eventID");
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
  var dateStart = new Date(timeStart);
  var dateEnd = new Date(timeEnd);
  var br = document.createElement("br");
  var eventName = document.createElement("a");
  eventName.className = "eventName";
  eventName.setAttribute("href","AttendeePage.html");
  setInnerHTMLSanitized(eventName, title);
  currentDiv.appendChild(eventName);
  currentDiv.appendChild(br);
}

function submitComprehensive() {
  var ques1 = document.getElementById('ques1').value;
  var ques2 = document.getElementById('ques2').value;
  var ques3 = document.getElementById('ques3').value;
  alert('Template saved');

  // Call backend function here
}
