package com.instagram.exception;

public class FileNotFoundException extends RuntimeException {

	private static final long serialVersionUID = -3669367335482111459L;

	public FileNotFoundException() {
        super();
    }
    
    public FileNotFoundException(String message) {
        super(message);
    }

     public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
