package org.ih.dto;

/**
 * Represents a school district
 *
 * @author Hector Plahar
 */
public class District implements DataObject {

    private long id;
    private String label;
    private String description;
    private long schoolCount;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public long getSchoolCount() {
        return schoolCount;
    }

    public void setSchoolCount(long schoolCount) {
        this.schoolCount = schoolCount;
    }
}
