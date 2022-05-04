package it.polimi.gma.exceptions;

public class ConsumerAlreadyRegisteredException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public ConsumerAlreadyRegisteredException(String message) {
		super(message);
	}

	public ConsumerAlreadyRegisteredException() {
		super();
	}
}
