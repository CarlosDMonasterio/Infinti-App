package org.ih.common.exception;

/**
 * @author Hector Plahar
 */
public class ServiceException extends RuntimeException {

    public ServiceException(Exception e) {
        super(e);
    }

    public ServiceException(String errMsg, Exception e) {
        super(errMsg, e);
    }

    public ServiceException(String errorMsg) {
        super(errorMsg);
    }
}
