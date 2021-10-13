package org.ih.task;

/**
 * Status for tasks that are run by the {@link TaskRunner}
 *
 * @author Hector Plahar
 */
public enum TaskStatus {

    // submitted and received by the system. Implies guaranteed execution to start or failure
    SUBMITTED,

    // task execution has began
    IN_PROGRESS,

    // task execution failed to complete successfully due to some exception. If there was an error it was saved
    FAILED,

    // task execution completed successfully
    COMPLETED
}
