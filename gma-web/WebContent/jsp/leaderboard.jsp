<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html> 
<%@page import="java.util.*"%> 
<%@page import="it.polimi.gma.entities.Consumer"%> 
<%
List<Consumer> leaderboard = (List<Consumer>)request.getAttribute("leaderboard");
%> 
<%@page import="it.polimi.gma.enums.AuthType"%>
<%@page import="it.polimi.gma.webutils.AuthenticationChecker" %> 
<% 
	AuthType auth = AuthenticationChecker.checkAuth(session);
	if (!auth.equals(AuthType.CONSUMER)) {
		response.sendRedirect(request.getContextPath() + "/login"); 
	}
%>
<html>
<head>
<style>
table, th, td {
  border: 1px solid black;
}
</style>
<meta charset="ISO-8859-1">
<title>Leaderboard</title>
</head>
<body>
	<form method="post" action="/gma-web/leaderboard">
		<table>
		<caption>LEADERBOARD</caption>
		<thead>
			<tr>
			<th>Name </th>
			<th>Points </th>
			</tr>
		</thead>
		<tbody>
			<%for ( int i =0; i < leaderboard.size(); i++) {
				%>
				<tr>
					<td><%out.println(leaderboard.get(i).getUsername());%></td>
					<td><%out.println(leaderboard.get(i).getPoints()); %> </td>
				</tr>
			<%}%>
		</tbody>
		</table>
		<%if(!(Boolean)request.getAttribute("fullLeaderboard")) {%>
	 	<input type="submit" value="show complete leaderboard"/>
	 	<%}%>
		<a href="/gma-web/consumerHome" >Home page</a>
	 </form>
</body>
</html>