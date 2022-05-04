package it.polimi.gma.singletons;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Singleton
public class QuestionnairesNumber {
	
	@PersistenceContext(name = "gma-ejb")
	private EntityManager em;
	
	private final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	private long pastQuestionnairesNumber;
	private long plannedQuestionnairesNumber;
	private Date actualDate;
	
	@PostConstruct
	private void init() {
		actualDate = new Date();
		this.updateCounters();
	}

	public long getPastQuestionnairesNumber() {
		this.checkDateCorrectness();
		return pastQuestionnairesNumber;
	}

	public long getPlannedQuestionnairesNumber() {
		this.checkDateCorrectness();
		return plannedQuestionnairesNumber;
	}
	
	private void updateCounters() {
		pastQuestionnairesNumber = em.createNamedQuery("Questionnaire.countPastQuestionnaires", Long.class).setParameter("dateValue", actualDate).getSingleResult();
		plannedQuestionnairesNumber = em.createNamedQuery("Questionnaire.countPlannedQuestionnaires",Long.class).setParameter("dateValue", actualDate).getSingleResult();
	}
	
	private void checkDateCorrectness() {
		
		Date testDate = new Date();
		
		if(!sdf.format(actualDate).equals(sdf.format(testDate))) {
			actualDate = testDate;
			this.updateCounters();
		}
	}
	
	public void decrementPastQuestionnaires() {
		pastQuestionnairesNumber --;
	}
	
	public void incrementPlannedQuestionnaires() {
		plannedQuestionnairesNumber ++;
	}
}
