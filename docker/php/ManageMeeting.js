var temp1 = document.getElementById("template1");
var temp2 = document.getElementById("template2");
function template1() {
  if (temp1.style.display === "none" || temp1.style.display === '')
  {
    if(temp2.style.display !== "none" || temp2.style.display !== '')
    {
      temp2.style.display = "none";
    }
    temp1.style.display = "block";
  } else {
    temp1.style.display = "none";
  }
}
function template2() {
  if (temp2.style.display === "none" || temp2.style.display === '')
  {
    if(temp1.style.display !== "none" || temp1.style.display !== '')
    {
      temp1.style.display = "none";
    }    
    temp2.style.display = "block"; 
  } else {
    temp2.style.display = "none";
  }
}
function showDiv() {
  document.getElementById("msg").style.display= " block";
}
function submitTemplate1() {
  var q1 = document.getElementById('q1').value;
  var q2 = document.getElementById('q2').value;
  var q3 = document.getElementById('q3').value;
  alert('Template saved');

  // Call backend function here
}
function submitTemplate2() {
  var ques1 = document.getElementById('ques1').value;
  var ques2 = document.getElementById('ques2').value;
  var ques3 = document.getElementById('ques3').value;
  alert('Template saved');

  // Call backend function here
}
