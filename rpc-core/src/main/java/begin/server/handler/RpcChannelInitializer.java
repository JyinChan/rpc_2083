package begin.server.handler;

import begin.message.RpcMessageDecoder;
import begin.message.RpcMessageEncoder;
import begin.message.RpcRequest;
import begin.message.RpcResponse;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

public class RpcChannelInitializer extends ChannelInitializer<NioSocketChannel> {

    @Override
    protected void initChannel(NioSocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("encoder", new RpcMessageEncoder(RpcResponse.class));
        pipeline.addLast("decoder", new RpcMessageDecoder(RpcRequest.class));
        pipeline.addLast("idle", new IdleStateHandler(0, 0, 0));
        pipeline.addLast("logger", new LoggingHandler(LogLevel.DEBUG));
        pipeline.addLast("handler", new RpcMessageHandler());

    }
}
