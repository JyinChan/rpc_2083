package begin.client;

import begin.service.DataService;
import begin.service.HelloService;
import begin.service.TaskService;
import begin.util.Log;

public class RpcClientStarter {

    public static void main(String[] args) {
        RpcClient client = new RpcClient("127.0.0.1", 8899);
        RpcClientManager.addRpcClient(0, client);
        client.run();

        HelloService helloService = ServiceProxy.createProxy(HelloService.class);
        String h = helloService.hello("jyin");
        Log.getLogger().debug("rpc result: " + h);
        //h = helloService.hello("mike");
        //Log.getLogger().debug("rpc result: " + h);
        /*TaskService taskService = ServiceProxy.createProxy(TaskService.class);
        taskService.run();

        DataService dataService = ServiceProxy.createProxy(DataService.class);
        dataService.save();*/
    }
}
