package begin.client;

import java.util.concurrent.ConcurrentHashMap;

public class RpcClientManager {

    private final static ConcurrentHashMap<Integer, RpcClient> clientTable = new ConcurrentHashMap<>();

    public static void addRpcClient(Integer serverId, RpcClient client) {
        clientTable.put(serverId, client);
    }

    public static RpcClient getRpcClient(Integer serverId) {
        return clientTable.get(serverId);
    }
}
