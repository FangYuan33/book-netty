package netty.book.practice.handler.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.book.practice.protocol.Packet;
import netty.book.practice.protocol.command.Command;

import java.util.HashMap;
import java.util.Map;

import static netty.book.practice.handler.client.CreateGroupHandler.CREATE_GROUP_HANDLER;
import static netty.book.practice.handler.client.GroupMessageHandler.GROUP_MESSAGE_HANDLER;
import static netty.book.practice.handler.client.JoinGroupHandler.JOIN_GROUP_HANDLER;
import static netty.book.practice.handler.client.ListGroupMembersHandler.LIST_GROUP_MEMBERS_HANDLER;
import static netty.book.practice.handler.client.LoginHandler.LOGIN_HANDLER;
import static netty.book.practice.handler.client.MessageHandler.MESSAGE_HANDLER;
import static netty.book.practice.handler.client.QuitGroupHandler.QUIT_GROUP_HANDLER;

/**
 * 客户端handler管理器
 *
 * @author FangYuan
 * @since 2023-04-08 11:35:08
 */
public class ClientHandler extends SimpleChannelInboundHandler<Packet> {

    /**
     * 单例
     */
    public static final ClientHandler CLIENT_HANDLER = new ClientHandler();

    /**
     * 策略模式封装Handler，这样就能在回调 ClientHandler 的 channelRead0 方法时
     * 找到具体的Handler，而不需要经过责任链的每个 Handler 节点，以此来提高性能
     */
    private final Map<Command, SimpleChannelInboundHandler<? extends Packet>> map;

    public ClientHandler() {
        map = new HashMap<>();

        map.put(Command.LOGIN_RESPONSE, LOGIN_HANDLER);
        map.put(Command.MESSAGE_RESPONSE, MESSAGE_HANDLER);
        map.put(Command.CREATE_GROUP_RESPONSE, CREATE_GROUP_HANDLER);
        map.put(Command.JOIN_GROUP_RESPONSE, JOIN_GROUP_HANDLER);
        map.put(Command.QUIT_GROUP_RESPONSE, QUIT_GROUP_HANDLER);
        map.put(Command.LIST_MEMBERS_RESPONSE, LIST_GROUP_MEMBERS_HANDLER);
        map.put(Command.SEND_GROUP_MESSAGE_RESPONSE, GROUP_MESSAGE_HANDLER);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
        SimpleChannelInboundHandler<? extends Packet> handler = map.get(msg.getCommand());
        if (handler != null) {
            handler.channelRead(ctx, msg);
        }
    }
}
