package cn.nukkit.scheduler;

import cn.nukkit.InterruptibleThread;

import java.util.LinkedList;

/**
 * author: MagicDroidX
 * Nukkit Project
 */
public class AsyncWorker extends Thread implements InterruptibleThread {
    private final LinkedList<AsyncTask> taskQueue = new LinkedList<>();

    public AsyncWorker() {
        this.setName("Asynchronous Worker");
    }

    public void addTask(AsyncTask task) {
        synchronized (taskQueue) {
            taskQueue.addFirst(task);
        }
    }

    public void clearTasks() {
        synchronized (taskQueue) {
            taskQueue.clear();
        }
    }

    public void removeTask(AsyncTask task) {
        synchronized (taskQueue) {
            taskQueue.remove(task);
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            processTasks();
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status
            }
        }
    }

    private void processTasks() {
        synchronized (taskQueue) {
            for (AsyncTask task : taskQueue) {
                if (!task.isFinished()) {
                    task.run();
                }
            }
        }
    }
}
