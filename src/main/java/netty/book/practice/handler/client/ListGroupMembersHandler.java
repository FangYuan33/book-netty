package netty.book.practice.handler.client;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.book.practice.protocol.response.ListGroupMembersResponsePacket;

/**
 * 客户端处理查询群组对象的响应
 *
 * @author FangYuan
 * @since 2023-04-06 20:34:20
 */
@ChannelHandler.Sharable
public class ListGroupMembersHandler extends SimpleChannelInboundHandler<ListGroupMembersResponsePacket> {

    /**
     * 单例
     */
    public static final ListGroupMembersHandler LIST_GROUP_MEMBERS_HANDLER = new ListGroupMembersHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ListGroupMembersResponsePacket msg) throws Exception {
        System.out.println("群[" + msg.getGroupId() + "]中的人包括：" + msg.getMembers());
    }
}
