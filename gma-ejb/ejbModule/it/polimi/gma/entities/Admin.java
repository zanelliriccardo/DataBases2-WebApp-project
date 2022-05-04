package it.polimi.gma.entities;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ADMIN", schema = "gmadb")
public class Admin extends User {
	private static final long serialVersionUID = 1L;
}
