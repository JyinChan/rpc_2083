package begin.message;

import begin.serialize.IRpcSerialize;
import begin.serialize.ProtostuffSerializeI;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class RpcMessageDecoder extends ByteToMessageDecoder {

    private Class<?> c;
    private IRpcSerialize serializer;

    public RpcMessageDecoder(Class<?> c) {
        this.c = c;
        this.serializer = new ProtostuffSerializeI();
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        while(in.readableBytes() > 4) {
            int origIndex = in.readerIndex();
            int l = in.readInt();
            if (in.readableBytes() >= l) {
                byte[] data = new byte[l];
                in.readBytes(data);
                Object o = serializer.deserialize(data, c);
                out.add(o);
            } else {
                in.readerIndex(origIndex);
            }
        }
    }
}
