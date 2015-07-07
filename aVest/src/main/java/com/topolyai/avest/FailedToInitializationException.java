package com.topolyai.avest;

/**
 * Created by geri on 7/7/2015.
 */
public class FailedToInitializationException extends RuntimeException {


    public FailedToInitializationException(Exception e) {
        super(e);
    }
}
