package begin.client;

import begin.message.RpcRequest;
import begin.message.RpcResponse;

import java.util.concurrent.*;

public class RpcFuture implements Future<Object> {

    private RpcRequest request;
    private RpcResponse response;
    private CountDownLatch latch;

    public RpcFuture(RpcRequest request) {
        this.request = request;
        this.latch = new CountDownLatch(1);
    }

    public void done(RpcResponse response) {
        this.response = response;
        latch.countDown();
    }
    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return false;
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        try {
            return get(200, TimeUnit.SECONDS);
        } catch (TimeoutException e) {
            throw new ExecutionException(e);
        }
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        latch.await(timeout, unit);
        if (response != null) {
            return response.getResult();
        } else {
            throw new TimeoutException("Rpc timeout: method[" + request.getMethodName() + "], class[" + request.getClassName() + "].");
        }
    }
}
