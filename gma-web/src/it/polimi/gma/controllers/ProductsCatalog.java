package it.polimi.gma.controllers;

import java.io.IOException;
import java.util.Set;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import it.polimi.gma.entities.Product;
import it.polimi.gma.enums.AuthType;
import it.polimi.gma.services.CatalogService;
import it.polimi.gma.webutils.AuthenticationChecker;


/**
 * Servlet implementation class productsPage
 */
@WebServlet
public class ProductsCatalog extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	@EJB(name="it.polimi.gma.services.CatalogService")
	CatalogService cat;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProductsCatalog() {
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
    	
    	RequestDispatcher view;
    	
    	Set<Product> products = cat.getAllProducts();
    	
    	request.setAttribute("products", products);
    	
    	view = request.getRequestDispatcher("/jsp/productsCatalog.jsp");
    	
        try {
			view.forward(request,response);
		} catch (ServletException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        return;
    	
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
