package begin.client;

import begin.client.handler.RpcChannelInitializer;
import begin.message.RpcRequest;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RpcClient implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(RpcClient.class);

    private String host;
    private int port;

    private ChannelFuture channelFuture;
    private RpcFutureService futureService;

    public RpcClient(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            EventLoopGroup group = new NioEventLoopGroup(1);
            Bootstrap b = new Bootstrap();
            b.group(group)
                    .channel(NioSocketChannel.class)
                    .handler(new RpcChannelInitializer(this));
            channelFuture = b.connect(host, port)
                    .sync()
                    .channel()
                    .closeFuture()
                    .addListener(ChannelFutureListener.CLOSE);
            futureService = new RpcFutureService();
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public RpcFuture send(RpcRequest req) {
        RpcFuture future = new RpcFuture(req);
        futureService.addFuture(req.getId(), future);
        channelFuture.channel().writeAndFlush(req);
        return future;
    }

    public RpcFutureService getFutureService() {
        return futureService;
    }
}
