function submitForm() {
  const title = document.getElementById("name").value;
  const date = document.getElementById("date").value;
  const stime = document.getElementById("starttime").value;
  const ftime = document.getElementById("endtime").value;
  //if (document.getElementById("desc") != null) {
    const desc = document.getElementById("desc").value;
  /*}
  else {
    const desc = "";
  }*/
  // Call backend function here
  createMeeting(title, date, stime, ftime, desc);
}

var seriesID = 1;

function createMeeting(title, date, stime, ftime, desc) {
  event = {};
  event.seriesID = seriesID;
  event.data = {};
  event.data["title"] = title;
  event.data["description"] = desc;
  //compare dates
  event.data["time-start"] = date + "T" + stime + ":00"
  event.data["time-end"] = date + "T" + ftime + ":00"


  $.ajax({
    type: "POST",
    url: endpointToRealAddress("/events"),
    dataType: "json",
    //dataType: "text",
    contentType: "application/json",
    data: JSON.stringify(event),
    success: function(result, status, xhr){
        //reponse = JSON.parse(result);
        eventID = result.eventID;
        setCookie("eventID", eventID, 1);
        return alert("pass");
    }/*,
    error: function(err) {
      return alert("fail")
    },
    complete: function(response, textStatus) {
      return alert("Hey: " + textStatus);
    },
    always: function() {
      return alert("help");
    }*/
  });

/*
  $.post(endpointToRealAddress("/events"), JSON.stringify({event}), function(response) {
      // store event in cookie
      setCookie("eventID", eventID, 1);
      //window.location.href = "/AttendeePage.html";
  }).fail(function() {
      alert("event not found");
  });
  */
}
