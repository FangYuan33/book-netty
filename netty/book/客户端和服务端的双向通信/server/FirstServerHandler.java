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
     * 读取从客户端发送过来的信息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;

        System.out.println(new Date() + ": 服务端读到数据 -> " + byteBuf.toString(StandardCharsets.UTF_8));
    }
}
