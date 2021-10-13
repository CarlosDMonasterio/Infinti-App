package org.ih.task;

/**
 * Represents a task that can only have a single instance running at a time
 *
 * @author Hector Plahar
 */
public abstract class SingleTask<T extends ITaskExecutor> extends ITask<T> {

    private String userId; // person who triggered the task

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public final TaskType getType() {
        return TaskType.SINGLE;
    }
}
