<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="it.polimi.gma.enums.AuthType"%>
<%@page import="it.polimi.gma.webutils.AuthenticationChecker" %>    
<%@page import="it.polimi.gma.entities.Questionnaire"%>
<%@page import="it.polimi.gma.entities.User"%>   
<%@page import="java.util.List"%>  
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Date" %>
<%

AuthType auth = AuthenticationChecker.checkAuth(session);
if (!auth.equals(AuthType.ADMIN)) {
	response.sendRedirect(request.getContextPath() + "/login"); 
}

int availablePageNumber = 1;
if(request.getAttribute("availablePageNumber") != null && (int)request.getAttribute("availablePageNumber") > 1){
	availablePageNumber = (int)request.getAttribute("availablePageNumber");
}


String pastOrPlanned = "past";
if( request.getAttribute("pastOrPlanned") != null && 
	(request.getAttribute("pastOrPlanned").equals("past") || request.getAttribute("pastOrPlanned").equals("planned"))){
	pastOrPlanned = (String)request.getAttribute("pastOrPlanned");
}

int currentPage = 1;
if(pastOrPlanned.equals("past") && session.getAttribute("pageHistory") != null){
	currentPage = (int)session.getAttribute("pageHistory");
}else if(pastOrPlanned.equals("planned") && session.getAttribute("pagePlanned") != null){
	currentPage = (int)session.getAttribute("pagePlanned");
}

String errorMessage = (String)request.getAttribute("errorMessage");

List<Questionnaire> questionnaires = (List<Questionnaire>)request.getAttribute("questionnairesHistory");
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
String todayDate = sdf.format(new Date());

User u = (User)session.getAttribute("user"); 

%> 

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>gma: AdminHome</title>
<style>
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
</head>
<body>
<h2>Hi <%= u.getUsername() %></h2>
<div style="text-align: right;"><a href="/gma-web/logout">Logout</a></div>
<h3><%= pastOrPlanned %> questionnaires</h3>
	<p id="errorMessage" style="color: red"><%= (errorMessage != null)? errorMessage : "" %></p>
	<%if(questionnaires != null && !questionnaires.isEmpty()){ %>
			<table>
				<tr>
					<th>
						Questionnaire Date
					</th>
					<th>
						Associated Product
					</th>
					<th>
						Delete Questionnaire
					</th>
				</tr>	
				<%for(Questionnaire q: questionnaires){ %>
					<tr>
						<td>
							<a href="recap?date=<%=sdf.format(q.getDate())%>"><%=sdf.format(q.getDate())%></a>
						</td>
						<td>
							<%= (q.getProduct() != null) ? q.getProduct().getName() : "no product associated" %>
						</td>
						<td>
							<%if(sdf.format(q.getDate()).compareTo(todayDate) < 0){%>
								<a href="deleteQuestionnaire?questionnaireDeleteDate=<%=q.getDate()%>">delete</a>
							<%}else{%>
								-
							<%} %>
						</td>
					</tr>
				<%}%>
			<%}else{ %>
				<li>no questionnaire found</li>
			<%}%>	
			</table>
	<div style="text-align: center; margin-top: 15px;">
		<button <%= (currentPage==1) ? "disabled" : ""%> onclick="document.getElementById('previous').submit()">previous</button>
		page: <%= currentPage %>/<%= availablePageNumber %> 
		<button <%= (currentPage < availablePageNumber) ? "" : "disabled" %> onclick="document.getElementById('next').submit()">next</button>
	</div>
	<button onclick="document.getElementById('showOtherQuestionnaires').submit()" ><%= (pastOrPlanned.equals("past")) ? "show planned" : "show past" %></button>
	<button onclick="document.getElementById('addQuestionnaireForm').submit()" >add questionnaire</button>
	
	
	<form id="next" method="post" action="adminHome">
		<input type="hidden" name="page" value="<%= (currentPage+1) %>">
		<input type="hidden" name="pastOrPlanned" value="<%= (pastOrPlanned.equals("past") ? "past" : "planned") %>">
	</form>
	<form id="previous" method="post" action="adminHome">
		<input type="hidden" name="page" value="<%= (currentPage==1) ? 1 : (currentPage-1) %>">
		<input type="hidden" name="pastOrPlanned" value="<%= (pastOrPlanned.equals("past") ? "past" : "planned") %>">
	</form>
	<form id="showOtherQuestionnaires" method="post" action="adminHome">  
		<input type="hidden" name="pastOrPlanned" value="<%= (pastOrPlanned.equals("past") ? "planned" : "past") %>">
	</form>
	<form id="addQuestionnaireForm" method="post" action="newQuestionnaireForm"></form>
</body>
</html>