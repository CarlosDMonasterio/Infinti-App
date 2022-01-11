package org.ih.task.consumer;

import org.ih.common.logging.Logger;
import org.ih.dao.hibernate.HibernateConfiguration;
import org.ih.task.Task;
import org.ih.task.TaskType;

import java.util.concurrent.BlockingQueue;

/**
 * Consumes tasks that are intended to be run serially. Achieves this by retrieving
 * the tasks from a single blocking queue, and then running them itself (as opposed to having the
 * main task service run them in a separate thread
 *
 * @author Hector Plahar
 */
public class SingleTaskConsumer extends AbstractTaskConsumer {

    public SingleTaskConsumer(BlockingQueue<Task> queue) {
        super(queue, TaskType.SINGLE);
    }

    @Override
    public void run() {
        while (!shutdown) {
            try {
                Task task = getQueue().take();
                if (task.getType() != supportedType) {
                    Logger.error("Cannot run task of type " + task.getType());
                    continue;
                }
                Logger.info("Running " + task.getType() + " task " + task.getUniqueTaskId());
                HibernateConfiguration.beginTransaction();
                task.getExecutor().execute(task);
                Logger.info(task.getType().toString() + " task completed.");
            } catch (InterruptedException e) {
                if (shutdown)
                    return;
                Logger.error(e);
            } catch (Exception e) {
                Logger.error("Task Exception", e);
            } finally {
                HibernateConfiguration.commitTransaction();
            }
        }
    }
}
