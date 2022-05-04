package it.polimi.gma.controllers;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.gma.entities.User;
import it.polimi.gma.enums.AuthType;
import it.polimi.gma.services.LeaderboardService;
import it.polimi.gma.webutils.AuthenticationChecker;
/**
 * Servlet implementation class leaderboard
 */
@WebServlet("/leaderboard")
public class Leaderboard extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@EJB(name = "it.polimi.gma.services.LeaderboardService")
    LeaderboardService leaderboardService;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Leaderboard() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub 
		AuthType auth = AuthenticationChecker.checkAuth(request.getSession());
    	if (!auth.equals(AuthType.ADMIN) && !auth.equals(AuthType.CONSUMER)) {
    		response.sendRedirect(request.getContextPath() + "/login"); 
    		return;
    	}
		request.setAttribute("leaderboard", leaderboardService.getLeaderboard());
		request.setAttribute("fullLeaderboard", false);
		request.getRequestDispatcher("/jsp/leaderboard.jsp").forward(request, response);	
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		AuthType auth = AuthenticationChecker.checkAuth(request.getSession());
    	if (!auth.equals(AuthType.ADMIN) && !auth.equals(AuthType.CONSUMER)) {
    		response.sendRedirect(request.getContextPath() + "/login"); 
    	}
		request.setAttribute("leaderboard", leaderboardService.getFullLeaderboard());
		request.setAttribute("fullLeaderboard", true);
		request.getRequestDispatcher("/jsp/leaderboard.jsp").forward(request, response);	
	}

}
