package netty.book.practice.handler.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.book.practice.protocol.response.MessageResponsePacket;

import java.util.Date;

/**
 * 消息处理Handler
 *
 * @author FangYuan
 * @since 2023-03-30 20:20:44
 */
public class MessageHandler extends SimpleChannelInboundHandler<MessageResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, MessageResponsePacket msg) {
        if (msg.isSuccess()) {
            System.out.println(msg.getFromUserId() + ":" + msg.getFromUserName() + " -> " + msg.getMessage());
        } else {
            System.out.println(new Date() + ": 收到服务端的消息: " + msg.getMessage());
        }
    }
}
