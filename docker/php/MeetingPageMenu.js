

var hostIDString = getCookie("eventID");
if (hostIDString == "") {
	window.location.href = "/MeetingList.html"
}
var hostID = parseInt(hostIDString);

$.getJSON(endpointToRealAddress("/events/" + hostID), function(data) {
	setInnerHTMLSanitized(document.getElementsByClassName("MeetingName")[0], data.data.title);
});


function deleteEvent() {
	$.ajax({
		url: endpointToRealAddress("/events/" + hostID),
		type: "DELETE",
		success: function() {
			document.getElementsByTagName("body")[0].prepend("meeting deleted");
			window.location.href = "/SessionPage.html";
		},
		error: function(XMLHttpRequest, textStatus, errorThrown) {
			document.getElementsByTagName("body")[0].prepend("error: " + textStatus);
		}

	});
}