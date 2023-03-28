package netty.book.practice.handler.client;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.book.practice.protocol.Packet;
import netty.book.practice.protocol.login.LoginRequestPacket;
import netty.book.practice.protocol.login.LoginResponsePacket;
import netty.book.practice.protocol.message.MessageResponsePacket;
import netty.book.practice.serialize.PacketCodeC;
import netty.book.practice.util.LoginUtil;

import java.util.Date;

/**
 * 客户端业务处理Handler
 *
 * @author FangYuan
 * @since 2023-03-28 20:19:38
 */
public class ClientHandler extends ChannelInboundHandlerAdapter {

    /**
     * 链接上服务器时调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        System.out.println(new Date() + ": 客户端开始登录");

        // 登录请求对象
        LoginRequestPacket loginRequestPacket = initialLoginRequest();
        // 编码
        ByteBuf byteBuf = PacketCodeC.encode(loginRequestPacket);

        // 写数据
        ctx.channel().writeAndFlush(byteBuf);
    }

    /**
     * 初始化登录请求对象
     */
    private LoginRequestPacket initialLoginRequest() {
        LoginRequestPacket loginRequestPacket = new LoginRequestPacket();
        loginRequestPacket.setUserName("wxb");
        loginRequestPacket.setPassword("lyh");

        return loginRequestPacket;
    }

    /**
     * 读取服务器的回复
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ByteBuf byteBuf = (ByteBuf) msg;

        Packet packet = PacketCodeC.decode(byteBuf);
        if (packet instanceof LoginResponsePacket) {
            LoginResponsePacket loginResponsePacket = (LoginResponsePacket) packet;

            if (loginResponsePacket.isSuccess()) {
                System.out.println(new Date() + ": 客户端登录成功");
                // 标记登录成功
                LoginUtil.markAsLogin(ctx.channel());
            } else {
                System.out.println(new Date() + ": 客户端登录失败，原因：" + loginResponsePacket.getReason());
            }
        } else if (packet instanceof MessageResponsePacket) {
            MessageResponsePacket messageResponsePacket = (MessageResponsePacket) packet;
            System.out.println(new Date() + ": 收到服务端的消息: " + messageResponsePacket.getMessage());
        }
    }
}
