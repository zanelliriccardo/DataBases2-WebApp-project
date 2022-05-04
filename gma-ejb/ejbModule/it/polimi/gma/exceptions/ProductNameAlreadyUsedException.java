package it.polimi.gma.exceptions;

public class ProductNameAlreadyUsedException extends Exception {

	private static final long serialVersionUID = 1L;
	
	public ProductNameAlreadyUsedException() {
		super();
	}
	
	public ProductNameAlreadyUsedException(String message) {
		super(message);
	}
}
