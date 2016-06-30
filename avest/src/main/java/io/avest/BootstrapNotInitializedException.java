package io.avest;

public class BootstrapNotInitializedException extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public BootstrapNotInitializedException() {
        super("Could not initalized the Context, please load first an Activity");
    }

}
