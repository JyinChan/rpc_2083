package begin.client.handler;

import begin.client.RpcClient;
import begin.message.RpcMessageDecoder;
import begin.message.RpcMessageEncoder;
import begin.message.RpcRequest;
import begin.message.RpcResponse;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class RpcChannelInitializer extends ChannelInitializer<NioSocketChannel> {

    private RpcClient client;

    public RpcChannelInitializer(RpcClient client) {
        this.client = client;
    }

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("logger", new LoggingHandler(LogLevel.INFO));
        pipeline.addLast("decoder", new RpcMessageDecoder(RpcResponse.class));
        pipeline.addLast("encoder", new RpcMessageEncoder(RpcRequest.class));
        pipeline.addLast("handler", new RpcMessageHandler(client));
    }
}
