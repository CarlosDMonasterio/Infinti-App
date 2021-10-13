package org.ih.common.exception;

/**
 * @author Hector Plahar
 */
public class TaskExecutionException extends Exception {

    public TaskExecutionException(String errMessage) {
        super(errMessage);
    }

    public TaskExecutionException(Exception ce) {
        super(ce);
    }
}
