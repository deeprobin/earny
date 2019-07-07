package de.deeprobin.earny.exception;

public class ShorteningException extends Exception {

    public ShorteningException(String message){
        super(message);
    }

    public ShorteningException(String message, Throwable throwable) {
        super(message, throwable);
    }

    public ShorteningException(Throwable throwable) {
        super(throwable);
    }

}
