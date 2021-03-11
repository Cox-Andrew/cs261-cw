function validateForm() {
    const fname = document.getElementById('fname').value;
    const email = document.getElementById('email').value;
    const password = document.getElementById('password').value;
    const cpassword = document.getElementById('cpassword').value;
    const errorElement = document.getElementById('error');

    let messages = [];
    if(password != cpassword)
    {
        messages.push('Password does not match');
    }
    if(password.length < 8)
    {
        messages.push('Password must contain at least 8 characters');
    }
    if(password.length > 20)
    {
        messages.push('Password cannot contain more than 20 characters');
    }
    if(messages.length > 0)
    {
        errorElement.innerText = messages.join(', ');
        return false;
    }
    else
    {
        // Call backend function here
        $.post(endpointToRealAddress("/attendees"), JSON.stringify({
            email: email,
            pass: password,
            "account-name": fname
        }), function(response) {
            // store attendee in cookie
            setCookie("attendeeID", response.attendeeID, 1);
            // redirect to signed in page
            window.location.href = "/AttendeeHomepage.html";
        }).fail(function() {
            alert("An error occurred");
        });

        return true;
    }
}
