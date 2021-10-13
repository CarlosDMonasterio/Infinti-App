package org.ih.task.consumer;

import org.ih.common.logging.Logger;
import org.ih.dao.hibernate.HibernateConfiguration;
import org.ih.task.ITask;
import org.ih.task.TaskType;

import java.util.concurrent.BlockingQueue;

/**
 * Consumes tasks that can be run in parallel (REGULAR type)
 * <p>
 * Tasks are submitted as new Runnable()s to the main task service to enable them be run in parallel.
 * This is in contrast to the Single Task consumer which runs the tasks within it's run method,
 * in order to only have a single one running at a time
 *
 * @author Hector Plahar
 */
public class ParallelTaskConsumer extends AbstractTaskConsumer {

    public ParallelTaskConsumer(BlockingQueue<ITask> queue) {
        super(queue, TaskType.REGULAR);
    }

    @Override
    public void run() {
        while (!shutdown) {
            try {
                ITask task = getQueue().take();    // blocks
                if (task.getType() != TaskType.REGULAR) {
                    Logger.error("Cannot run task of type " + task.getType());
                    continue;
                }
                Logger.info("Running task of type " + task.getType());
                submitTask(task);
            } catch (InterruptedException e) {
                if (shutdown)
                    return;
                Logger.error(e);
            } catch (Exception e) {
                Logger.info("Task Exception");
            }
        }
    }

    protected void submitTask(final ITask task) {
        this.threadPool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    HibernateConfiguration.beginTransaction();
                    task.getExecutor().execute(task);
                } catch (Exception e) {
                    Logger.error("Error running task");
                } finally {
                    HibernateConfiguration.commitTransaction();
                }
            }
        });
    }
}
