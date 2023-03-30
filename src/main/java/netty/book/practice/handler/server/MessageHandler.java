package netty.book.practice.handler.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.book.practice.protocol.request.MessageRequestPacket;
import netty.book.practice.protocol.response.MessageResponsePacket;

import java.util.Date;

/**
 * 服务端处理Message Handler
 *
 * @author FangYuan
 * @since 2023-03-30 20:34:37
 */
public class MessageHandler extends SimpleChannelInboundHandler<MessageRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageRequestPacket msg) {
        System.out.println(new Date() + ": 收到客户端消息: " + msg.getMessage());

        // 回消息
        MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
        messageResponsePacket.setMessage("服务端回复【" + msg.getMessage() + "】");
        ctx.channel().writeAndFlush(messageResponsePacket);
    }
}
