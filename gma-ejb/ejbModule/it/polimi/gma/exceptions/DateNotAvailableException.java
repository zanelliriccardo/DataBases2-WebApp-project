package it.polimi.gma.exceptions;

public class DateNotAvailableException extends Exception {
	private static final long serialVersionUID = 1L;

	public DateNotAvailableException() {
		super();
	}
	
	public DateNotAvailableException(String message) {
		super(message);
	}
}
