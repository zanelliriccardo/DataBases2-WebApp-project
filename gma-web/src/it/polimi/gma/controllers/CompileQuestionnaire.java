package it.polimi.gma.controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.gma.entities.Questionnaire;
import it.polimi.gma.enums.AuthType;
import it.polimi.gma.services.QuestionnaireCompilerService;
import it.polimi.gma.webutils.AuthenticationChecker;

@WebServlet
public class CompileQuestionnaire extends HttpServlet{
	private static final long serialVersionUID = 1L;
	
	@EJB(name = "it.polimi.gma.services.QuestionnaireCompilerService")
	private QuestionnaireCompilerService qcs;
	
	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		AuthType auth = AuthenticationChecker.checkAuth(request.getSession());
    	if (!auth.equals(AuthType.CONSUMER)) {
    		response.sendRedirect(request.getContextPath() + "/login"); 
    		return;
    	}
		Questionnaire q = qcs.findTodaysQuestionnaire();
		if(q!=null) {
			request.getSession().setAttribute("questionnaire", q);
			request.getSession().setAttribute("questions", q.getQuestions());
			request.getRequestDispatcher("/jsp/compileQuestionnaire.jsp").forward(request, response);
		}
		else {
			response.sendRedirect(request.getContextPath() + "/consumerHome");
		}
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		this.processRequest(request, response);
	}
}
