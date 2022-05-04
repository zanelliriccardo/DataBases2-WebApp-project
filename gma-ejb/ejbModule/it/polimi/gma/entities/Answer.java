package it.polimi.gma.entities;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

/**
 * Entity implementation class for Entity: Answer
 *
 */

@Entity
@Table(name = "ANSWER", schema = "gmadb")
@NamedQueries({
	@NamedQuery(name="Answer.getDistinctUserByData",
	query="SELECT DISTINCT a.consumer "
			+ "FROM Answer a "
			+ "WHERE a.questionnaire.date = :dateValue"),
	@NamedQuery(name="Answer.getAnswers",
	query="SELECT a "
			+ "FROM Answer a "
			+ "WHERE a.questionnaire.date = :dateValue")
})
public class Answer implements Serializable{
	private static final long serialVersionUID = 1L;
	
	// ====================
	// Attributes
	// ====================
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Column(name = "VALUE", nullable = false)
	private String value;
	
	// ====================
	// Relations
	// ====================
	
	@ManyToOne // sembrerebbe niente cascade (se modifichi una answer non devi cambiare l'utente, idem per rimozione, update e merge), fetch default
	@JoinColumn(name = "USER_ID")
	private Consumer consumer;
	
	@ManyToOne(fetch = FetchType.LAZY) // no cascade perchè non vuoi modificare (o cancellare, aggiungere, ecc...) le domande se modifichi una risposta 
		// Perchè quando prendi i Consumer dal Questionnaire passi per Answer e non ti interessano le Question
		// Nel caso in cui dobbiamo vedere le risposte che ha dato un Consumer, ci servono le domande e facciamo la join con Question nella namedquery
	@JoinColumn(name = "QUESTION_ID")
	private Question question;
	
	@ManyToOne // Il fetch eager se non mettiamo la ManyToMany tra Consumer e Questionnaire
		// il cascade è default perchè <come sopra>
	@JoinColumn(name = "QUESTIONNAIRE_ID")
	private Questionnaire questionnaire;
	
	// ====================
	// Constructors
	// ====================
	
	public Answer(){
		
	}

	public Answer(long id, String value, Consumer user) {
		super();
		this.id = id;
		this.value = value;
		this.consumer = user;
	}

	// ====================
	// Getters & Setters
	// ====================
	
	public long getId() { return id; }
	public void setId(long id) { this.id = id; }

	public String getValue() { return value; }
	public void setValue(String value) { this.value = value; }

	public Consumer getUser() { return consumer; }
	public void setUser(Consumer user) { this.consumer = user; }
	
	public Question getQuestion() { return question; }
	public void setQuestion(Question question) { this.question = question; }

	public Questionnaire getQuestionnaire() { return questionnaire; }
	public void setQuestionnaire(Questionnaire questionnaire) { this.questionnaire = questionnaire; }

	// ====================
	// Hashcode & Equals
	// ====================
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((question == null) ? 0 : question.hashCode());
		result = prime * result + ((questionnaire == null) ? 0 : questionnaire.hashCode());
		result = prime * result + ((consumer == null) ? 0 : consumer.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Answer other = (Answer) obj;
		if (id != other.id)
			return false;
		if (question == null) {
			if (other.question != null)
				return false;
		} else if (!question.equals(other.question))
			return false;
		if (questionnaire == null) {
			if (other.questionnaire != null)
				return false;
		} else if (!questionnaire.equals(other.questionnaire))
			return false;
		if (consumer == null) {
			if (other.consumer != null)
				return false;
		} else if (!consumer.equals(other.consumer))
			return false;
		if (value == null) {
			if (other.value != null)
				return false;
		} else if (!value.equals(other.value))
			return false;
		return true;
	}	
		
}
