package netty.book.practice.handler.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import netty.book.practice.protocol.request.JoinGroupRequestPacket;
import netty.book.practice.protocol.response.JoinGroupResponsePacket;
import netty.book.practice.util.SessionUtil;

/**
 * 服务端加入群聊的Handler
 *
 * @author FangYuan
 * @since 2023-04-03 20:41:44
 */
public class JoinGroupHandler extends SimpleChannelInboundHandler<JoinGroupRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, JoinGroupRequestPacket msg) {
        // 要加入的群组ID
        String groupId = msg.getGroupId();
        // 将新用户添加进来
        ChannelGroup channelGroup = SessionUtil.getChannelGroup(groupId);
        channelGroup.add(ctx.channel());

        // 初始化响应对象
        JoinGroupResponsePacket responsePacket = new JoinGroupResponsePacket();
        responsePacket.setGroupId(groupId);
        responsePacket.setSuccess(true);
        responsePacket.setMessage(SessionUtil.getUserName(ctx.channel()) + " 加入群聊");

        channelGroup.writeAndFlush(responsePacket);
    }
}
