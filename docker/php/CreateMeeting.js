function submitForm() {
  const title = document.getElementById("name").value;
  const date = document.getElementById("date").value;
  const stime = document.getElementById("starttime").value;
  const ftime = document.getElementById("endtime").value;
  const desc = document.getElementById("desc").value;
  // Call backend function here
  createMeeting(title, date, stime, ftime, desc);
}


function createMeeting(title, date, stime, ftime, desc) {
  event = {};
  //retrieve seriesID from cookie
  event.seriesID = getCookie("seriesID");
  event.data = {};
  event.data["title"] = title;
  event.data["description"] = desc;
  //compare dates
  event.data["time-start"] = date + "T" + stime + ":00"
  event.data["time-end"] = date + "T" + ftime + ":00"

  // check that the time-end is after time-start
  var parsedTimeStart = new Date(event.data["time-start"]);
  var parsedTimeEnd = new Date(event.data["time-end"]);

  if (parsedTimeStart.getTime() >= parsedTimeEnd.getTime()) {
    document.getElementById("error").innerHTML = "Meeting End Time must be after Meeting Start Time";
    return false;
  }


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
      window.location.href = "/MeetingPagemenu.html";
    }
  });

}
