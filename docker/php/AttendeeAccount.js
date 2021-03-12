


var hostIDString = getCookie("attendeeID");
if (hostIDString == "") {
	window.location.href = "/AttendeeSignin.html";
}
var hostID = parseInt(hostIDString);


function fillInHostAccountInfo () {
	$.getJSON(endpointToRealAddress("/attendees/" + hostID), function(attendee) {
		setInnerHTMLSanitized($("#fullname")[0], attendee.data["account-name"]);
		setInnerHTMLSanitized($("#email")[0], attendee.data["email"]);
	})
	.fail(function() {alert("error - unable to get attendee info")})
	;
}

function deleteAttendee() {
	$.ajax({
		url: endpointToRealAddress("/attendees/" + hostID),
		type: "DELETE",
		success: function() {
			document.getElementsByTagName("body")[0].prepend("account deleted");
			window.location.href = "/";
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			document.getElementsByTagName("body")[0].prepend("error: " + textStatus);
		}

	});
}

fillInHostAccountInfo();
