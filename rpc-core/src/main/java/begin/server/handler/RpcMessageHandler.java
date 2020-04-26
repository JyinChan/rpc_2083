package begin.server.handler;

import begin.exception.NoSuchServiceException;
import begin.exception.RpcInvokingException;
import begin.message.RpcRequest;
import begin.message.RpcResponse;
import begin.server.ServiceBeanFactory;
import begin.util.Log;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.lang.reflect.Method;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class RpcMessageHandler extends SimpleChannelInboundHandler<RpcRequest> {

    static ForkJoinPool pool = new ForkJoinPool(1);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcRequest msg) throws Exception {

        pool.submit(() -> {
            String className = msg.getClassName();
            String methodName = msg.getMethodName();

            Log.getLogger().debug("rpc request [{}.{}]", className, methodName);

            Object service = ServiceBeanFactory.getService(className);
            Object result;
            if (service == null) {
                Log.getLogger().warn("can not found service by [{}]", className);
                result = new NoSuchServiceException();
            }
            else {
                try {
                    Method serviceMethod = service.getClass().getDeclaredMethod(methodName, msg.getParamsType());
                    result = serviceMethod.invoke(service, msg.getParams());
                } catch (Exception e) {
                    Log.getLogger().error("", e);
                    result = new RpcInvokingException();
                }
            }

            RpcResponse response = new RpcResponse();
            response.setId(msg.getId());
            response.setResult(result);

            ctx.writeAndFlush(response);
        });
    }
}
