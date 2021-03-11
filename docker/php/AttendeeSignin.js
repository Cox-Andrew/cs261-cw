function submitForm() {
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;

    // TODO this is temporary
    $.post(endpointToRealAddress("/attendee-temp-sign-in"), JSON.stringify({
        email: email,
        pass: password
    }), function(response) {
        // store attendee in cookie
        document.cookie = "attendeeID=" + response.attendeeID;
        // redirect to signed in page
        window.location.href = "/AttendeeHomepage.html";
    }).fail(function() {
        alert("user not found");
    });

}
