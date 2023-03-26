package 客户端和服务端的双向通信.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 服务端与客户端之间的处理器
 *
 * @author FangYuan
 * @since 2023-03-26 14:42:54
 */
public class FirstServerHandler extends ChannelInboundHandlerAdapter {

    /**
     * 收到客户端数据后会回调该方法
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.println(new Date() + ": 服务端读到数据 -> " + byteBuf.toString(StandardCharsets.UTF_8));

        // 回复客户端数据
        System.out.println(new Date() + ": 服务端写出数据");
        ByteBuf out = getByteBuf(ctx);
        ctx.channel().writeAndFlush(out);
    }

    private ByteBuf getByteBuf(ChannelHandlerContext ctx) {
        ByteBuf buffer = ctx.alloc().buffer();

        byte[] bytes = "你好哇".getBytes(StandardCharsets.UTF_8);

        buffer.writeBytes(bytes);

        return buffer;
    }
}
