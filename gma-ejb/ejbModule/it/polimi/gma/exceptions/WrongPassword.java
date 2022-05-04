package it.polimi.gma.exceptions;

public class WrongPassword extends Exception {
	private static final long serialVersionUID = 1L;
	
	public WrongPassword(String message) {
		super(message);
	}

	public WrongPassword() {
		super();
	}
}

