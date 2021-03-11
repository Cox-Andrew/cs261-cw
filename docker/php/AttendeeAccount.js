

var attendeeID = 9;

function fillInAttendeeAccountInfo () {
	$.getJSON(endpointToRealAddress("/attendees/" + attendeeID), function(attendee) {
		setInnerHTMLSanitized($("#fullname")[0], attendee.data["account-name"]);
		setInnerHTMLSanitized($("#email")[0], attendee.data["email"]);
	})
	.fail(function() {alert("error - unable to get attendee info")})
	;
}

function deleteAttendee() {
	$.ajax({
		url: endpointToRealAddress("/attendees/" + attendeeID),
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

fillInAttendeeAccountInfo();