

var hostIDString = getCookie("seriesID");
if (hostIDString == "") {
	window.location.href = "/SeriesList.html"
}
var eventID = parseInt(hostIDString);


$.getJSON(endpointToRealAddress("/series/" + eventID), function(data) {
	data.eventIDs.forEach(eventID => {
		var newLi = document.createElement("li");
		var newA = document.createElement("a");
		newA.setAttribute("href", "#");
		$(newA).click(function() {
			setCookie("eventID", ""+eventID, 1);
			window.location.href = "/MeetingPageMenu.html"
		});
		newLi.appendChild(newA);
		document.getElementById("meeting-list").appendChild(newLi);

		$.getJSON(endpointToRealAddress("/events/"+eventID), function(eventData) {
			setInnerHTMLSanitized(newA, eventData.data.title);
		});
	});
});