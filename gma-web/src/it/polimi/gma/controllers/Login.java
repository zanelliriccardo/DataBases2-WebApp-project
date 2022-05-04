package it.polimi.gma.controllers;

import java.io.IOException;
import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.gma.entities.Consumer;
import it.polimi.gma.entities.User;
import it.polimi.gma.exceptions.UserNotFound;
import it.polimi.gma.exceptions.WrongPassword;
import it.polimi.gma.services.LoginService;
import it.polimi.gma.services.QuestionnaireCompilerService;
import it.polimi.gma.singletons.QuestionnairesNumber;

/**
 * Servlet implementation class Login
 */
@WebServlet
public class Login extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(name = "it.polimi.gma.services.LoginService")
    LoginService loginService;
	@EJB(name="it.polimi.gma.singletons.QuestionnairesNumber")
	QuestionnairesNumber qnb;
	@EJB(name="it.polimi.gma.services.QuestionnaireCompilerService")
	QuestionnaireCompilerService qcs;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Login() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
		return;
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	  String username = request.getParameter("username"),
			  pwd = request.getParameter("password");
	  Boolean adminLogin;
	  try {
		//  System.out.print(request.getParameter("adminLogin"));
	    adminLogin = request.getParameter("adminLogin").contains("true") ? true : false;
	  }
	  catch(Exception e) {
		  adminLogin=false;
	  }
        if (username == null || username.isEmpty() ||
                pwd == null || pwd.isEmpty()) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        
		try {
			User u = loginService.requestLogin(username, pwd, adminLogin);
			request.getSession().setAttribute("user", u);
			request.getSession().setAttribute("loginTime", LocalDateTime.now());
			if(!adminLogin) {
				request.getSession().setAttribute("canSubmit", qcs.couldSubmit((Consumer)u));
			}
		} catch (UserNotFound | WrongPassword e) {
			request.setAttribute("errorMessage", "Invalid user or password");
			request.getSession().invalidate();
			request.getRequestDispatcher("/jsp/login.jsp").forward(request, response);
			return;
		}
		
		if(!adminLogin)
			response.sendRedirect(request.getContextPath() + "/consumerHome");
		else 
			response.sendRedirect(request.getContextPath() + "/adminHome");

	}
	
}
