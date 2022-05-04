package it.polimi.gma.services;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.ejb.Remove;
import javax.ejb.Stateful;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import it.polimi.gma.entities.Question;
import it.polimi.gma.entities.Product;
import it.polimi.gma.entities.Questionnaire;
import it.polimi.gma.exceptions.DateNotAvailableException;
import it.polimi.gma.exceptions.ProductNotFoundException;
import it.polimi.gma.exceptions.QuestionnaireCreationException;

@Stateful
public class QuestionnaireCreationService {
	
	@PersistenceContext(name = "gma-ejb")
	EntityManager em;
	
	Product product = null;
	List<String> questions = new ArrayList<String>();
	Date questionnaireDate;
	
	public List<String> getQuestions() {
		return questions;
	}
	
	public void setQuestions(List<String> questions) {
		this.questions = questions;
	}
	
	public void setDate(Date date){
		questionnaireDate = date;
	}
	
	public void setProduct(long productId) throws ProductNotFoundException {
		product = em.find(Product.class, productId);
		if(product == null) {
			throw new ProductNotFoundException();
		}
	}
	public Date getDate() {
		return questionnaireDate;
	}
	
	
	public void createQuestionnaire() throws DateNotAvailableException, QuestionnaireCreationException{
	
		if(em.find(Questionnaire.class,questionnaireDate) != null) {
			throw new DateNotAvailableException();
		}
		

			
		if((questionnaireDate != null) 
				&& questions.size() > 0
				&& product != null){
			
			//grouping the questions for the new questionnaire
			List<Question> newQuestionnaireQuestions = new ArrayList<Question>();
			
			for(String q: questions) {
				List <Question> resultingQuestions = em.createNamedQuery("Question.findQuestion", Question.class).setParameter("questionValue", q).getResultList();
				if(resultingQuestions.isEmpty()) {
					Question newQuestion = new Question();
					newQuestion.setText(q);
					newQuestion.setPoints(2);
					newQuestionnaireQuestions.add(newQuestion);
				}else {
					newQuestionnaireQuestions.add(resultingQuestions.get(0));
				}
			}
			
			// new questionnaire creation
			Questionnaire newQuestionnaire = new Questionnaire();
			newQuestionnaire.setQuestions(newQuestionnaireQuestions);
			newQuestionnaire.setDate(questionnaireDate);
			newQuestionnaire.setProduct(product);
			em.persist(newQuestionnaire);
		}else {
			throw new QuestionnaireCreationException();
		}
		
	}
	
	@Remove
	public void remove() {
		
	}
	
	
}
