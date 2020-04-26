package begin.client.handler;

import begin.client.RpcClient;
import begin.client.RpcFuture;
import begin.message.RpcResponse;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

public class RpcMessageHandler extends SimpleChannelInboundHandler<RpcResponse> {

    private RpcClient client;

    public RpcMessageHandler(RpcClient client) {
        this.client = client;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, RpcResponse msg) throws Exception {
        RpcFuture future = client.getFutureService().getFuture(msg.getId());
        future.done(msg);
    }
}
