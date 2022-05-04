package it.polimi.gma.controllers;

import java.io.IOException;
import java.sql.Date;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.gma.entities.Consumer;
import it.polimi.gma.enums.AuthType;
import it.polimi.gma.enums.Sex;
import it.polimi.gma.services.RegistrationService;
import it.polimi.gma.webutils.AuthenticationChecker;

@WebServlet
public class Registration extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB(name = "it.polimi.gma.services.RegistrationService")
	RegistrationService registrationService;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// TODO add a check for the session (the user doesn't have to be logged in!)

		RequestDispatcher view = request.getRequestDispatcher("/jsp/registration.jsp");
		view.forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO add a check for the session (the user doesn't have to be logged in!)

		AuthType auth = AuthenticationChecker.checkAuth(request.getSession());
    	if (!auth.equals(AuthType.UNAUTHENTICATED)) {
    		response.sendRedirect(request.getContextPath() + "/login"); 
    		return;
    	}
		Consumer c = new Consumer();
		// mandatory fields
		c.setUsername(request.getParameter("username"));
		Pattern re = Pattern.compile("\\S+@\\S+.\\S+");
		String email = request.getParameter("email");
		if(re.matches("\\S+@\\S+.\\S+", email))
			c.setEmail(email);
		else {
			request.setAttribute("failureReason", "Invalid email format.");
			this.doGet(request, response);
		}
		String password = request.getParameter("password");
		if(password.length()>=8) {
			c.setPassword(request.getParameter("password"));
		}
		else {
			request.setAttribute("failureReason", "Password has to be at least 8 characters long.");
			this.doGet(request, response);
		}
		
		// default fields
		c.setBanned(false);
		c.setPoints(0);

		// optional fields
		if (request.getParameter("birthdate") != null)
			try {
				c.setBirthdateFromString(request.getParameter("birthdate"));
			}
			catch (IllegalArgumentException e) {
				c.setBirthdateFromString(null);
			}
		if (request.getParameter("sex") != null)
			c.setSex(Sex.valueOf(request.getParameter("sex")));
		
		try {
			registrationService.registerConsumer(c);
		} catch (Exception e) {
			request.setAttribute("failureReason", e.getMessage());
			this.doGet(request, response);
		}

		RequestDispatcher view = request.getRequestDispatcher("/jsp/login.jsp");
		view.forward(request, response);

	}
}
