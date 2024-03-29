function submitForm() {
  const title = document.getElementById('name').value;
  const desc = document.getElementById('desc').value;
  // Call backend function here
  createSession(title, desc);
}


function createSession(title, desc) {
  series = {};
  //retrieve hostID from cookie
  series.hostID = getCookie("hostID");
  series.data = {};
  series.data["title"] = title;
  series.data["description"] = desc;

  $.ajax({
    type: "POST",
    url: endpointToRealAddress("/series"),
    dataType: "json",
    contentType: "application/json",
    data: JSON.stringify(series),
    async: false,
    success: function(result, status, xhr){
      seriesID = result.seriesID;
      setCookie("seriesID", seriesID, 1);
      window.location.href = "/SessionPage.html";
    }
  });
}
