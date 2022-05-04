package it.polimi.gma.entities;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.*;

/**
 * Entity implementation class for Entity: Question
 *
 */

@Entity
@Table(name="QUESTION", schema = "gmadb")

@NamedQuery(name="Question.findQuestion",
			query="SELECT q "
				+ "FROM Question q "
				+ "WHERE q.text = :questionValue")

public class Question implements Serializable {
	private static final long serialVersionUID = 1L;

	// ====================
	// Attributes
	// ====================
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	
	@Column(nullable = false)
	private String text;
	
	@Column(nullable = false)
	private int points;
	
	// ====================
	// Relations
	// ====================
	
	@OneToMany(mappedBy = "question") // default (LAZY) perchè altrimenti ogni volta che un consumer apre il questionario vengono caricate tutte le risposte per ogni domanda del questionario 
	private Set<Answer> answers;
	
	@ManyToMany(mappedBy = "questions") // default (LAZY) perchè ogni volta che un consumer apre un questionario vengono caricati tutti gli altri questionari contenenti le stesse questions
	private Set<Questionnaire> questionnaires;	
	
	// ====================
	// Constructors
	// ====================
	
	public Question() {
		super();
	}
	
	public Question(String questionText, int points){
        this.text= questionText;
        this.points = points;
    }
	
	// ====================
	// Getters & Setters
	// ====================
	
	public int getIdQuestion() {	return id;	}
	public void setIdQuestion(int idQuestion) {	this.id = idQuestion;	}

	public String getText() {	return text;	}
	public void setText(String text) {	this.text = text;	}
	
	
    public int getPoints() {return points;   }
    public void setPoints(int points) {	this.points = points;  }   
    
    public Set<Answer> getAnswers() { return answers; }
    public void setAnswers(Set<Answer> answers) { this.answers = answers; }
    
    // ====================
 	// Hashcode & Equals
 	// ====================
    
    @Override
    public int hashCode() {
    	final int prime = 31;
        int result = id;
        result = prime * result + (text != null ? text.hashCode() : 0);
        result = prime * result + points;
        return result;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Question question = (Question) o;

        if (id != question.id) return false;
        if (points != question.points) return false;
        if (text != null ? !text.equals(question.text) : question.text != null)
            return false;

        return true;
    }
    
    @Override
    public String toString() {
        return this.id + " " + this.text;
    }
    
}
