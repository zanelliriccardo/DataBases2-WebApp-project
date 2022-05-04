<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    
 <% 
 String errorMessage = (String)request.getAttribute("errorMessage");
 %>   
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Gma: Add Product</title>
</head>
<body>
	<h1>Insert a new product</h1>
	<div style="text-align: right;"><a href="/gma-web/adminHome">Home</a></div>
	<form id="addProductForm" method="post" action="/gma-web/addProduct" enctype="multipart/form-data">
		<div>
			<label>product name: </label>
			<input type="text" name="productName" required />
		</div>
		<div>
			<label>Product's image: </label>
			<input type="file" name="productImage" accept="image/jpeg,image/png" required/>
		</div>
		<p style= "color: red"><%= (errorMessage == null) ? "" : errorMessage %></p>
		<input type="submit" value="add product">
	</form>
</body>
</html>