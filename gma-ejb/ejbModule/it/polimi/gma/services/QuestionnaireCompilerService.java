package it.polimi.gma.services;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.commons.lang3.StringUtils;

import it.polimi.gma.entities.Answer;
import it.polimi.gma.entities.Consumer;
import it.polimi.gma.entities.OffensiveWord;
import it.polimi.gma.entities.Questionnaire;
import it.polimi.gma.exceptions.OffensiveWordException;

@Stateless
public class QuestionnaireCompilerService {
	@PersistenceContext(name = "gma-ejb")
	EntityManager em;
	
	public Questionnaire findTodaysQuestionnaire() {
		Questionnaire q = em.find(Questionnaire.class, new Date(System.currentTimeMillis()));
		em.refresh(q);
		return q;
	}

	public void persistAnswers(Set<Answer> answers) throws OffensiveWordException {
		List<OffensiveWord> ofw = em.createNamedQuery("OffensiveWord.findAll", OffensiveWord.class).getResultList();
		for(Answer a : answers) {
			for(OffensiveWord elem : ofw) {
				if(StringUtils.containsIgnoreCase(a.getValue(), elem.getWord()))
					throw new OffensiveWordException();
			}
			em.persist(a);
			em.merge(a.getQuestion());
		}
		em.flush();
	}
	
	public void banUser(String username) {
		Consumer c = em.find(Consumer.class, username);
		c.setBanned(true);
		em.flush();
	}
	
	// check if the consumer c could submit the questionaire q or if it has already been submitted by c
	public boolean couldSubmit(Consumer c) {
		Date date = Date.valueOf(LocalDate.now());
		List<Consumer> c_list = em.createNamedQuery("Answer.getDistinctUserByData", Consumer.class).setParameter("dateValue", date).getResultList();
		for (Consumer elem : c_list) {
			if(elem.getUsername() == c.getUsername()) return false;
		}
		return true;
	}
	
	public void cancelQuestionnaire(Consumer c, LocalDateTime d, Questionnaire q) throws Exception {
		if(d == null) throw new Exception("date null");
		Map<Consumer, Timestamp> cancellations = q.getCancellations();
		if(cancellations == null) {
			q.setCancellations(new HashMap<Consumer, Timestamp>());
		}
		q.getCancellations().put(c, Timestamp.valueOf(d));
		em.merge(q);
	}
}
