package org.ih.service.rest;

import org.ih.dto.DataObject;

/**
 * @author Hector Plahar
 */
public class DownStreamAutomationParameter implements DataObject {

    private String fieldName;
    private String value;
    private String defaultValue;
    private String description;

    public DownStreamAutomationParameter() {
        this.defaultValue = "";
        this.description = "";
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String name) {
        this.fieldName = name;
    }
}
