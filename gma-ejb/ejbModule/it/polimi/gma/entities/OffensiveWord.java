package it.polimi.gma.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@NamedQuery(name="OffensiveWord.findAll",
query="SELECT o FROM OffensiveWord o")
@Table(name = "OFFENSIVE_WORD", schema = "gmadb")
public class OffensiveWord {
	@Id
	@Column(name = "WORD", nullable = false)
	private String word;

	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}
}
