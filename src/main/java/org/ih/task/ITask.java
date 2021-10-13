package org.ih.task;

/**
 * Interface for tasks that are meant to be executed by
 * the task runner
 *
 * @author Hector Plahar
 */
public abstract class ITask<T extends ITaskExecutor> {

    public abstract T getExecutor();

    public abstract TaskType getType();

    /**
     * @return a unique identifier this task for the task.
     * Recommend that implementations of this class add additional information such as
     * the canonical class name plus some additional parameters to the task id, to be able to uniquely identify
     * this task instance
     */
    public abstract String getUniqueTaskId();

    /**
     * Unique identifier for a specific task.
     * Same tasks for the same entity should have the same task name. This differs
     * from unique task ids, in which case they will have a different task id
     *
     * @return unique task name
     */
    public abstract String getTaskName();
}
