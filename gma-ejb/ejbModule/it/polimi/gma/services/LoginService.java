package it.polimi.gma.services;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.codec.digest.DigestUtils;

import it.polimi.gma.entities.Admin;
import it.polimi.gma.entities.Consumer;
import it.polimi.gma.entities.User;
import it.polimi.gma.exceptions.UserNotFound;
import it.polimi.gma.exceptions.WrongPassword;

@Stateless
public class LoginService {
	@PersistenceContext(name = "gma-ejb")
	EntityManager em;
	
	public User requestLogin(String username, String password, Boolean adminLogin) throws UserNotFound, WrongPassword {
		return (adminLogin?requestAdminLogin(username,password):requestConsumerLogin(username,password));
	}
	
	public User requestAdminLogin(String username, String password) throws UserNotFound, WrongPassword {
		User u = em.find(Admin.class, username);
		
		if(u == null)
			throw new UserNotFound("Cannot find user: " + username);
	
		if(!u.getPassword().equals(DigestUtils.sha256Hex(password)))
			throw new WrongPassword("Wrong password for user: "+ username);
		
		return u;
	}
	
	public User requestConsumerLogin(String username, String password) throws UserNotFound, WrongPassword {
		User u = em.find(Consumer.class, username);
	
		if(u == null)
			throw new UserNotFound("Cannot find user: " + username);
	
		if(!u.getPassword().equals(DigestUtils.sha256Hex(password)))
			throw new WrongPassword("Wrong password for user: "+ username);
		
		return u;		
	}
}
