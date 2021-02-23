function validateForm() {
    const fname = document.getElementById('fname');
    const email = document.getElementById('email');
    const password = document.getElementById('password');
    const cpassword = document.getElementById('cpassword');
    const errorElement = document.getElementById('error');
    const form = document.getElementById('form');

    let messages = [];
    if(password.value != cpassword.value)
    {
        messages.push('Password does not match');
    }
    if(password.value.length < 8)
    {
        messages.push('Password must contain at least 8 characters');
    }
    if(password.value.length > 20)
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
        return true;
    }  
}
