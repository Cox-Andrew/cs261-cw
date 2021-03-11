

var seriesIDString = getCookie("seriesID");
if (seriesIDString == "") {
	alert("no session selected");
	window.location.href = "/SessionList.html";
}
var sessionID = parseInt(seriesIDString);

$.getJSON(endpointToRealAddress("/series/" + sessionID), function(data) {
	setInnerHTMLSanitized(document.getElementsByClassName("SessionName")[0], data.data.title);
});