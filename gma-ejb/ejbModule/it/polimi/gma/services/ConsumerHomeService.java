package it.polimi.gma.services;

import java.sql.Date;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import it.polimi.gma.entities.Product;
import it.polimi.gma.entities.Questionnaire;
import it.polimi.gma.entities.Review;
import it.polimi.gma.entities.Consumer;

@Stateless
public class ConsumerHomeService {
	@PersistenceContext(name = "gma-ejb")
	EntityManager em;
	
	public Product getProductOfTheDay() {
		Questionnaire q = em.find(Questionnaire.class, new Date(System.currentTimeMillis()));
		try {
			em.refresh(q);
			return q.getProduct();
		} catch (Exception e) { 
			return null;
		}
	}
	
	public Review getOneReview(long productId) {
		try {
			return (Review) em.createNamedQuery("findReviewForProduct", Review.class).setParameter("productId", productId).setMaxResults(1).getResultList().get(0);
		} catch (Exception e) { // if there are problems retrieving the review, do not print anything
			return null;
		}
	}
	
	public Consumer getConsumer(String username) {
		return em.find(Consumer.class, username);
	}
}
