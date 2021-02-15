<html>
<head>
<title>Sample JSP Page</title>
<meta>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%> 
</meta>
</head>
 
<body>
    <c:out value="Jetty JSP Configuration Example"></c:out>
    <br /> 
    <% System.out.println("Hello Java Code Geeks"); %>
    <div align="center" style="margin-top: 50px;">
  
        <form id="loginForm">
            Please enter your Username:  <input type="text" name="username" size="20px"> <br>
            Please enter your Password:  <input type="text" name="password" size="20px"> <br><br>
            <input type="submit" value="submit">
        </form>
  
        </div>
</body>
</html>