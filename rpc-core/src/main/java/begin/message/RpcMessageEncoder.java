package begin.message;

import begin.serialize.IRpcSerialize;
import begin.serialize.ProtostuffSerializeI;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.ObjectOutputStream;

public class RpcMessageEncoder extends MessageToByteEncoder {

    private Class<?> c;
    private IRpcSerialize serializer;

    public RpcMessageEncoder(Class<?> c) {
        this.c = c;
        serializer = new ProtostuffSerializeI();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        if (c.isInstance(msg)) {
            byte[] data = serializer.serialize(msg);
            out.writeInt(data.length);
            out.writeBytes(data);
        }
    }
}
