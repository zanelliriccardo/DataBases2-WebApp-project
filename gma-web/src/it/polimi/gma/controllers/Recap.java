package it.polimi.gma.controllers;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.gma.entities.Answer;
import it.polimi.gma.entities.Consumer;
import it.polimi.gma.entities.Questionnaire;
import it.polimi.gma.enums.AuthType;
import it.polimi.gma.services.RecapService;
import it.polimi.gma.webutils.AuthenticationChecker;

/**
 * Servlet implementation class Inspection
 */
@WebServlet("/recap")
public class Recap extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(name = "it.polimi.gma.services.RecapService")
    RecapService recapService;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Recap() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		AuthType auth = AuthenticationChecker.checkAuth(request.getSession());
    	if (!auth.equals(AuthType.ADMIN)) {
    		response.sendRedirect(request.getContextPath() + "/login"); 
    		return;
    	}
		if(request.getParameter("date")==null) {
			request.getRequestDispatcher("/jsp/recap.jsp").forward(request, response);
			return;
		}
		
		try {
			//get questionnaire 
			Questionnaire q=recapService.getInformation(request.getParameter("date"));
			
			//get answers from questionnaire and convert them in type List 
			List<Answer> answers=(q.getAnswers().stream()).collect(Collectors.toList());
			
			//get users who have cancelled the questionnaire from questionnaire and convert they in type List
			Map<Consumer, Timestamp> canc = q.getCancellations();
			List<String> cancUsers=new ArrayList<String>();
			
			//convert set of cancellations into a list of the users who made them
			canc.entrySet().forEach(c->cancUsers.add(c.getKey().getUsername()));
			
			//get users who have submitted the questionnaire from questionnaire and convert them in type List(distinct removes duplicate) 
			List<String> subUsers=answers.stream().map(answer->((Answer) answer).getUser().getUsername()).distinct().collect(Collectors.toList());
			
			request.setAttribute("submittingUser", subUsers);			
			request.setAttribute("cancellingUser", cancUsers);
			request.setAttribute("answers", answers);			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			request.setAttribute("dataError", true);
		} 
		request.getRequestDispatcher("/jsp/recap.jsp").forward(request, response);		
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
