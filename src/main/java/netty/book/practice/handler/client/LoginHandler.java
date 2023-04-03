package netty.book.practice.handler.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.book.practice.protocol.response.LoginResponsePacket;
import netty.book.practice.session.Session;
import netty.book.practice.util.SessionUtil;

import java.util.Date;

/**
 * 登录业务处理Handler
 *
 * @author FangYuan
 * @since 2023-03-30 20:19:55
 */
public class LoginHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    /**
     * 处理登录response对象
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket msg) {
        if (msg.isSuccess()) {
            System.out.println(new Date() + ": 客户端登录成功");
            // 标记登录成功
            SessionUtil.bindSession(new Session(msg.getUserId(), msg.getUserName()), ctx.channel());
        } else {
            System.out.println(new Date() + ": 客户端登录失败，原因：" + msg.getReason());
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("客户端连接被关闭");
    }
}
