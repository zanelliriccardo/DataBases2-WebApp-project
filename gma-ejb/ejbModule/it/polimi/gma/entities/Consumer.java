package it.polimi.gma.entities;

import java.sql.Date;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import it.polimi.gma.enums.Sex;

@Entity
@NamedQueries({
	@NamedQuery(name="Consumer.getListOrderByPoints",
	query="SELECT c "
			+ "FROM Consumer c "
			+ "ORDER BY c.points DESC")
})
@Table(name = "CONSUMER", schema = "gmadb")
public class Consumer extends User {
	private static final long serialVersionUID = 1L;
	

	// ====================
	// Attributes
	// ====================
	
	@Enumerated(EnumType.ORDINAL)
	private Sex sex;
	
	@Column(nullable = true)
	private Date birthdate;
	
	@Column(nullable = false)
	private boolean banned;
	
	@Column(nullable = false)
	private int points;
	
	// ====================
	// Relations
	// ====================
	
	@OneToMany(mappedBy = "author") // Lasciamo di default perchè il percorso per le review viene fatto dal prodotto(?)
	private Set<Review> reviews;
	
	@OneToMany(mappedBy = "consumer") // Lasciamo di default perchè quando prendi la classifica non ti servono tutte le answers del db
	private Set<Answer> answers;
	
	// ====================
	// Constructors
	// ====================
	
	public Consumer(){
		
	}
	
	public Consumer(String username, String password, String email) {
		this.username = username;
		this.password = password;
		this.email = email;
		this.banned = false;
		this.points = 0;
		this.reviews = new HashSet<Review>();
		this.answers = new HashSet<Answer>();
	}

	// ====================
	// Getters & Setters
	// ====================

	public Sex getSex() {
		return sex;
	}

	public void setSex(Sex sex) {
		this.sex = sex;
	}
	
	public Integer getAge() {
		if(this.birthdate != null) {
			return LocalDate.now().getYear() - birthdate.toLocalDate().getYear();
		}
		else {
			return null;
		}
	}

	public Date getBirthdate() {
		return birthdate;
	}

	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	
	public void setBirthdateFromString(String date) {
		if(date == null) 
			return;
		else 
			this.birthdate = Date.valueOf(date);
	}

	public boolean isBanned() {
		return banned;
	}

	public void setBanned(boolean banned) {
		this.banned = banned;
	}

	public int getPoints() {
		return points;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public Set<Review> getReviews() {
		return reviews;
	}

	public void setReviews(Set<Review> reviews) {
		this.reviews = reviews;
	}

	public Set<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(Set<Answer> answers) {
		this.answers = answers;
	}
	
	// ====================
	// Hashcode & Equals
	// ====================
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (banned ? 1231 : 1237);
		result = prime * result + ((birthdate == null) ? 0 : birthdate.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((password == null) ? 0 : password.hashCode());
		result = prime * result + points;
		result = prime * result + ((sex == null) ? 0 : sex.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
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
		Consumer other = (Consumer) obj;
		if (banned != other.banned)
			return false;
		if (birthdate == null) {
			if (other.birthdate != null)
				return false;
		} else if (!birthdate.equals(other.birthdate))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (points != other.points)
			return false;
		if (sex != other.sex)
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
}
