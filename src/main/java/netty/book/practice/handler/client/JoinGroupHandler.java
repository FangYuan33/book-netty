package netty.book.practice.handler.client;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.book.practice.protocol.response.JoinGroupResponsePacket;

/**
 * 客户端处理加入群组的响应
 *
 * @author FangYuan
 * @since 2023-04-03 20:46:01
 */
@ChannelHandler.Sharable
public class JoinGroupHandler extends SimpleChannelInboundHandler<JoinGroupResponsePacket> {

    /**
     * 单例
     */
    public static final JoinGroupHandler JOIN_GROUP_HANDLER = new JoinGroupHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupResponsePacket msg) throws Exception {
        if (msg.isSuccess()) {
            System.out.println(msg.getMessage());
        } else {
            System.err.println("加入群[" + msg.getGroupId() + "]失败，原因为：" + msg.getReason());
        }
    }
}
