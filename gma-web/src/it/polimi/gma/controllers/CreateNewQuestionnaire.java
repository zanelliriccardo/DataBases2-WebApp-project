package it.polimi.gma.controllers;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.gma.exceptions.DateNotAvailableException;
import it.polimi.gma.exceptions.QuestionnaireCreationException;
import it.polimi.gma.services.QuestionnaireCreationService;
import it.polimi.gma.singletons.QuestionnairesNumber;

/**
 * Servlet implementation class CreateNewQuestionnaire
 */
@WebServlet("/CreateNewQuestionnaire")
public class CreateNewQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@EJB(name="it.polimi.gma.singletons.QuestionnaireNumber")
	QuestionnairesNumber qn;	
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CreateNewQuestionnaire() {
        super();
        // TODO Auto-generated constructor stub
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	
    	//checking that the questionnaire creation ejb has been instantiated
    	QuestionnaireCreationService qcs = (QuestionnaireCreationService)request.getSession(false).getAttribute("questCreationService");
    	if(qcs == null) {
    		response.sendRedirect(request.getContextPath() + "/productsCatalog");
    		return;
    	}
    	
    	//checking for the presence of the date
    	String stringDate = request.getParameter("questionnaireDate");
    	if(stringDate == null) {
    		request.setAttribute("errorMessage", "Please insert a valid date");
    		request.getRequestDispatcher("/jsp/addQuestionnairePage.jsp").forward(request, response);
    		return;
    	}
    	
    	//adding the inserted questions to the questionnaire
    	int i = 0;
    	String newQuestion;
    	List<String> questions = qcs.getQuestions();
    	while((newQuestion = request.getParameter("question"+i)) != null) {
    		if(newQuestion.length() > 0 && !isDuplicated(questions,newQuestion)) {
    			questions.add(newQuestion);
    		}
    		i++;
    	}
    	
    	Date questDate = null;
		try {
			
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			questDate = sdf.parse(stringDate);
		
	    	qcs.setDate(questDate);
	    	qcs.setQuestions(questions);
	    	
	    	//checking that the date is not earlier than today
	    	if(sdf.format(questDate).compareTo(sdf.format( new Date())) < 0) {
	    		request.setAttribute("errorMessage", "Please insert a valid date");
	    		request.getRequestDispatcher("/jsp/addQuestionnairePage.jsp").forward(request, response);
	    		return;
	    	}
	    	
	    	//checking that the list of questions is not empty
	    	if(qcs.getQuestions().size() <= 0) {
	    		request.setAttribute("errorMessage", "Insert at least a question please");
	    		request.getRequestDispatcher("/jsp/addQuestionnairePage.jsp").forward(request, response);
	    		return;
	    	}
  
    		qcs.createQuestionnaire();
    		qn.incrementPlannedQuestionnaires();
    		qcs.remove();
    		response.sendRedirect(request.getContextPath() + "/adminHome");
    		
    	}catch(DateNotAvailableException ex) {
    		request.setAttribute("errorMessage", "The selected date is not available");
    		request.getRequestDispatcher("/jsp/addQuestionnairePage.jsp").forward(request, response);
    		return;
    	}catch(ParseException pe){
    		request.setAttribute("errorMessage", "Please select a valid date");
    		request.getRequestDispatcher("/jsp/addQuestionnairePage.jsp").forward(request, response);
    		return;
    	}catch(QuestionnaireCreationException e) {    
    		request.setAttribute("errorMessage", "Something whent wrong during the creation of the questionnaire :(");
    		request.getRequestDispatcher("/jsp/adminHome.jsp").forward(request, response);
    		return;
    	}
    	
    }
    
    
    
    private boolean isDuplicated(List<String> questions, String newQuestion) {
    	if(questions.size() > 0) {
    		for(String q: questions) {
    			if(q.equals(newQuestion)) {
    				return true;
    			}
    		}
    	}
    	return false;
    }
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		this.processRequest(request, response);
	}

}
