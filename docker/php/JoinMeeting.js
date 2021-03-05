function submitForm() {
    const mname = document.getElementById('mname').value;
    const name = document.getElementById('name').value;
    var checkbox = document.getElementById('anonymous');

    var anonymous = 0;
    if (checkbox.checked == true){
        anonymous = 1;   //anonymous is 1 when selected and 0 otherwise
    }

    // Call backend function here
    // On success, Redirect to "AttendeePage.html"
}