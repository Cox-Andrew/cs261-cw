

var hostIDString = getCookie("hostID");
if (hostIDString == "") {
	window.location.href = "/HostSignin.html"
}
var hostID = parseInt(hostIDString);


$.getJSON(endpointToRealAddress("/series?hostID=" + hostID), function(data) {
	data.seriesIDs.forEach(seriesID => {
		var newLi = document.createElement("li");
		var newA = document.createElement("a");
		newA.setAttribute("href", "#");
		$(newA).click(function() {
			setCookie("seriesID", ""+seriesID, 1);
			window.location.href = "/SessionPage.html"
		});
		newLi.appendChild(newA);
		document.getElementById("session-list").appendChild(newLi);

		$.getJSON(endpointToRealAddress("/series/"+seriesID), function(seriesData) {
			setInnerHTMLSanitized(newA, seriesData.data.title);
		});
	});
});