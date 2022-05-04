<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html>
<%@page import="java.util.*"%>
<%@page import="it.polimi.gma.entities.Consumer"%>
<%@page import="it.polimi.gma.entities.Answer"%>
<%@page import="it.polimi.gma.entities.Questionnaire"%>
<%@page import="it.polimi.gma.enums.AuthType"%>
<%@page import="it.polimi.gma.webutils.AuthenticationChecker" %> 
<% 
	AuthType auth = AuthenticationChecker.checkAuth(session);
	if (!auth.equals(AuthType.ADMIN)) {
		response.sendRedirect(request.getContextPath() + "/login"); 
	}
%>
<%
List<String> submittingUser = (List<String>)request.getAttribute("submittingUser");
List<String> cancellingUser = (List<String>)request.getAttribute("cancellingUser");
List<Answer> answers = (List<Answer>)request.getAttribute("answers");
%>
<html>
<head>
<style>
      .tableFixHead {
        overflow-y: auto;
        height: 20%;
      }
      .tableFixHead thead th {
        position: sticky;
        top: 0;
      }
      table {
        border-collapse: collapse;
        width: 100%;
      }
      th,
      td {
        padding: 8px 16px;
        border: 1px solid #ccc;
      }
      th {
        background: #eee;
      }
    </style>
<meta charset="ISO-8859-1">
<title>Inspection page</title>
</head>
<body>
	<h1>Admin inspection page</h1>
	<form method="get" action="/gma-web/recap">	
	<a href="#submitUsers">SubUsers</a>
	<a href="#cancUsers">CancUsers</a>
	<a href="#answers">Answers</a>
	<a href="/gma-web/adminHome">Home page</a>
	
		<br>
		<section id="submitUsers"></section>
		<label> USERS WHO SUBMITTED</label>
		<div class=tableFixHead>		  
			<table>			 
			   <tr>
			  	<th>Username</th>
			  	 <%if(submittingUser!=null){%>
			    	<%for ( int i =0; i < submittingUser.size(); i++) {%>
					<tr><td><%if(i < submittingUser.size())
								out.println(submittingUser.get(i));}%></td></tr>
				<%}else{%>
				<tr><td>no data found.</td></tr>
				<%} %>
			</table>				
		</div>	
		
			
		<br>
		<section id="cancUsers"></section>
		<label> USERS WHO CANCELLED</label>
		<div class=tableFixHead>			
			 <table>
			    <tr>
			  	<th>Username</th>
			  	 <%if(cancellingUser!=null){%>
			    	<%for ( int i =0; i < cancellingUser.size(); i++) {%>
					<tr><td><%out.println(cancellingUser.get(i));}%></td></tr>
				<%}else{%>
				<tr><td>no data found.</td></tr>
				<%} %>
			  </table>	
		</div>
		
		
		
		<br>
		<section id="answers"></section><label>ANSWERS</label>
		<div class=tableFixHead>
			<table>		  
			  <tr>
			  	<th>User</th>
			    <th>Answer</th>		    
			    <th>Question</th>
			  </tr>			    
			  	<%if(answers!=null){%>
		    	<%for(int i=0;i<answers.size();i++){%>
				<tr>
					<td><%out.println(answers.get(i).getUser().getUsername());%></td>
					<td><%out.println(answers.get(i).getValue());%></td>
					<td><%out.println(answers.get(i).getQuestion().getText());}%></td>
				</tr>
				<%}else{%>
				<tr><td>no data found.</td><td>no data found.</td><td>no data found.</td></tr>
				<% }%>
			  </table>
		</div>
	</form>
</body>
</html>

