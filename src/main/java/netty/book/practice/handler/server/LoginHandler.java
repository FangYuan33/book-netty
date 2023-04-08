package netty.book.practice.handler.server;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.book.practice.protocol.request.LoginRequestPacket;
import netty.book.practice.protocol.response.LoginResponsePacket;
import netty.book.practice.session.Session;
import netty.book.practice.util.SessionUtil;

import java.util.UUID;

/**
 * 服务端处理登录请求的Handler
 *
 * @author FangYuan
 * @since 2023-03-30 20:33:06
 */
@ChannelHandler.Sharable
public class LoginHandler extends SimpleChannelInboundHandler<LoginRequestPacket> {

    /**
     * 单例
     */
    public static final LoginHandler LOGIN_HANDLER = new LoginHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestPacket msg) {
        // 初始化用户ID
        String userId = UUID.randomUUID().toString().split("-")[0];
        // 默认登录成功
        LoginResponsePacket loginResponsePacket = initialResponseInfo(userId, msg.getUserName());

        System.out.println("[" + loginResponsePacket.getUserName() + "]登录成功");
        SessionUtil.bindSession(new Session(userId, msg.getUserName()), ctx.channel());

        // 回写登录响应
        ctx.channel().writeAndFlush(loginResponsePacket);
    }

    /**
     * 初始化登录响应信息
     */
    private LoginResponsePacket initialResponseInfo(String userId, String userName) {
        LoginResponsePacket loginResponsePacket = new LoginResponsePacket();
        loginResponsePacket.setSuccess(true);
        loginResponsePacket.setReason("登录成功");
        loginResponsePacket.setUserId(userId);
        loginResponsePacket.setUserName(userName);

        return loginResponsePacket;
    }

    /**
     * 客户端登出时回调
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        SessionUtil.unBindSession(ctx.channel());
    }
}
