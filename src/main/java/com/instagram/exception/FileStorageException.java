package com.instagram.exception;

public class FileStorageException extends RuntimeException {
    

	private static final long serialVersionUID = -3661819762727984130L;

	public FileStorageException() {
        super();
    }
    
    public FileStorageException(String message) {
        super(message);
    }

     public FileStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
