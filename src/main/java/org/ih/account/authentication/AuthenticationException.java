package org.ih.account.authentication;

/**
 * Exception class for the Authentication
 *
 * @author Hector Plahar
 */
public class AuthenticationException extends Exception {

    public AuthenticationException(String message) {
        super(message);
    }

    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
