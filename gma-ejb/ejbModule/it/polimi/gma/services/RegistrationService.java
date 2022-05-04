package it.polimi.gma.services;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.codec.digest.DigestUtils;

import it.polimi.gma.entities.Consumer;
import it.polimi.gma.exceptions.ConsumerAlreadyRegisteredException;

@Stateless
public class RegistrationService {
	
	@PersistenceContext(name = "gma-ejb")
	EntityManager em;
	
	public void registerConsumer(Consumer c) throws ConsumerAlreadyRegisteredException {
		// check if the consumer is already registered
		if(em.find(Consumer.class, c.getUsername()) != null) {
			throw new ConsumerAlreadyRegisteredException("User \""+c.getUsername()+"\" already exists.");
		}
		
		// hash the user's password
		c.setPassword(DigestUtils.sha256Hex(c.getPassword())); 
		
		// register the consumer by persisting it
		em.persist(c);
	}
}
