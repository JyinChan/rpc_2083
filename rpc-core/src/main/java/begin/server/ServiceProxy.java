package begin.server;

import begin.service.HelloService;
import begin.service.imp.HelloServiceImpl;
import begin.util.Log;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

class InvokeTask extends RecursiveTask<Object> {

    private Object target;
    private Method method;
    private Object[] args;

    InvokeTask(Object target, Method method, Object[] args) {
        this.target = target;
        this.method = method;
        this.args = args;
    }

    @Override
    protected Object compute() {
        try {
            Log.getLogger().debug("invoke: {}.{}", target.getClass().getName(), method.getName());
            return method.invoke(target, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

public class ServiceProxy implements InvocationHandler {

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T target) {
        return (T) Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), new ServiceProxy(target));
    }

    private Object target;
    private ServiceProxy(Object target) {
        this.target = target;
    }

    ForkJoinPool pool = new ForkJoinPool(4);

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("toString")) {
            return method.invoke(target, args);
        }
        InvokeTask invokeTask = new InvokeTask(target, method, args);
        //ForkJoinPool.commonPool().submit(invokeTask);
        pool.submit(invokeTask);

        return invokeTask.join();
    }

    public static void main(String[] args) {
        HelloService helloService = new HelloServiceImpl();
        helloService = createProxy(helloService);
        System.out.println(helloService.hello("world"));
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
