package it.polimi.gma.entities;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.persistence.*;

/**
 * Entity implementation class for Entity: Questionnaire
 *
 */

@Entity
@Table(name = "QUESTIONNAIRE", schema = "gmadb")

@NamedQueries({
	@NamedQuery(name="Questionnaire.countPlannedQuestionnaires",
	query="SELECT count(q) "
		+ "FROM Questionnaire q "
		+ "WHERE q.date > :dateValue"),
	@NamedQuery(name="Questionnaire.countPastQuestionnaires",
	query="SELECT count(q) "
		+ "FROM Questionnaire q "
		+ "WHERE q.date <= :dateValue"),
	@NamedQuery(name="Questionnaire.returnOrderedPlannedQuestionnaires", 
	query="SELECT q "
		+ "FROM Questionnaire q "
		+ "WHERE q.date > :dateValue "
		+ "ORDER BY q.date ASC"),
	@NamedQuery(name="Questionnaire.returnOrderedPastQuestionnaires", 
	query="SELECT q "
		+ "FROM Questionnaire q "
		+ "WHERE q.date <= :dateValue "
		+ "ORDER BY q.date DESC")
})


public class Questionnaire implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// ====================
	// Attributes
	// ====================
	
	@Id
	@Temporal(TemporalType.DATE)
	private Date date;
	
	// ====================
	// Relations
	// ====================
	
	@OneToMany(mappedBy="questionnaire", cascade= CascadeType.REMOVE) // default (LAZY) perchè non voglio caricarmi tutte le risposte degli altri utenti ogni volta che il consumer apre un questionario
	private Set<Answer> answers;
	
	@ManyToOne
	@JoinColumn(name = "PRODUCT_ID") // default (EAGER) perchè ogni volta che apri un questionario vuoi vedere il prodotto (e la foto?) a cui si riferisce
	private Product product;
	
	@ManyToMany(cascade =CascadeType.PERSIST) // default perchè l'unico caso in cui servono le Questions è quando il questionario viene aperto dal consumer
	@JoinTable (name = "QUESTIONNAIRE_QUESTION",
			joinColumns=@JoinColumn(name = "QUESTIONNAIRE_ID"),
			inverseJoinColumns=@JoinColumn(name = "QUESTION_ID"))
	private List<Question> questions;
	
	// ====================
	// Collections
	// ====================
	
	@ElementCollection(fetch = FetchType.LAZY) // Lazy perchè ogni volta che prendi un questionario non ti interessa sapere chi l'ha cancellato
	@CollectionTable(name = "QUESTIONNAIRE_CANNCELLATIONS", joinColumns = @JoinColumn(name = "QUESTIONNAIRE_DATE"))
	@MapKeyJoinColumn(name = "USER_ID")
	@Column(name = "CANCEL_DATETIME")
	//@Temporal(TemporalType.DATE)
	private Map<Consumer, Timestamp> cancellations;
	
	// ====================
	// Constructors
	// ====================
	
	public Questionnaire() {
		super();
	}
	

	public Questionnaire(Date date, Set<Answer> answers, Product product, List<Question> questions,
			Map<Consumer, Timestamp> cancellations) {
		super();
		this.date = date;
		this.answers = answers;
		this.product = product;
		this.questions = questions;
		this.cancellations = cancellations;
	}
	
	// ====================
	// Getters & Setters
	// ====================

	public Date getDate() { return date; }
	public void setDate(Date date) { this.date = date; }

	public Set<Answer> getAnswers() { return answers; }
	public void setAnswers(Set<Answer> answers) { this.answers = answers; }

	public Product getProduct() { return product; }
	public void setProduct(Product product) { this.product = product; }

	public List<Question> getQuestions() { return questions; }
	public void setQuestions(List<Question> questions) { this.questions = questions; }

	public Map<Consumer, Timestamp> getCancellations() { return cancellations; }
	public void setCancellations(Map<Consumer, Timestamp> cancellations) { this.cancellations = cancellations; }


	// ====================
	// Hashcode & Equals
	// ====================
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cancellations == null) ? 0 : cancellations.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((product == null) ? 0 : product.hashCode());
		result = prime * result + ((questions == null) ? 0 : questions.hashCode());
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
		Questionnaire other = (Questionnaire) obj;
		if (cancellations == null) {
			if (other.cancellations != null)
				return false;
		} else if (!cancellations.equals(other.cancellations))
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (product == null) {
			if (other.product != null)
				return false;
		} else if (!product.equals(other.product))
			return false;
		if (questions == null) {
			if (other.questions != null)
				return false;
		} else if (!questions.equals(other.questions))
			return false;
		return true;
	}
	
}
