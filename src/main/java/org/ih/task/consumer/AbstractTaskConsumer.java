package org.ih.task.consumer;

import org.ih.common.logging.Logger;
import org.ih.task.ITask;
import org.ih.task.ITaskExecutor;
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

    private final BlockingQueue<ITask> queue;
    protected final TaskType supportedType;
    protected boolean shutdown;
    protected ExecutorService threadPool;
    protected final HashMap<String, ITaskExecutor> executingTasks;

    public AbstractTaskConsumer(BlockingQueue<ITask> queue, TaskType supportedType) {
        this.queue = queue;
        this.supportedType = supportedType;
        this.executingTasks = new HashMap<>();
    }

    BlockingQueue<ITask> getQueue() {
        return this.queue;
    }

    public void shutdown() {
        this.shutdown = true;
    }

    public void setThreadPool(ExecutorService threadPool) {
        this.threadPool = threadPool;
    }

    public boolean cancelTask(String uniqueName) {
        ITaskExecutor executor = executingTasks.get(uniqueName);
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
