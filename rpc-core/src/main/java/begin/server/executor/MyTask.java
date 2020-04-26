package begin.server.executor;

public abstract class MyTask<T> {

    abstract T call();

    public T join() {
        return null;
    }

    public T get() {
        return null;
    }

    public boolean isDone() {
        return false;
    }

    protected void done(T result) {

    }
}
