package org.ih.task;

/**
 * Interface for all classes that are capable of running tasks
 *
 * @author Hector Plahar
 */
public interface ITaskExecutor<T extends ITask> {

    /**
     * Executes task using details passed in parameter
     *
     * @param task
     */
    void execute(T task);

    /**
     * Cancels the currently running task
     *
     * @return true if task successfully canceled, false otherwise
     */
    boolean cancel();
}
