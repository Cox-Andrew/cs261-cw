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
