package netty.book.practice.serialize.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import netty.book.practice.serialize.PacketCodeC;

import java.util.List;

/**
 * 将消息解码成Packet对象
 *
 * @author FangYuan
 * @since 2023-03-30 20:30:05
 */
public class PacketDecoder extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) {
        out.add(PacketCodeC.decode(in));
    }
}
