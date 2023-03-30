package netty.book.practice.handler.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.book.practice.protocol.request.LoginRequestPacket;
import netty.book.practice.protocol.response.LoginResponsePacket;

import java.util.Date;

/**
 * 服务端处理登录请求的Handler
 *
 * @author FangYuan
 * @since 2023-03-30 20:33:06
 */
public class LoginHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket msg) throws Exception {
        // 默认登录成功
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setSuccess(true);
        loginResponsePacket.setReason("登录成功");
        System.out.println(new Date() + ": 登录成功!");
    }
}
