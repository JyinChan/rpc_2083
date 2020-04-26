package begin.server.executor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;

public class MyExecutor {

    private final static int RUNNING = 1;
//    private final static int

    private final BlockingQueue<MyTask> taskQueue;
    private final List<MyWorker> workers;

    private final int parallelism;

    private int state;
    private int working;

    public MyExecutor(int parallelism) {
        this.parallelism = parallelism;
        this.workers = new ArrayList<>(parallelism);
        this.taskQueue = new LinkedBlockingQueue<>();
    }

    public boolean submit(MyTask task) {
        synchronized (this) {
            if (state == RUNNING)
            if (working < parallelism) {
                MyWorker worker = new MyWorker(taskQueue);
                worker.start();
                workers.add(worker);
            }
            return taskQueue.add(task);
        }
    }

    public void shutdown() {

    }
}
