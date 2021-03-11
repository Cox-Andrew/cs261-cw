function submitForm() {
  const email = document.getElementById('email').value;
  const password = document.getElementById('password').value;
  // Call backend function here
  hostSignIn(email, password);
}


function hostSignIn(email, password) {
  host = {};
  //retrieve hostID from cookie
  host["email"] = email;
  host["pass"] = password;

  $.ajax({
    type: "POST",
    url: endpointToRealAddress("/host-temp-sign-in"),
    dataType: "json",
    contentType: "application/json",
    data: JSON.stringify(host),
    async: false,
    success: function(result, status, xhr){
      hostID = result.hostID;
      setCookie("hostID", hostID, 1);
      window.location.href = "/HostHomepage.html";
    }
  });
}
