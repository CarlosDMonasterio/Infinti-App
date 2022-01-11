package org.ih.task.consumer;

import org.ih.common.logging.Logger;
import org.ih.task.Task;
import org.ih.task.TaskExecutor;
import org.ih.task.TaskType;

import java.util.HashMap;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;

/**
 * Parent (default task consumer) for consuming tasks
 * Each specific type of consumer has a queue that it is associated with
 * and which it draws tasks from
 *
 * @author Hector Plahar
 */
public abstract class AbstractTaskConsumer implements Runnable {

    private final BlockingQueue<Task> queue;
    protected final TaskType supportedType;
    protected boolean shutdown;
    protected ExecutorService threadPool;
    protected final HashMap<String, TaskExecutor> executingTasks;

    public AbstractTaskConsumer(BlockingQueue<Task> queue, TaskType supportedType) {
        this.queue = queue;
        this.supportedType = supportedType;
        this.executingTasks = new HashMap<>();
    }

    BlockingQueue<Task> getQueue() {
        return this.queue;
    }

    public void shutdown() {
        this.shutdown = true;
    }

    public void setThreadPool(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    public boolean cancelTask(String uniqueName) {
        TaskExecutor executor = executingTasks.get(uniqueName);
        if (executor == null) {
            Logger.warn("Cannot cancel task. Couldn't retrieve executor for task " + uniqueName);
            return false;
        }

        if (!executor.cancel()) {
            Logger.warn("Failed to cancel task " + uniqueName);
            return false;
        } else {
            Logger.info("Task " + uniqueName + " canceled");
            return true;
        }
    }
}
