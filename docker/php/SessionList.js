

var hostIDString = getCookie("hostID");
if (hostID == "") {
	window.location.href = "/HostSignin.html"
}
var hostID = parseInt(hostIDString);


$.getJSON(endpointToRealAddress("/series?hostID=" + hostID), function(listOfSeries) {
	for (const seriesID in listOfSeries) {
		var newA = $("<a></a>").click(function() {
			setCookie("seriesID", ""+seriesID, 1);
			window.location.href = "/SessionPage.html"
		});
		var newA = document.createElement("a");
		newA.setAttribute("onclick")
		document.getElementById("session-list").appendChild
	}
});