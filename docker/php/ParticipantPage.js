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
// function submitGeneral() {
//   document.getElementById("submitGen").disabled = true;
//   setTimeout(function() {
//       document.getElementById("submitGen").disabled = false;
//   }, 25000);
// }
// document.getElementById("submitGen").addEventListener("click", submitGeneral);

// $(function()
// {
//   $("#btn").click(function()
//   {
//     $("btn").attr("disabled","disabled");
//     setTimeout(function()
//     {
//       $("#btn").removeAttr("disabled");
//     },3000);
//   });
// });