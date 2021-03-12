function joinEvent(eventID) {
  setCookie("eventID", eventID, 1);
  return true;
}

function pastEventDisplay(eventIDs) {
  i = 0;
  const currentDiv = document.getElementById("links");
  Array.from(eventIDs).forEach(eventID => {
    $.ajax({
      type: "GET",
      url: endpointToRealAddress("/events/" + eventID),
      dataType: "json",
      async: false,
      success: function(result, status, xhr){
        title = result.data["title"];
        timeEnd = result.data["time-end"];
      }
    });
    var dateEnd = new Date(timeEnd);
    if (loadTime > dateEnd) {
      var br = document.createElement("br");
      var eventName = document.createElement("a");
      eventName.className = "eventName";
      eventName.setAttribute("href","AttendeePastforms.html");
      eventName.setAttribute("onclick", "joinEvent(" + eventID + ")");
      setInnerHTMLSanitized(eventName, title);
      currentDiv.appendChild(eventName);
      currentDiv.appendChild(br);
      i++;
    }
  });
  if (i == 0) {
    var noEvents = document.createElement("div");
    noEvents.className = "noEvents";
    setInnerHTMLSanitized(noEvents, "You have no past meetings.");
    currentDiv.appendChild(noEvents);
  }

}

function registeredEventIDs() {
  attendeeID = getCookie("attendeeID");
  eventIDs = [];
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

var loadTime = new Date();
eventIDs = registeredEventIDs();
pastEventDisplay(eventIDs);
