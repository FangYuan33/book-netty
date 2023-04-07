package netty.book.practice.handler.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.book.practice.protocol.response.GroupMessageResponsePacket;

/**
 * 客户端处理群组消息响应的Handler
 *
 * @author FangYuan
 * @since 2023-04-07 20:31:40
 */
public class GroupMessageHandler extends SimpleChannelInboundHandler<GroupMessageResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageResponsePacket msg) throws Exception {
        System.out.println("收到群[" + msg.getFromGroupId() + "]中[" + msg.getFromUser() + "]发来的消息：" + msg.getMessage());
    }
}
