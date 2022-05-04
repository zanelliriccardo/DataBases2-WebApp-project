package it.polimi.gma.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import it.polimi.gma.entities.Product;
import it.polimi.gma.exceptions.ProductNameAlreadyUsedException;

@Stateless
public class CatalogService {
	
	@PersistenceContext(name="gma-ejb")
	EntityManager em;
	
	public Set<Product> getAllProducts() {	
		Set<Product> products = null;
		products = new HashSet<Product>( em.createNamedQuery("Product.findAll", Product.class).getResultList());
		return products;
	}
	
	public void saveNewProduct(String productName, byte[] productImg) throws ProductNameAlreadyUsedException {
		
		List<Product> product = null;
		product = em.createNamedQuery("Product.findByName",Product.class).setParameter("nameValue", productName).getResultList();
		
		if(!product.isEmpty()) {
			throw new ProductNameAlreadyUsedException();
		}
		
		Product newProduct = new Product();
		newProduct.setName(productName);
		newProduct.setProductImage(productImg);
		
		em.persist(newProduct);
	}
}
