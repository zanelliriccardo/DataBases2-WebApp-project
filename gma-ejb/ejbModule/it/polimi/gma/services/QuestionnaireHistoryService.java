package it.polimi.gma.services;

import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;

import it.polimi.gma.entities.Questionnaire;

@Stateless
public class QuestionnaireHistoryService {
	
	@PersistenceContext(name = "gma-ejb")
	private EntityManager em;
	
	public List<Questionnaire> getPage(int page, int pageSize){
		
		Date actualDate = new Date();
		List<Questionnaire> results = null;
		
		//retrieving from the database the questionnaires requested + 1 to check whethere more questionnaires are available or not
		results = em.createNamedQuery("Questionnaire.returnOrderedPastQuestionnaires",Questionnaire.class).setParameter("dateValue", actualDate).setFirstResult((page-1) * pageSize).setMaxResults(pageSize).getResultList();

		return results;
	}
	
	public List<Questionnaire> getPlannedPage(int page, int pageSize){
		Date actualDate = new Date();
		List<Questionnaire> results = null;
		
		//retrieving from the database the questionnaires requested + 1 to check whethere more questionnaires are available or not
		results = em.createNamedQuery("Questionnaire.returnOrderedPlannedQuestionnaires",Questionnaire.class).setParameter("dateValue", actualDate).setFirstResult((page-1) * pageSize).setMaxResults(pageSize).getResultList();
	
		return results;
	}
	
	public void removeQuestionnaire(Date questionnaireDate) {
		
		//delete the questionnaire
		Questionnaire q = em.find(Questionnaire.class, questionnaireDate);
		if(q != null) {
			em.remove(q);
			em.flush();
		}
		
		
	}
	
}
