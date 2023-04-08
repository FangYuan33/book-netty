package netty.book.practice.handler.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.book.practice.protocol.Packet;
import netty.book.practice.protocol.command.Command;

import java.util.HashMap;
import java.util.Map;

import static netty.book.practice.handler.server.CreateGroupHandler.CREATE_GROUP_HANDLER;
import static netty.book.practice.handler.server.GroupMessageHandler.GROUP_MESSAGE_HANDLER;
import static netty.book.practice.handler.server.JoinGroupHandler.JOIN_GROUP_HANDLER;
import static netty.book.practice.handler.server.ListGroupMembersHandler.LIST_GROUP_MEMBERS_HANDLER;
import static netty.book.practice.handler.server.MessageHandler.MESSAGE_HANDLER;
import static netty.book.practice.handler.server.QuitGroupHandler.QUIT_GROUP_HANDLER;

/**
 * 服务端handler管理器
 *
 * @author FangYuan
 * @since 2023-04-08 11:25:24
 */
@ChannelHandler.Sharable
public class ServerHandler extends SimpleChannelInboundHandler<Packet> {

    /**
     * 单例
     */
    public static final ServerHandler SERVER_HANDLER = new ServerHandler();

    /**
     * 策略模式封装Handler，这样就能在回调 ServerHandler 的 channelRead0 方法时
     * 找到具体的Handler，而不需要经过责任链的每个 Handler 节点，以此来提高性能
     */
    private final Map<Command, SimpleChannelInboundHandler<? extends Packet>> map;

    public ServerHandler() {
        map = new HashMap<>();

        map.put(Command.MESSAGE_REQUEST, MESSAGE_HANDLER);
        map.put(Command.CREATE_GROUP_REQUEST, CREATE_GROUP_HANDLER);
        map.put(Command.JOIN_GROUP_REQUEST, JOIN_GROUP_HANDLER);
        map.put(Command.QUIT_GROUP_REQUEST, QUIT_GROUP_HANDLER);
        map.put(Command.LIST_MEMBERS_REQUEST, LIST_GROUP_MEMBERS_HANDLER);
        map.put(Command.SEND_GROUP_MESSAGE_REQUEST, GROUP_MESSAGE_HANDLER);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
        map.get(msg.getCommand()).channelRead(ctx, msg);
    }
}
