function viewForm(eventFormID, formID) {
  setCookie("eventFormID", eventFormID, 1);
  setCookie("formID", formID, 1);
  return true;
}



function getEventFormsFromEvent(event) {
  attendeeID = getCookie("attendeeID");

  const currentDiv = document.getElementById("links");
  Array.from(event.eventFormIDs).forEach(eventFormID => {
    $.ajax({
      type: "GET",
      url: endpointToRealAddress("/event-forms/" + eventFormID),
      dataType: "json",
      async: false,
      success: function(result, status, xhr){
        formID = result["formID"];
        $.ajax({
          type: "GET",
          url: endpointToRealAddress("/forms/" + formID),
          dataType: "json",
          async: false,
          success: function(result, status, xhr){
            title = result.data["title"];
            questionID = result["questionIDs"][0];
            $.ajax({
              type: "GET",
              url: endpointToRealAddress("/questions/" + questionID),
              dataType: "json",
              async: false,
              success: function(result, status, xhr){
                desc = result.data["text"];
                var br = document.createElement("br");
                var formName = document.createElement("a");
                formName.className = "formName";
                formName.setAttribute("href","AttendeePastfeedback.html");
                formName.setAttribute("onclick", "viewForm(" + eventFormID + ", " + formID + ")");
                setInnerHTMLSanitized(formName, title + ": " + desc);
                currentDiv.appendChild(formName);
                currentDiv.appendChild(br);
              }
            });
          }
        });
      }
    });
  });
}

eventID = getCookie("eventID");
getAllEventData(eventID, getEventFormsFromEvent);
