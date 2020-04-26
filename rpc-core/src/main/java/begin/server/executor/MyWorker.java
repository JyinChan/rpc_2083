package begin.server.executor;

import java.util.concurrent.BlockingQueue;

public class MyWorker extends Thread {

    private BlockingQueue<MyTask> taskQueue;

    protected MyWorker(BlockingQueue<MyTask> taskQueue) {
        this.taskQueue = taskQueue;
    }

    @Override
    public void run() {
        for ( ; ;) {
            //MyTask task = taskQueue.take();

        }
    }
}
