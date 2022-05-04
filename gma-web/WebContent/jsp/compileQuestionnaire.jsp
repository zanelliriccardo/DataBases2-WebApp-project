<%@page import="it.polimi.gma.entities.Consumer"%>
<%@page import="java.util.Base64"%>
<%@page import="java.util.Set"%>
<%@page import="java.sql.Date"%>
<%@page import="it.polimi.gma.entities.Question"%>
<%@page import="it.polimi.gma.entities.Questionnaire"%>
<%@page import="it.polimi.gma.entities.Product"%>
<%@page import="it.polimi.gma.enums.AuthType"%>
<%@page import="it.polimi.gma.webutils.AuthenticationChecker" %> 
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>GMA - Compile Questionnaire</title>
</head>
<body>
<% 
	AuthType auth = AuthenticationChecker.checkAuth(session);
	if (!auth.equals(AuthType.CONSUMER)) {
		response.sendRedirect(request.getContextPath() + "/login"); 
	}
	Questionnaire q = (Questionnaire) session.getAttribute("questionnaire");
	Product p = q.getProduct();
%>
	<div style="border-style: solid;">
		<div style="margin: 10px"><i><%= p.getName() %></i></div>
		<% 
		byte[] image = p.getProductImage();
		if (image != null) {
		%>
			<img src="data:image/jpg;base64,<%= Base64.getMimeEncoder().encodeToString(image) %>" style="width: 400px">
		<%
		}
		else {
		%>
			<img src="${pageContext.request.contextPath}/images/image_not_found.png" style="width: 400px"/> <!-- TODO fix the url of the image -->
		<%
		}
		%>
	</div>
	
	<form action="submitQuestionnaire" method="POST">
		<!-- Optional statistical fields -->
		<div id="optional_fields">
			<% if( ((Consumer) session.getAttribute("user")).getAge() != null) { %>
			<input type="checkbox" name="age" value="true"/> I agree to share my <b>age</b> with the producer.<br>
			<% } if( ((Consumer) session.getAttribute("user")).getSex() != null) { %>
			<input type="checkbox" name="sex" value="true"/> I agree to share my <b>sex</b> with the producer.<br>
			<% } %>
			
			My expertise level with the product is:<br>
			<input type="radio" id="low" name="expertise" value="low">
			<label for="low">Low</label><br>
			
			<input type="radio" id="medium" name="expertise" value="medium">
			<label for="medium">Medium</label><br>
			
			<input type="radio" id="high" name="expertise" value="high">
			<label for="high">High</label><br>
		</div>
		
		<!-- Mandatory marketing fields -->
		<div id="mandatory_fields" style="display: none;">
			<% 
			for (Question elem : q.getQuestions()) { 
				if (elem.getPoints() != 1) { 
			%>
				<div style="margin-bottom: 5px">
					<%= elem.getText() %><br>
					<input type="text" name="question<%= elem.getIdQuestion() %>" required="required"/>
				</div>
			<% 
				}
			} 
			%>
			<input type="submit" value="Submit Questionnaire">
		</div>
	</form>
	<button id="switch" onclick=
			"
				optional = document.getElementById('optional_fields');
				mandatory = document.getElementById('mandatory_fields');
				
				button = document.getElementById('switch');
				
				if (optional.style.display == 'none') {
					optional.style.display = 'block';
					mandatory.style.display = 'none';
					button.innerHTML = 'Next'
				}
				else if(mandatory.style.display == 'none') {
					optional.style.display = 'none';
					mandatory.style.display = 'block';
					button.innerHTML = 'Previous'
				}
			">Next</button>
	<button onclick=
	"
		window.location = '/gma-web/submitQuestionnaire?cancel=1';
	">Cancel</button>
</body>
</html>