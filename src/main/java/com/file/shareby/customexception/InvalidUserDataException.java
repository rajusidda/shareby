package com.file.shareby.customexception;

public class InvalidUserDataException extends RuntimeException {

    private static final long serialVersionUID = 1 ;

    public InvalidUserDataException(String message) {
        super(message);
    }
}
