var temp1 = document.getElementById("general");
var temp2 = document.getElementById("feedback");
function general() {
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
function feedback() {
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

google.charts.load("current", {packages:["corechart"]});
google.charts.setOnLoadCallback(drawChart);
function drawChart() {
  var data = google.visualization.arrayToDataTable([
    ['Task', 'Hours per Day'],
    ['Work',     11],
    ['Eat',      2],
    ['Commute',  2],
    ['Watch TV', 2],
    ['Sleep',    7]
  ]);

  var options = {
    title: 'My Daily Activities',
    is3D: true,
  };

  var chart = new google.visualization.PieChart(document.getElementById('piechart_3d'));
  chart.draw(data, options);
}
