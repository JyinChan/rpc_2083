package begin.server;

import begin.server.handler.RpcChannelInitializer;
import begin.service.ServiceScan;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class RpcServer implements Runnable {

    private final static Logger logger = LoggerFactory.getLogger(RpcServer.class);

    private String host;

    private int port;

    public RpcServer(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public void run() {

        EventLoopGroup parent = new NioEventLoopGroup(1);
        EventLoopGroup child = new NioEventLoopGroup(1);

        ServerBootstrap b = new ServerBootstrap();
        b.group(parent, child)
                .option(ChannelOption.SO_BACKLOG, 1024)
                .childOption(ChannelOption.SO_REUSEADDR, true) //重用地址
                .childOption(ChannelOption.SO_RCVBUF, 65536)
                .childOption(ChannelOption.SO_SNDBUF, 65536)
                .childOption(ChannelOption.TCP_NODELAY, true)
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                .childOption(ChannelOption.ALLOCATOR, new PooledByteBufAllocator(false))
                .channel(NioServerSocketChannel.class)
                .handler(new LoggingHandler(LogLevel.INFO))
                .childHandler(new RpcChannelInitializer());

        ChannelFuture channelFuture;
        try {
            channelFuture = b.bind(host, port).sync();
            channelFuture.channel().closeFuture().addListener(ChannelFutureListener.CLOSE);
        } catch (Exception e) {
            logger.error("", e);
        }
    }

    public static void main(String[] args) throws Exception {
        //new AnnotationConfigApplicationContext(ServiceBeanFactory.class, ServiceScan.class);
        ServiceBeanFactory.load("begin.service.imp");
        RpcServer server = new RpcServer("127.0.0.1", 8899);
        server.run();
    }
}
