package org.ih.dto;

import org.ih.task.TaskStatus;

/**
 * Serves dual role as data and DTO for long running tasks
 *
 * @author Hector Plahar
 */
public class TaskTransfer implements DataObject {

    private long id;
    private String name;
    private long submitTime;
    private long startTime;
    private long endTime;
    private TaskStatus status;
    private String executorClazz;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public long getEndTime() {
        return endTime;
    }

    public void setEndTime(long endTime) {
        this.endTime = endTime;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public long getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(long submitTime) {
        this.submitTime = submitTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getExecutorClazz() {
        return executorClazz;
    }

    public void setExecutorClazz(String executorClazz) {
        this.executorClazz = executorClazz;
    }
}
