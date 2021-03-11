// call backend function to add meeting name to the live and upcoming events

//div for live     events is under div class=left
//div for upcoming events is under div class=right

function joinEvent(eventID) {
  setCookie("eventID", eventID, 1);
  return true;
}

function liveEventDisplay(eventIDs) {

  const currentDiv = document.getElementById("live");
  Array.from(eventIDs).forEach(eventID => {
    $.ajax({
      type: "GET",
      url: endpointToRealAddress("/events/" + eventID),
      dataType: "json",
      async: false,
      success: function(result, status, xhr){
        title = result.data.title;
      }
    });
    var br = document.createElement("br");
    var eventName = document.createElement("a");
    eventName.className = "eventName";
    eventName.setAttribute("href","AttendeePage.html");
    eventName.setAttribute("onclick", "joinEvent(" + eventID + ")");
    setInnerHTMLSanitized(eventName, "Meeting name:- " + title);
    currentDiv.appendChild(eventName);
    currentDiv.appendChild(br);

  });
}

function registeredEventIDs() {
  attendeeID = getCookie("attendeeID");

  $.ajax({
    type: "GET",
    url: endpointToRealAddress("/register-event?attendeeID=" + attendeeID),
    dataType: "json",
    async: false,
    success: function(result, status, xhr){
      eventIDs = result.eventIDs;
    }
  });
  return eventIDs;
}

eventIDs = registeredEventIDs();
liveEventDisplay(eventIDs);
