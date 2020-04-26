package begin.client;

import begin.exception.RpcException;
import begin.message.RpcRequest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class ServiceProxy implements InvocationHandler  {

    @SuppressWarnings("unchecked")
    public static <T> T createProxy(Class<T> interfaceClass) {
        return (T) Proxy.newProxyInstance(interfaceClass.getClassLoader(), new Class[] {interfaceClass}, new ServiceProxy());
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        RpcRequest request = new RpcRequest();
        request.setId(MessageIdentifier.create());
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParamsType(method.getParameterTypes());
        request.setParams(args);

        RpcClient client = RpcClientManager.getRpcClient(0);
        RpcFuture future = client.send(request);

        Object result = future.get();
        if (result instanceof RpcException) {
            throw (RpcException) result;
        }
        return result;
    }
}
