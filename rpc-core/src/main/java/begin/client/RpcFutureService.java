package begin.client;

import java.util.concurrent.ConcurrentHashMap;

public class RpcFutureService {

    private ConcurrentHashMap<Integer, RpcFuture> rpcFutureTable = new ConcurrentHashMap<>();

    public void addFuture(Integer id, RpcFuture future) {
        rpcFutureTable.put(id, future);
    }

    public RpcFuture getFuture(Integer id) {
        return rpcFutureTable.get(id);
    }
}
