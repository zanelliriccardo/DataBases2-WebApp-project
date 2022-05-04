package it.polimi.gma.controllers;

import java.io.IOException;

import java.io.PrintWriter;
import java.rmi.ServerException;
import java.util.Date;
import java.util.List;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.gma.services.QuestionnaireHistoryService;
import it.polimi.gma.singletons.QuestionnairesNumber;
import it.polimi.gma.webutils.AuthenticationChecker;
import it.polimi.gma.entities.Questionnaire;
import it.polimi.gma.enums.AuthType;

/**
 * Servlet implementation class adminHome
 */
@WebServlet
public class AdminHome extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(name="it.polimi.gma.services.QuestionnaireHistoryService")
	QuestionnaireHistoryService qhs;
	@EJB(name="it.polimi.gma.singletons.QuestionnairesNumber")
	QuestionnairesNumber qnb;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AdminHome() {
        super();
        // TODO Auto-generated constructor stub
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException {
   	 	response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
        	
        	// checks on the session obj
        	AuthType auth = AuthenticationChecker.checkAuth(request.getSession());
        	if (!auth.equals(AuthType.ADMIN)) {
        		response.sendRedirect(request.getContextPath() + "/login"); 
        		return;
        	}
        	
        	//depending on the value of the string we are returning the list of past or planned questionnaires
        	String pastOrPlanned = "past";
        	String requestPastOrPlanned = (String)request.getParameter("pastOrPlanned");
        	
        	if( requestPastOrPlanned != null && 
        			(requestPastOrPlanned.equals("past") ||
        			 requestPastOrPlanned.equals("planned"))) {
        		pastOrPlanned = requestPastOrPlanned;
        	}
        	
        	//fixed page size (number of questionnaires loaded per page)
	        final int pageSize = 10;
	        
	        int page = 1;
	        int questionnairesNumber = (int)(pastOrPlanned.equals("past") ? qnb.getPastQuestionnairesNumber() : qnb.getPlannedQuestionnairesNumber());
	        int availablePagesNumber = (questionnairesNumber/pageSize)+((questionnairesNumber%pageSize==0)? 0 : 1) ;
	        
	        /*
	         * priority is given to the page sent as a parameter, if no parameter is found the last 
	         * page visited by the user is retrieved
	        */
	        if(request.getParameter("page") != null) {
	        	page = Integer.parseInt(request.getParameter("page"));
        	} else if(request.getSession(false).getAttribute(pastOrPlanned.equals("past") ? "pageHistory":"pagePlanned") != null){
	        	page = (int)(pastOrPlanned.equals("past") ? request.getSession(false).getAttribute("pageHistory") : request.getSession(false).getAttribute("pagePlanned"));
	        }
	        if(page > availablePagesNumber) { page = availablePagesNumber; }
	        if(page < 1) { page=1; }
	        
	        //retrieving the list of past or planned questionnaires depending on the value obtained from the request
	        List<Questionnaire> questionnaires = pastOrPlanned.equals("past") ? qhs.getPage(page, pageSize) : qhs.getPlannedPage(page, pageSize);
	        
	        //saving the last page loaded by the user in its session
	        request.getSession(false).setAttribute(pastOrPlanned.equals("past") ? "pageHistory" : "pagePlanned" , page);
	        
	        //setting request attributes for jsp paging
	        request.setAttribute("pastOrPlanned", pastOrPlanned.equals("past") ? "past" : "planned");
	        request.setAttribute("questionnairesHistory", questionnaires);
	        request.setAttribute("availablePageNumber", availablePagesNumber);
	        
	        //forwarding to adminHome jsp
        	request.getRequestDispatcher("/jsp/adminHome.jsp").forward(request,response);
          
        } catch (Exception e) {
        	e.printStackTrace(out);
        	throw new ServerException("Dispacther Servlet Error",e);
          
        } finally {      
        	out.close();
        }
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
