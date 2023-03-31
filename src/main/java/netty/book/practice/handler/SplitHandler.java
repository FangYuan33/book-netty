package netty.book.practice.handler;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import netty.book.practice.serialize.PacketCodeC;

/**
 * 拆包处理Handler
 *
 * @author FangYuan
 * @since 2023-03-31 20:17:22
 */
public class SplitHandler extends LengthFieldBasedFrameDecoder {

    /**
     * 拆包处理器的方法
     */
    public SplitHandler() {
        // 最大长度 数据长度偏移量 表示该数据长度的字段字节数
        super(Integer.MAX_VALUE, 7, 4);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
        if (in.getInt(in.readerIndex()) != PacketCodeC.MAGIC_NUMBER) {
            ctx.channel().close();
            return null;
        }

        return super.decode(ctx, in);
    }
}
