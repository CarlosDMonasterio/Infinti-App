package org.ih.dto;

/**
 * @author Hector Plahar
 */
public class Statistics implements DataObject {

    private long inProgressCount;
    private long successCount;
    private long failureCount;
    private String label;

    public Statistics(long inProgressCount, long successCount, long failureCount, String label) {
        this.inProgressCount = inProgressCount;
        this.successCount = successCount;
        this.failureCount = failureCount;
        this.label = label;
    }

    public long getInProgressCount() {
        return inProgressCount;
    }

    public long getSuccessCount() {
        return successCount;
    }

    public long getFailureCount() {
        return failureCount;
    }

    public String getLabel() {
        return label;
    }
}
