package netty.book.practice.handler.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import netty.book.practice.protocol.request.GroupMessageRequestPacket;
import netty.book.practice.protocol.response.GroupMessageResponsePacket;
import netty.book.practice.util.SessionUtil;

/**
 * 服务端处理群组消息的Handler
 *
 * @author FangYuan
 * @since 2023-04-07 20:22:40
 */
@ChannelHandler.Sharable
public class GroupMessageHandler extends SimpleChannelInboundHandler<GroupMessageRequestPacket> {

    /**
     * 单例
     */
    public static final GroupMessageHandler GROUP_MESSAGE_HANDLER = new GroupMessageHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupMessageRequestPacket msg) throws Exception {
        // 初始化响应对象
        GroupMessageResponsePacket responsePacket = new GroupMessageResponsePacket();
        responsePacket.setMessage(msg.getMessage());
        responsePacket.setFromUser(SessionUtil.getUserName(ctx.channel()));
        responsePacket.setFromGroupId(msg.getGroupId());

        // 获取群组
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(msg.getGroupId());

        // 回写消息
        channelGroup.writeAndFlush(responsePacket);
    }
}
