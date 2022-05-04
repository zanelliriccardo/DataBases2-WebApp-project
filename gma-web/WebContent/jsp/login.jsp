<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Login</title>
</head>
<%@page import="it.polimi.gma.enums.AuthType"%>
<%@page import="it.polimi.gma.webutils.AuthenticationChecker" %> 
<% 
	AuthType auth = AuthenticationChecker.checkAuth(session);
	if (auth.equals(AuthType.CONSUMER)) {
		response.sendRedirect(request.getContextPath() + "/consumerHome"); 
	}
	if (auth.equals(AuthType.ADMIN)) {
		response.sendRedirect(request.getContextPath() + "/adminHome"); 
	}
%>
<body>
	<form method="post" action="login">
		<div>
			Username*: <input type="text" name="username" required/>
		</div>
		<div>
			Password*: <input type="password" id="password" name="password" required/>
		</div>
		<input type="submit" value="Login"/>
		
		<% if(request.getAttribute("errorMessage")!= null) { %>
		<div>
			<%=request.getAttribute("errorMessage") %>
		</div>
		<% } %>
		
		<input type="checkbox" name="adminLogin" value="true" > Administrator login<BR>
		<p> You're not registered?
            <a href="/gma-web/jsp/registration.jsp" >Click here to Sign Up</a>
        
        
	</form>
</body>
</html>