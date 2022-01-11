package org.ih.task;

import org.ih.common.logging.Logger;

import java.util.concurrent.BlockingQueue;

/**
 * Producer that reads the database for tasks to be placed on the queue.
 * Keeps track of the different types of tasks to know when to add one to the queue
 * <p>
 * TODO : not needed if not keep track of futures. addTask() can be added to task runner since it instantiates this
 * TODO : with all the queue information
 *
 * @author Hector Plahar
 */
public class TaskProducer implements Runnable {

    private boolean shutdown;
    //    private final List<Future<ITask>> futures;
    private final BlockingQueue<Task> singleTaskQueue;
    private final BlockingQueue<Task> taskQueue;

    public TaskProducer(BlockingQueue<Task> singleTaskQueue, BlockingQueue<Task> taskQueue) {
        this.singleTaskQueue = singleTaskQueue;
        this.taskQueue = taskQueue;
//        this.futures = new ArrayList<>();
    }

    @Override
    public void run() {
        Logger.info("Running Task Monitor");

        while (!shutdown) {
//            if (futures.isEmpty()) {
            try {
                Thread.sleep(3000000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                Logger.info("Monitor thread interrupted");
                return;
            }
//                continue;
//            }

//            Logger.info("Checking status of futures: " + futures.size() + " pending");
//            Iterator<Future<ITask>> iterator = futures.iterator();
//            while (iterator.hasNext()) {
//                Future<ITask> taskFuture = iterator.next();
//
//                try {
//                    ITask task = taskFuture.get(5, TimeUnit.SECONDS);
//                    iterator.remove();
//                } catch (InterruptedException | ExecutionException | TimeoutException e) {
//                    e.printStackTrace();
//                }
//            }
        }
    }

    public void addTask(Task task) {
        Logger.info("Adding task type " + task.getType() + " with id " + task.getUniqueTaskId());
        switch (task.getType()) {
            case SINGLE:
                singleTaskQueue.add(task);
                break;

            default:
            case REGULAR:
                taskQueue.add(task);
                break;
        }
    }

    public void shutDown() {
        shutdown = true;
    }
}
