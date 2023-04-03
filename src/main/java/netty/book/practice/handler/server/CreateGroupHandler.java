package netty.book.practice.handler.server;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.DefaultChannelGroup;
import netty.book.practice.protocol.request.CreateGroupRequestPacket;
import netty.book.practice.protocol.response.CreateGroupResponsePacket;
import netty.book.practice.util.SessionUtil;

import java.util.List;
import java.util.UUID;

/**
 * 创建群组服务端处理Handler
 *
 * @author FangYuan
 * @since 2023-04-03 20:03:32
 */
public class CreateGroupHandler extends SimpleChannelInboundHandler<CreateGroupRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupRequestPacket msg) {
        List<String> userNameList = msg.getUserNameList();

        // 创建ChannelGroup 获取所有用户的channel 用来之后发送群聊创建完成的通知
        DefaultChannelGroup channelGroup = new DefaultChannelGroup(ctx.executor());
        for (String userName : userNameList) {
            Channel specificChannel = SessionUtil.getChannel(userName);
            if (specificChannel != null) {
                channelGroup.add(specificChannel);}
        }

        // 创建群组结果对象
        CreateGroupResponsePacket responsePacket = new CreateGroupResponsePacket();
        responsePacket.setSuccess(true);
        responsePacket.setGroupId(UUID.randomUUID().toString().split("-")[0]);
        responsePacket.setUserNameList(userNameList);

        // 保存群组Id 和 channelGroup 的关系
        SessionUtil.bindChannelGroup(responsePacket.getGroupId(), channelGroup);

        // 群发消息
        channelGroup.writeAndFlush(responsePacket);

        System.out.print("群创建成功，id 为[" + responsePacket.getGroupId() + "], ");
        System.out.println("群里面有：" + responsePacket.getUserNameList());
    }
}
