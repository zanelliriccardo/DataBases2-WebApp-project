package it.polimi.gma.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import it.polimi.gma.entities.Answer;
import it.polimi.gma.entities.Consumer;

@Stateless
public class LeaderboardService {
	@PersistenceContext(name = "gma-ejb")
	EntityManager em;
	 
    public List<Consumer> getLeaderboard() {
        // TODO Auto-generated constructor stub
    	List<Consumer> res = em.createNamedQuery("Consumer.getListOrderByPoints").setMaxResults(5).getResultList(); 
    	for(Consumer c : res) {
    		em.refresh(c);
    	}
    	return res;
    }
    
    public List<Consumer> getFullLeaderboard() {
    	// TODO Auto-generated constructor stub
    	List<Consumer> res = em.createNamedQuery("Consumer.getListOrderByPoints").setMaxResults(5).getResultList(); 
    	for(Consumer c : res) {
    		em.refresh(c);
    	}
    	return res;
    }  
}
