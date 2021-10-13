package org.ih.account.authentication;

/**
 * Interface for authentication
 *
 * @author Hector Plahar
 */
public interface Authentication {

    /**
     * Authenticates a user's name and password against the system's stored credentials
     *
     * @param email    user email
     * @param password user password
     * @return true on successful authentication with the credentials provided, false otherwise
     * @throws AuthenticationException on exception authenticating
     */
    boolean authenticates(String email, String password) throws AuthenticationException;
}
