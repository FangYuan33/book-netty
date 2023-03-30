package netty.book.practice.handler.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.book.practice.protocol.request.LoginRequestPacket;
import netty.book.practice.protocol.response.LoginResponsePacket;
import netty.book.practice.util.LoginUtil;

import java.util.Date;

/**
 * 登录业务处理Handler
 *
 * @author FangYuan
 * @since 2023-03-30 20:19:55
 */
public class LoginHandler extends SimpleChannelInboundHandler<LoginResponsePacket> {

    /**
     * 连接上服务器时调用
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 登录请求对象
        LoginRequestPacket loginRequestPacket = initialLoginRequest();

        // 写数据
        ctx.channel().writeAndFlush(loginRequestPacket);
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
     * 处理登录response对象
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginResponsePacket msg) {
        if (msg.isSuccess()) {
            System.out.println(new Date() + ": 客户端登录成功");
            // 标记登录成功
            LoginUtil.markAsLogin(ctx.channel());
        } else {
            System.out.println(new Date() + ": 客户端登录失败，原因：" + msg.getReason());
        }
    }
}
