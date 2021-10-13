package org.ih.common.exception;

/**
 * Checked exception thrown by utility classes as needed
 *
 * @author Hector Plahar
 */
public class UtilityException extends Exception {

    public UtilityException(Exception e) {
        super(e);
    }
}
