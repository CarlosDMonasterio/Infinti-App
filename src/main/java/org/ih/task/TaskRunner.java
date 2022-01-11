package org.ih.task;

import org.ih.common.logging.Logger;
import org.ih.task.consumer.ParallelTaskConsumer;
import org.ih.task.consumer.SingleTaskConsumer;

import java.util.List;
import java.util.concurrent.*;

/**
 * Task runner responsible for executing tasks in a separate
 * thread and guarantees that they are completed. Tasks that can be
 * run must implement the {@link Task} interface
 * <p>
 * It initializes a thread pool with a fixed number of threads
 * <p>
 * Maintains references to the producer and different consumers which are run as threads in the
 *
 * @author Hector Plahar
 */
public class TaskRunner {

    private static TaskRunner INSTANCE;
    private ExecutorService pool;
    private final TaskProducer monitor;
    private final int threadCount = 1000;

    // consumers
    private final SingleTaskConsumer singleTaskConsumer;
    private final ParallelTaskConsumer parallelTaskConsumer;

    /**
     * Initiates service by creating a new thread pool
     * and submitting the consumers as tasks
     */
    private TaskRunner() {

        BlockingQueue<Task> taskQueue = new LinkedBlockingQueue<>();
        BlockingQueue<Task> singleTaskQueue = new LinkedBlockingQueue<>();

        // initialize producer and consumers
        this.monitor = new TaskProducer(singleTaskQueue, taskQueue);

        this.singleTaskConsumer = new SingleTaskConsumer(singleTaskQueue);
        this.parallelTaskConsumer = new ParallelTaskConsumer(taskQueue);

        Logger.info("Initiating thread pool with " + threadCount + " threads");

        pool = Executors.newFixedThreadPool(threadCount);
        parallelTaskConsumer.setThreadPool(pool);

        pool.submit(monitor);
        pool.submit(singleTaskConsumer);
        pool.submit(parallelTaskConsumer);
    }

    public static TaskRunner getInstance() {
        synchronized (TaskRunner.class) {
            if (INSTANCE == null) {
                INSTANCE = new TaskRunner();
            }
            return INSTANCE;
        }
    }

    public ExecutorService getService() {
        return this.pool;
    }

    /**
     * shuts down the executor
     */
    public void stopService() {
        Logger.info("Shutting down executor service");
        monitor.shutDown();
        singleTaskConsumer.shutdown();
        parallelTaskConsumer.shutdown();

        pool.shutdown(); // Disable new tasks from being submitted
        try {
            // Wait a while for existing tasks to terminate
            if (!pool.awaitTermination(2, TimeUnit.SECONDS)) {
                List<Runnable> running = pool.shutdownNow(); // Cancel currently executing tasks
                Logger.info(running.size() + " tasks running at shutdown");

                // Wait a while for tasks to respond to being cancelled
                if (!pool.awaitTermination(2, TimeUnit.SECONDS))
                    Logger.info("Executor service did not terminate");
            }
        } catch (InterruptedException ie) {
            // (Re-)Cancel if current thread also interrupted
            pool.shutdownNow();
            // Preserve interrupt status
            Thread.currentThread().interrupt();
        }
        pool = null;
    }

    /**
     * Adds a task to the executor service to be run on the thread pool
     *
     * @param iTask task to be run. Contains information about the executor for running the task
     */
    public void runTask(Task iTask) {
        monitor.addTask(iTask);
    }

    public boolean cancelTask(String runId, TaskType taskType) {
        Logger.info("Attempting to cancel task " + runId);
        switch (taskType) {
            case SINGLE:
                return singleTaskConsumer.cancelTask(runId);

            default:
            case REGULAR:
                return parallelTaskConsumer.cancelTask(runId);
        }
    }
}
