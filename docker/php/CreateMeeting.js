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
    contentType: "application/json",
    data: JSON.stringify(event),
    async: false,
    success: function(result, status, xhr){
        eventID = result.eventID;
        setCookie("eventID", eventID, 1);
    }
  });

}
