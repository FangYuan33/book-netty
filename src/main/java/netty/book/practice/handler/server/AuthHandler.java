package netty.book.practice.handler.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.book.practice.util.SessionUtil;

/**
 * 身份验证Handler
 *
 * @author FangYuan
 * @since 2023-04-01 20:29:53
 */
public class AuthHandler extends ChannelInboundHandlerAdapter {
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (SessionUtil.hasLogin(ctx.channel())) {
            // 如果登录成功则移除该处理节点
            ctx.pipeline().remove(this);
            super.channelRead(ctx, msg);
        } else {
            // 没有成功的话需要关闭连接
            ctx.channel().close();
        }
    }

    /**
     * 当登录成功节点被移除时回调
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        if (SessionUtil.hasLogin(ctx.channel())) {
            System.out.println("当前连接登录验证完毕，无需再次验证, AuthHandler 被移除");
        } else {
            System.out.println("无登录验证，强制关闭连接!");
        }
    }
}
