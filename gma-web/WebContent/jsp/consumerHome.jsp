<%@page import="it.polimi.gma.entities.Review"%>
<%@page import="java.util.Base64"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="it.polimi.gma.entities.Product"%>
<%@page import="it.polimi.gma.entities.User"%>
<%@page import="it.polimi.gma.entities.Consumer"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>gma: ConsumerHome</title>
</head>
<body>
<%@page import="it.polimi.gma.enums.AuthType"%>
<%@page import="it.polimi.gma.webutils.AuthenticationChecker" %> 
<% 
	AuthType auth = AuthenticationChecker.checkAuth(session);
	if (!auth.equals(AuthType.CONSUMER)) {
		response.sendRedirect(request.getContextPath() + "/login"); 
	}
	
	Review r = (Review)request.getAttribute("review");
%>
<% 
	boolean banned = false;
	if(session.getAttribute("user") != null && ((Consumer)session.getAttribute("user")).isBanned()) { 
		banned = true; %>
<script>window.alert("You are banned, ur mum didn't tell you to not use the bad words??")</script>
<% 	} %>
	
	<h1>Gamified Marketing Application</h1>
	<div style="text-align: right;"><a href="/gma-web/logout">Logout</a></div>
	<%  
		Product p = (Product)request.getAttribute("product");
	%>

	
	<h3>
		Hi
		<%= ((User)session.getAttribute("user")).getUsername() %>!
	</h3>
	<% if(banned) { %>
		<b><span style="color: red">Your account is banned because you used an offensive word in your last questionnaire.</span></b>
		<br>(If you have complaints please send an email at <a href="mailto:foo@bar.polimi.it">foo@bar.polimi.it</a>)
	<% } %>
	<!-- If there is a product of the day -->
	<% if(p != null) { %>
		<h2>Product of the day:</h2>
		<div style="border-style: solid;">
			<div style="margin: 10px"><b><%= p.getName() %></b></div>
			<% 
			byte[] image = p.getProductImage();
			if (image != null) {
			%>
				<img src="data:image/jpg;base64,<%= Base64.getMimeEncoder().encodeToString(image) %>" style="width: 400px">
			<%
			}
			else {
			%>
				<img src="${pageContext.request.contextPath}/images/image_not_found.png" style="width: 400px"/>
			<%
			}
			%>
			<% if(r != null) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy"); %>
				<div style="margin: 5px"><i>"<%= r.getText() %>"</i> - <%= r.getAuthor().getUsername() %> (<%= sdf.format(r.getDate()) %>)</div>
			<% } %>
		</div>
		<% if(banned || (session.getAttribute("canSubmit")!=null && !(boolean) session.getAttribute("canSubmit"))) { %>
		<div>
			<button disabled="disabled">Compile Questionnaire</button>
		</div>
		<% } else { %>
		<div>
			<form id="logoutForm" method="post" action="questionnaireOfTheDay"></form>
			<button onclick="window.location.href = 'compileQuestionnaire'">Compile Questionnaire</button>
		</div>
		<% } %>
	<!-- If there is no product of the day -->	
	<% } else { %>
	<h2>No product of the day found, come back tomorrow!</h2> 
	<% } %>
	
	<div>
		<a href="/gma-web/leaderboard">Leaderboard</a>
	</div>
</body>
</html>