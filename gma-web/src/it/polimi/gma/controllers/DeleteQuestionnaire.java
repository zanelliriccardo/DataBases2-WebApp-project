package it.polimi.gma.controllers;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.ServerException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.gma.enums.AuthType;
import it.polimi.gma.services.QuestionnaireHistoryService;
import it.polimi.gma.singletons.QuestionnairesNumber;
import it.polimi.gma.webutils.AuthenticationChecker;

/**
 * Servlet implementation class DeleteQuestionnaire
 */
@WebServlet("/deleteQuestionnaire")
public class DeleteQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	@EJB(name="it.polimi.gma.services.QuestionnaireHistoryService")
	QuestionnaireHistoryService qhs;
	@EJB(name="it.polimi.gma.singletons.QuestionnairesNumber")
	QuestionnairesNumber qnb;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteQuestionnaire() {
        super();
        // TODO Auto-generated constructor stub
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
    	
    	// checks on the session obj
    	AuthType auth = AuthenticationChecker.checkAuth(request.getSession());
    	if (!auth.equals(AuthType.ADMIN)) {
    		response.sendRedirect(request.getContextPath() + "/login"); 
    		return;
    	}
    	
    	Date deleteDate;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			deleteDate = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy").parse(request.getParameter("questionnaireDeleteDate"));
		    if(deleteDate != null && sdf.format(deleteDate).compareTo(sdf.format(new Date())) < 0) {
		   		qhs.removeQuestionnaire(deleteDate);
		   		qnb.decrementPastQuestionnaires();
		   	}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    	
    	response.sendRedirect(request.getContextPath() + "/adminHome");
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
