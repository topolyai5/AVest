package com.topolyai.avest;

public class FailedToInitializationException extends RuntimeException {


    public FailedToInitializationException(Exception e) {
        super(e);
    }
}
