<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<%@page import="java.util.Set" %>
<%@page import="it.polimi.gma.entities.Product" %>   
<%@page import="java.util.Base64"%>
<%@page import="it.polimi.gma.enums.AuthType"%>
<%@page import="it.polimi.gma.webutils.AuthenticationChecker" %> 
<% 
	AuthType auth = AuthenticationChecker.checkAuth(session);
	if (!auth.equals(AuthType.ADMIN)) {
		response.sendRedirect(request.getContextPath() + "/login"); 
	}
%> 
<%

Set<Product> products = (Set<Product>)request.getAttribute("products");

%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>gma: ProductsCatalog</title>

<style>
	.catalog{
		max-width: 80%;
		margin-left:10%;
		margin-bottom: 50px;
	}
  	.productCard{
		width: 300px;
		height: 300px;
		float: left;
		text-align: center;
	}
  	
  	.prodImg{
  		margin-left:30px;
  		margin-right:30px;
  		margin-top:30px;
		margin-bottom:30px;
		width: 240px;
		height: 160px;
  	}
  	
  	.productName{
  		font-size: 25px;
  	}
  	
  	.clearfix:after {
		content: ".";
    	visibility: hidden;
    	display: block;
    	height: 0;
    	clear: both;
  	}
  	
  	#newProdButton{
	  margin-left: 10%;
	  border-style: solid;
	  border-width: 2px;
	  text-align: center;
	  width: 300px;
	  padding-top: 20px;
	  padding-bottom: 20px;
	}
  	
</style>

</head>
<body>

<h1>Products Catalog</h1>
<div style="text-align: right;"><a href="/gma-web/adminHome">Home</a></div>
	<div class="catalog clearfix">
		<%
		if(products != null && !products.isEmpty()){
			for(Product p: products){
				%>
				<div class="productCard">
					<% 
					byte[] image = p.getProductImage();
					if (image != null) {
					%>
						<img src="data:image/jpg;base64,<%= Base64.getMimeEncoder().encodeToString(image) %>" class="prodImg">
					<%
					}
					else {
					%>
						<img src="${pageContext.request.contextPath}/images/image_not_found.png" class="prodImg"/> 
					<%
					}
					%>
					<span class="productName"><%= p.getName() %></span>
					<br>
					<a href="/gma-web/newQuestionnaireForm?productId=<%= p.getIdProduct() %>">+ addQuestionnaire</a>
				</div>
				<% 
			}
		}else{%>
			No product available
		<% 
		}
		%>
	</div>
	<a href="/gma-web/jsp/addProductPage.jsp">
		<div id="newProdButton">
			add new product
		</div>
	</a>
</body>
</html>