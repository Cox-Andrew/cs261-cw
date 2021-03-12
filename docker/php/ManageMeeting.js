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






// the large container div elements
var pageForms = [];
var pageBubbles = [];




// create a place for a form to be displayed
function blankFormFactory() {

  var formhtml = `<div class = "template1">
    <div class = "form">
      <h3 class="template-title"></h3>
      <h4 class="template-description"></h4>
      <form id = "temp1" >
        <input type = "submit" class = "submitTemp1" value="Save"></input>
      </form>
    </div>
  </div>
  `
  var wrapper = document.createElement("div");
  wrapper.innerHTML = formhtml;
  var newNode = wrapper.firstChild;
  return newNode;
}

// create a place for a question to be displayed
function blankQuestionFactory(hasOptions=false) {
  
  var questionhtml = `<label></label><br><textarea class = "textarea" style='font-size: 16px; font-family: "poppins", sans-serif' rows="2" cols="15"></textarea>`;
  if (hasOptions) questionhtml += '<div class="options"></div>';

  var wrapper = document.createElement("div");
  wrapper.innerHTML = formhtml;
  return wrapper;
}

function blankBubbleFactory() {

}

// create a new form in the browser (not in db yet)
function addNewDisplayForm() {

  displayFormNumber = 0;
  return displayFormNumber;
}

// create a new question in the browser (not in db yet)
function addNewDisplayQuestion(displayFormNumber) {


  displayQuestionNumber = 0;
  return displayQuestionNumber;
}

// submits the newly created form to the backend
function submitDisplayForm(displayFormNumber) {


}


function createAndDisplayForm(eventFormID, formID, form) {

  // make form title and description
  var templateNode = blankFormFactory();
  var formNode = templateNode.getElementsByClassName("form")[0];
  setInnerHTMLSanitized(formNode.getElementsByClassName("template-title")[0], form.data.title);
  setInnerHTMLSanitized(formNode.getElementsByClassName("template-description")[0], form.data.description);

  // create questions
  for (const i in form.questions) {
    var question = form.questions[i];
    var questionID = form.questionIDs[i];
    createQuestion(question, questionID, formNode, i+1);
  }

  // add a bubble at the top of the screen


  // add the form to the page


  // set up page events to switch between bubbles


  

  return newNode;
}

function createQuestion(questionID, question, appendTo, questionDisplayNumber) {

  var questionWrapper = blankQuestionFactory();

  // add text like "Question 1 - multi" in the <label>
  setInnerHTMLSanitized(questionWrapper.getElementsByTagName("label")[0], "Question " + questionDisplayNumber + " - " + question.data.type);

  // insert the current data
  setInnerHTMLSanitized(questionWrapper.getElementsByTagName("textarea")[0], question.data.text);

  // add options
  if (question.data.type == "multi") {
    question.data.options.forEach(option => {

      // create box to contain the output 
      var optionWrapper = document.createElement("div");
      optionWrapper.innerHTML = `<textarea class = "textarea" style='font-size: 16px; font-family: "poppins", sans-serif' rows="1" cols="15">`;
      var optionBox = optionWrapper.childNodes[0];

      // add option box to the form
      setInnerHTMLSanitized(optionBox, option);
      questionWrapper.getElementsByClassName("options")[0].appendChild(optionBox);
    });
  }

  // append the question to the form
  questionWrapper.childNodes.forEach(questionPart => {
    appendTo.appendChild(questionPart);
  });



  
}




// get eventID
var eventIDString = getCookie("eventID");
if (eventIDString == "") {
  window.location.href = "/MeetingList.html"
}
var eventID = parseInt(eventIDString);


// The following items are added to eventData:
// same as the form in formData, but with null for some formIDs that haven't been submitted yet
// and also with "displayFormNumber" field.
// the nested questions also have these two properties, i.e. "displayQuestionNumber"

var eventData;

getAllEventData(eventID, function(e_d) {
  eventData = e_d;

  for (i=0; i<eventData.eventFormIDs.length; i++) {

    var eventFormID = eventData.eventFormIDs[i];
    var formID = eventData.formIDs[i];
    var form = eventData.forms[i];

    createAndDisplayForm(eventFormID, formID, form);
  }
});


