

var seriesIDString = getCookie("seriesID");
if (seriesIDString == "") {
	window.location.href = "/SeriesList.html"
}
var seriesID = parseInt(seriesIDString);


$.getJSON(endpointToRealAddress("/series/" + seriesID), function(data) {
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