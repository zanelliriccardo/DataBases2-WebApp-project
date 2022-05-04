<%@page import="it.polimi.gma.enums.Sex"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
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
<html>
<head>
<meta charset="UTF-8">
<title>Register</title>
</head>
<body>
	<form method="post" action="/gma-web/register">

		<div>
			E-mail*: <input type="text" id= "email" name="email" required oninput="
				email = document.getElementById('email');
		        var re = /\S+@\S+\.\S+/;
		        if(re.test(email.value)) {
		        	document.getElementById('email_error').textContent = '';
		        }
		        else {
		        	document.getElementById('email_error').textContent = 'Invalid email format.';
		        }
		        console.log(re);
		        console.log(email);
		        console.log(re.test(email))"/>
		<span style= "color: red" id = "email_error"></span></div>
		<div>
			Username*: <input type="text" name="username" required/>
		</div>
		<div>
			Password*: <input type="password" id="password" name="password" required oninput="
				pwd = document.getElementById('password').value;
		        if(pwd.length < 8 || pwd == '') {
		        	document.getElementById('password_error').textContent = 'Password has to be at least 8 characters.';
		        }
		        else {
		        	document.getElementById('password_error').textContent = '';
		        }"/>
		<span style= "color: red" id = "password_error"></span></div>
		<div>
			Repeat password*: <input type="password" id="password_rep" required oninput="
				pwd1 = document.getElementById('password').value;
				pwd2 = document.getElementById('password_rep').value;
		        if(pwd1 != pwd2) {
		        	document.getElementById('password_rep_error').textContent = 'Passwords do not coincide.';
		        }
		        else {
		        	document.getElementById('password_rep_error').textContent = '';
		        }"/>
		<span style= "color: red" id = "password_rep_error"></span></div>
		<div>
			Birth date: <input type="date" name="birthdate" />
		</div>
		<div>Sex:</div>
		<div>
			<input type="radio" name="sex" value="<%=Sex.MALE%>" ><label>Male (<%=Sex.MALE%>)</label>
		</div>
		<div>
			<input type="radio" name="sex" value="<%=Sex.FEMALE%>" /><label>Female (<%=Sex.FEMALE%>)</label>
		</div>
		<div>
			<input type="radio" name="sex" value="<%=Sex.OTHER%>" /><label>Other (<%=Sex.OTHER%>)</label>
		</div>

		<input type="submit" value="Register"/>
		<% if(request.getAttribute("failureReason")!=null) { %>
			<div style="color: red"> <%= request.getAttribute("failureReason") %> </div>
		<% } %>
	</form>
</body>
</html>