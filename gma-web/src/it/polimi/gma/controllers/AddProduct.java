package it.polimi.gma.controllers;

import java.io.IOException;
import java.io.InputStream;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import it.polimi.gma.enums.AuthType;
import it.polimi.gma.exceptions.ProductNameAlreadyUsedException;
import it.polimi.gma.services.CatalogService;
import it.polimi.gma.utils.ImageUtils;
import it.polimi.gma.webutils.AuthenticationChecker;
import net.sf.jmimemagic.MagicMatch;

/**
 * Servlet implementation class AddProduct
 */
@WebServlet
@MultipartConfig
public class AddProduct extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	@EJB(name="it.polimi.gma.services.CatalogService")
	CatalogService cat;
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddProduct() {
        super();
        // TODO Auto-generated constructor stub
    }

    private void processRequest(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	
    	// checks on the session obj
    	AuthType auth = AuthenticationChecker.checkAuth(request.getSession());
    	if (!auth.equals(AuthType.ADMIN)) {
    		response.sendRedirect(request.getContextPath() + "/login"); 
    		return;
    	}

    	
    	//retrieving the name 
    	String productName = request.getParameter("productName");
    	// checks on the inserted product name
    	if(productName == null || productName.isEmpty()) {
    		request.setAttribute("errorMessage", "please insert a valid name for the product");
    		request.getRequestDispatcher("/jsp/addProductPage.jsp").forward(request,response);
    		return;
    	}
    	
    	//retrieving submitted file
    	Part productImg = request.getPart("productImage");    	
    	//checks if the file was correctly loaded
    	if(productImg == null || productImg.getSize() == 0) {
    		request.setAttribute("errorMessage", "please insert a valid image");
    		request.getRequestDispatcher("/jsp/addProductPage.jsp").forward(request,response);
    		return;
    	}
  
    	//converting the received file in a stream of bytes
    	InputStream imgContent = productImg.getInputStream();
    	byte[] imgByteArray = ImageUtils.readImage(imgContent);
    	
    	
    	try {
	    	//check on the inserted file (only jpg and png image are accepted)
	    	if(!ImageUtils.isJPEG(imgByteArray) && !ImageUtils.isPNG(imgByteArray)) {
	    		request.setAttribute("errorMessage", "only jpeg or png file supported");
	    		request.getRequestDispatcher("/jsp/addProductPage.jsp").forward(request,response);
	    		return;
	    	}
    	}catch(Exception  e) {
    		/*
    		 * the method getMagicMatch if passed some type of files (tar.gz, zip, ...) have problems hence 
    		 * this try catch was added to cope with such an issue
	    	*/
    		request.setAttribute("errorMessage", "only jpeg or png file supported");
			request.getRequestDispatcher("/jsp/addProductPage.jsp").forward(request,response);
			e.printStackTrace();
    	}
    	
    	try {
    		/*
    		 * saving the new product on the db, if a product with the same name is already present 
    		 * then an exception is raised
    		 * 
    		 * */
    		cat.saveNewProduct(productName, imgByteArray);
			response.sendRedirect(request.getContextPath() + "/productsCatalog");
		} catch (ProductNameAlreadyUsedException e1) {
			request.setAttribute("errorMessage", "the inserted name is already associate to another product!");
			request.getRequestDispatcher("/jsp/addProductPage.jsp").forward(request,response);
			e1.printStackTrace();
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
