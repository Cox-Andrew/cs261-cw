function endpointToRealAddress(endpoint) {
  url = new URL(window.location.protocol + "//" + window.location.host + "/backend.php");
  url.searchParams.append("target", "/v0" + endpoint);
  return url.toString();
}

function setInnerHTMLSanitized(element, unsanitized) {
  if (unsanitized == null) unsanitized = "";
  element.innerHTML = DOMPurify.sanitize(unsanitized.toString());
}

function setCookie(key, value, expiry) {
        var expires = new Date();
        expires.setTime(expires.getTime() + (expiry * 24 * 60 * 60 * 1000));
        document.cookie = key + '=' + value + ';path=/' + ';expires=' + expires.toUTCString();
}

function getCookie(cname) {
  var name = cname + "=";
  var decodedCookie = decodeURIComponent(document.cookie);
  var ca = decodedCookie.split(';');
  for(var i = 0; i <ca.length; i++) {
    var c = ca[i];
    while (c.charAt(0) == ' ') {
      c = c.substring(1);
    }
    if (c.indexOf(name) == 0) {
      return c.substring(name.length, c.length);
    }
  }
  return "";
}

// download event data

// call like this:
/*
getAllEventData(eventID, function(eventData) {
  // do stuff with eventData
});
*/
function getAllEventData(eventID, callback) {
  $.getJSON(endpointToRealAddress("/events/" + eventID), function (event) {

    var uncompleted_sub_event = event.formIDs.length + event.eventFormIDs.length;

    event.forms = [];
    event.eventForms = [];

    event.formIDs.forEach(formID => {
      $.getJSON(endpointToRealAddress("/forms/" + formID), function(form) {
        // need to add this, the form GET doesn't return this, it's only in the request
        form["formID"] = formID;

        // find the first place in forms array where this form should be placed and that hasn't already been filled in
        // this to fix a bug where there couldn't be duplicates in the forms list
        var formIndex = event.formIDs.indexOf(formID);
        while (event.forms[formIndex] != null) {
          formIndex = event.formIDs.indexOf(formID, formIndex + 1);
        }
        event.forms[formIndex] = form;

        var uncompleted_sub_form = form.questionIDs.length;
        form.questions = [];
        form.questionIDs.forEach(questionID => {
          $.getJSON(endpointToRealAddress("/questions/" + questionID), function(question) {

            // insert into questions
            var questionIndex = form.questionIDs.indexOf(questionID);
            while (form.questions[questionIndex] != null) {
              questionIndex = form.questions.indexOf(questionID, questionIndex + 1);
            }
            form.questions[questionIndex] = question;

            if (--uncompleted_sub_form == 0) {
              if (--uncompleted_sub_event == 0) {
                // we have finished getting event data
                callback(event);
              }
            }
          });
        });
      });
    });
    event.eventFormIDs.forEach(eventFormID => {
      $.get(endpointToRealAddress("/event-forms/" + eventFormID), function(eventForm) {
        var eventFormIndex = form.eventFormIDs.indexOf(eventFormID);
        form.eventForms[eventFormIndex] = eventForm;

        if (--uncompleted_sub_event == 0) {
          callback(event);
        }
      });
    });
  });
}


const navSlide = () => {
    const burger = document.querySelector(".burger");
    const nav = document.querySelector(".nav-links");
    const navLinks = document.querySelectorAll(".nav-links li");
    burger.addEventListener("click", () => {
      //Toggle nav
      nav.classList.toggle("nav-active");

      //Animate Links
      navLinks.forEach((link, index) => {
        if (link.style.animation) {
          link.style.animation = "";
        } else {
          link.style.animation = `navLinkFade 0.5s ease forwards ${
            index / 7 + 0.5
          }s`;
        }
      });

      //Burger Animation
      burger.classList.toggle("toggle");
    });
  };

  navSlide();
