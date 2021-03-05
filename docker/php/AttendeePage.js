var gen = document.getElementById("general");
var feed = document.getElementById("feedback");
function general() {
  if (gen.style.display === "none" || gen.style.display === '')
  {
    if(feed.style.display !== "none" || feed.style.display !== '')
    {
      feed.style.display = "none";
    }
    gen.style.display = "block";
  } else {
    gen.style.display = "none";
  }
}
function feedback() {
  if (feed.style.display === "none" || feed.style.display === '')
  {
    if(gen.style.display !== "none" || gen.style.display !== '')
    {
      gen.style.display = "none";
    }    
    feed.style.display = "block"; 
  } else {
    feed.style.display = "none";
  }
}
function showDiv() {
  document.getElementById("msg").style.display= " block";
}

function submitGeneral() {
  var emoji = document.getElementsByName('emoji');
  var emojiValue = null;
  for(i = 0; i < emoji.length; i++) 
  { 
    if(emoji[i].checked) 
      emojiValue = emoji[i].value;
  } 
  //emojiValue is the name of selected emoji

  var comments = document.getElementById('comments').value;
  alert('Form submitted successfully');

  // Call backend function here
}

function submitComprehensive() {
  var ques1 = document.getElementById('ques1').value;
  var ques2 = document.getElementById('ques2').value;
  var ques3 = document.getElementById('ques3').value;
  alert('Template saved');

  // Call backend function here
}
