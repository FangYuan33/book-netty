package netty.book.practice.handler.client;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.book.practice.protocol.response.CreateGroupResponsePacket;

/**
 * 客户端处理创建群组响应Handler
 *
 * @author FangYuan
 * @since 2023-04-03 20:05:08
 */
@ChannelHandler.Sharable
public class CreateGroupHandler extends SimpleChannelInboundHandler<CreateGroupResponsePacket> {

    /**
     * 单例
     */
    public static final CreateGroupHandler CREATE_GROUP_HANDLER = new CreateGroupHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, CreateGroupResponsePacket msg) {
        System.out.print("群创建成功，id 为[" + msg.getGroupId() + "], ");
        System.out.println("群里面有：" + msg.getUserNameList());
    }
}
