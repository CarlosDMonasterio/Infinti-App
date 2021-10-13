package org.ih.dao;

import java.io.Serial;

/**
 * RuntimeException thrown on issue trying to access data
 *
 * @author Hector Plahar
 */
public class DataAccessException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public DataAccessException() {
        super();
    }

    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }

    public DataAccessException(String message) {
        super(message);
    }

    public DataAccessException(Throwable cause) {
        super(cause);
    }
}
