package it.polimi.gma.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.naming.InitialContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


import org.apache.commons.lang3.StringUtils;

import it.polimi.gma.exceptions.ProductNotFoundException;
import it.polimi.gma.services.QuestionnaireCreationService;
import it.polimi.gma.webutils.AuthenticationChecker;

/**
 * Servlet implementation class CreateQustionnaire
 */
@WebServlet
public class SetProductForNewQuestionnaire extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetProductForNewQuestionnaire() {
        super();
        // TODO Auto-generated constructor stub
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {

    	QuestionnaireCreationService qcs = (QuestionnaireCreationService)request.getSession(false).getAttribute("questionnaireCreationService");
    	
    	if( qcs == null) {
    		try {
    			InitialContext ic = new InitialContext();
    			qcs = (QuestionnaireCreationService)ic.lookup("java:/openejb/local/QuestionnaireCreationServiceLocalBean");
    		}catch(Exception e) {
    			e.printStackTrace();
    			
    		}
    	}
    	
    	//setting the product for the questionnaire to be created
    	try {    	
	    	String prodStringId;
	    	if((prodStringId = request.getParameter("productId")) != null && StringUtils.isNumeric(prodStringId)) {
	    		long productId = Long.parseLong(prodStringId);  
	    		qcs.setProduct(productId);
	    		request.getSession(false).setAttribute("questCreationService", qcs);
	    		request.getRequestDispatcher("/jsp/addQuestionnairePage.jsp").forward(request, response);
	    	}else {
	    		response.sendRedirect(request.getContextPath() + "/productsCatalog");
	    	}
    	
    	}catch(ProductNotFoundException e) {
    		response.sendRedirect(request.getContextPath() + "/productsCatalog");
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
