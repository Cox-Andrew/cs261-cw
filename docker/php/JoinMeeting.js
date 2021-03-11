function submitForm() {
  const inviteCode = document.getElementById('mname').value;
  var checkbox = document.getElementById('anonymous');

  var anonymous = 0;
  if (checkbox.checked == true){
      anonymous = 1;   //anonymous is 1 when selected and 0 otherwise
  }

  // Call backend function here
  // On success, Redirect to "AttendeePage.html"
  joinMeeting(inviteCode);
}

function joinMeeting(inviteCode) {
  register = {};
  register["invite-code"] = inviteCode;
  register["attendeeID"] = getCookie("attendeeID");

  $.ajax({
    type: "POST",
    url: endpointToRealAddress("/register-event"),
    dataType: "json",
    contentType: "application/json",
    data: JSON.stringify(register),
    async: false,
    success: function(result, status, xhr){
      eventID = result.eventID;
      setCookie("eventID", eventID, 1);
      window.location.href = "/AttendeePage.html";
    }
  });
}
