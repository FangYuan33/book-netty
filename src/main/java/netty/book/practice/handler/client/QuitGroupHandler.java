package netty.book.practice.handler.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.book.practice.protocol.response.QuitGroupResponsePacket;

/**
 * 客户端处理退出群组响应
 *
 * @author FangYuan
 * @since 2023-04-06 20:05:43
 */
public class QuitGroupHandler extends SimpleChannelInboundHandler<QuitGroupResponsePacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, QuitGroupResponsePacket msg) throws Exception {
        if (msg.isSuccess()) {
            System.out.println("退出群聊[" + msg.getGroupId() + "]成功！");
        } else {
            System.out.println("退出群聊[" + msg.getGroupId() + "]失败！");
        }
    }
}
