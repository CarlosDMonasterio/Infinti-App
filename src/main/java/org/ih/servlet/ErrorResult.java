package org.ih.servlet;

import org.ih.dto.DataObject;

/**
 * @author Hector Plahar
 */
public class ErrorResult implements DataObject {

    private String errorMessage;

    public ErrorResult(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
