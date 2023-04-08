package netty.book.practice.serialize.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;
import netty.book.practice.protocol.Packet;
import netty.book.practice.serialize.PacketCodeC;

import java.util.List;

/**
 * 编码解码器合并Handler
 *
 * @author FangYuan
 * @since 2023-04-08 10:52:08
 */
@ChannelHandler.Sharable
public class PacketCodecHandler extends MessageToMessageCodec<ByteBuf, Packet> {

    /**
     * 单例
     */
    public static final PacketCodecHandler PACKET_CODEC_HANDLER = new PacketCodecHandler();

    @Override
    protected void encode(ChannelHandlerContext ctx, Packet msg, List<Object> out) throws Exception {
        ByteBuf byteBuf = ctx.alloc().ioBuffer();
        PacketCodeC.encode(byteBuf, msg);

        out.add(byteBuf);
    }

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf msg, List<Object> out) {
        out.add(PacketCodeC.decode(msg));
    }
}
