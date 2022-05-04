<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<%@page import="it.polimi.gma.entities.Questionnaire"%>  
<%@page import="java.util.List"%>  
<%@page import="java.text.SimpleDateFormat" %>
<%@page import="java.util.Date" %>
<%@page import="it.polimi.gma.entities.Question" %>
<%@page import="it.polimi.gma.services.QuestionnaireCreationService" %>
    
<%
 		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
 		String todayDate = sdf.format(new Date());	
 		
 		String errorMessage = (String)request.getAttribute("errorMessage");
 		QuestionnaireCreationService qcs = (QuestionnaireCreationService)session.getAttribute("questCreationService");
 		List<String> previouslyInsertedQuestions = qcs.getQuestions();
 		
 		String stringQuestions = "[";
 		if(previouslyInsertedQuestions != null && previouslyInsertedQuestions.size() > 0){
 			for(String pq: previouslyInsertedQuestions){
 				stringQuestions = stringQuestions+"\""+pq+"\", ";
 			}
 			stringQuestions = stringQuestions+"]";
 		}
%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>gma: QuestionnaireCreation</title>

	<style type="text/css">
		table {
			margin-top: 15px;
			margin-bottom: 15px;
			border-collapse: collapse;
       		width: 100%;
	    }
	    th,td {
	        padding: 8px 16px;
	        border: 1px solid #ccc;
	    }
	    th {
	        background: #eee;
	    }
	    .question{
	    	width: 90%;
	    }
	    .delete{
	    	width: 10%;
	    }
	</style>
	<script>
	
		<% if(previouslyInsertedQuestions != null && previouslyInsertedQuestions.size() > 0){%>
			var questions = <%= stringQuestions %>;
		<%}else{%>
			var questions = new Array();
		<%}%>
		
		function isDuplicated(question){		
			for(var c = 0; c < questions.length; c++){
				if(questions[c]===question){
					return true;
				}	
			}	
			return false;
		}
		
		function createDeleteLink(index){
			var a = document.createElement('a');
			var linkText = document.createTextNode("delete");
			a.appendChild(linkText);
			a.setAttribute("onclick","deleteQuestion("+index+")");
			a.href = "#";
			
			return a;
		}
		
		function deleteQuestion(index){
			var table = document.getElementById("questionsTable");
			table.deleteRow(index);
			questions.splice(index, 1);
			for(var k=index; k<questions.length; k++){
				table.rows[k].cells[1].removeChild(table.rows[k].cells[1].lastChild);
				table.rows[k].cells[1].appendChild(createDeleteLink(k));
			}
		}
		
		function addRowToTable(text){
			var table = document.getElementById("questionsTable");
			var row = table.insertRow();
			var cell0 = row.insertCell(0);
			var cell1 = row.insertCell(1);
			cell0.innerHTML = text;
			cell1.appendChild(createDeleteLink(questions.length-1));
		}
		
		function addHiddenInputs(){
			for(var k=0; k<questions.length; k++){
				var hiddenInput = document.createElement("input");
				hiddenInput.setAttribute("type","hidden");
				hiddenInput.setAttribute("name","question"+k);
				hiddenInput.value = questions[k];			
				document.getElementById("createQuestionnaireForm").appendChild(hiddenInput);				
			}
		}
		
		function printError(error){
			var errorMsg = document.createTextNode("please, write the question first");
			document.getElementById("errorMessage").innerHTML='';
			document.getElementById("errorMessage").appendChild(errorMsg);
		}
		
		function addQuestion(){
			var insertedText = document.getElementById("questionTextField").value; 
			if(insertedText.length <= 0){
				printError("please, write the question first");
			}else if(isDuplicated(insertedText)){
				printError("the question is already present");
			}else{
				questions.push(insertedText);
				addRowToTable(insertedText);
			}
			document.getElementById("questionTextField").value='';
		}
		
		function submitForm(){
				if(questions.length > 0){
					addHiddenInputs();
					document.getElementById("createQuestionnaireForm").submit();
				}else{
					printError("insert at least a question, please");
				}
		}
	</script>

</head>
<body>
	<h1>Questionnaire Creation Form</h1>
	<div style="text-align: right;"><a href="/gma-web/adminHome">Home</a></div>
	<div>
		<table id="questionsTable">
		<tr>
			<th class="question">Question</th>
			<th class="delete">Delete</th>
		</tr>
		<%if(previouslyInsertedQuestions != null && previouslyInsertedQuestions.size() > 0){
			int i = 0;
			for(String pq: previouslyInsertedQuestions){%>
			<tr>
				<td class="question"><%= pq %></td>
				<td class="delete"><a onclick="deleteQuestion(<%= i %>)" href="#">delete</a></td>
			</tr>
		<% }}%>
		</table>
		<form name="createQuestionnaireForm" id="createQuestionnaireForm" action="CreateNewQuestionnaire" method="POST">
			<label>insert question:</label>
			<input type="text" id="questionTextField" name="newQuestion" placeholder="question ?">
			<button type="button" onclick="addQuestion()">addQuestion</button><br>
			<label>select a date:</label>
			<input type="date" name="questionnaireDate" min="<%= todayDate %>" value="<%= todayDate %>">
			<p id="errorMessage" style="color:red"><%= (errorMessage == null)? "":errorMessage %></p>
			<button type="button" onclick="submitForm()">create questionnaire</button>
		</form>
	</div>
</body>
</html>