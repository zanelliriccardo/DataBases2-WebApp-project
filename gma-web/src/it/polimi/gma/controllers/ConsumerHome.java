package it.polimi.gma.controllers;

import java.io.IOException;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.gma.entities.Consumer;
import it.polimi.gma.entities.Product;
import it.polimi.gma.entities.Review;
import it.polimi.gma.enums.AuthType;
import it.polimi.gma.services.ConsumerHomeService;
import it.polimi.gma.services.QuestionnaireCompilerService;
import it.polimi.gma.webutils.AuthenticationChecker;

@WebServlet
public class ConsumerHome extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@EJB(name = "it.polimi.gma.services.ConsumerHomeService")
	private ConsumerHomeService chs;
	
	@EJB(name = "it.polimi.gma.services.QuestionnaireCompilerService")
	private QuestionnaireCompilerService qcs;

	protected void processRequest(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		AuthType auth = AuthenticationChecker.checkAuth(request.getSession());
		if (!auth.equals(AuthType.CONSUMER)) {
			response.sendRedirect(request.getContextPath() + "/login");
			return;
		}
		Product productOfTheDay = chs.getProductOfTheDay();
		Review review;
		if (productOfTheDay == null)
			review = null;
		else
			review = chs.getOneReview(productOfTheDay.getIdProduct());
		request.setAttribute("product", productOfTheDay);
		request.setAttribute("review", review);
		request.getRequestDispatcher("/jsp/consumerHome.jsp").forward(request, response);
		return;

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
