package netty.book.practice.handler.server;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.book.practice.protocol.login.LoginRequestPacket;
import netty.book.practice.protocol.Packet;
import netty.book.practice.protocol.login.LoginResponsePacket;
import netty.book.practice.protocol.message.MessageRequestPacket;
import netty.book.practice.protocol.message.MessageResponsePacket;
import netty.book.practice.serialize.PacketCodeC;

import java.util.Date;

/**
 * 服务端业务处理Handler
 *
 * @author FangYuan
 * @since 2023-03-28 20:41:03
 */
public class ServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println(new Date() + ": 客户端开始登录……");

        ByteBuf requestByteBuf = (ByteBuf) msg;

        Packet packet = PacketCodeC.decode(requestByteBuf);

        if (packet instanceof LoginRequestPacket) {
            // 接收到的客户端登录请求
            LoginRequestPacket loginRequestPacket = (LoginRequestPacket) packet;

            // 默认登录成功
            LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
            loginResponsePacket.setSuccess(true);
            loginResponsePacket.setReason("登录成功");
            System.out.println(new Date() + ": 登录成功!");

            // 登录请求响应
            ByteBuf byteBuf = PacketCodeC.encode(loginResponsePacket);
            ctx.channel().writeAndFlush(byteBuf);
        } else if (packet instanceof MessageRequestPacket) {
            MessageRequestPacket messageRequestPacket = (MessageRequestPacket) packet;
            System.out.println(new Date() + ": 收到客户端消息: " + messageRequestPacket.getMessage());

            // 回消息
            MessageResponsePacket messageResponsePacket = new MessageResponsePacket();
            messageResponsePacket.setMessage("服务端回复【" + messageRequestPacket.getMessage() + "】");
            // 编码并发送
            ByteBuf byteBuf = PacketCodeC.encode(messageResponsePacket);
            ctx.channel().writeAndFlush(byteBuf);
        }

    }
}
