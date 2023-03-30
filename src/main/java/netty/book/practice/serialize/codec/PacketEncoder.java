package netty.book.practice.serialize.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import netty.book.practice.protocol.Packet;
import netty.book.practice.serialize.PacketCodeC;

/**
 * 将消息编码成ByteBuf
 *
 * @author FangYuan
 * @since 2023-03-30 20:27:44
 */
public class PacketEncoder extends MessageToByteEncoder<Packet> {
    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, ByteBuf out) throws Exception {
        PacketCodeC.encode(out, msg);
    }
}
