package org.ih.task;

/**
 * Interface for all classes that are capable of running tasks
 *
 * @author Hector Plahar
 */
public interface TaskExecutor<T extends Task<?>> {

    /**
     * Executes task using details passed in parameter
     *
     * @param task task to be run
     */
    void execute(T task);

    /**
     * Cancels the currently running task
     *
     * @return true if task successfully canceled, false otherwise
     */
    boolean cancel();
}
