package netty.book.practice.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * ChannelHandler的生命周期
 *
 * @author FangYuan
 * @since 2023-03-31 21:02:31
 */
public class LifeCycleHandler extends ChannelInboundHandlerAdapter {
    /**
     * 当检测到新连接之后，调用 ch.pipeline().addLast(...); 之后的回调
     * 表示当前channel中成功添加了 Handler
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("逻辑处理器被添加时回调：handlerAdded()");
        super.handlerAdded(ctx);
    }

    /**
     * 表示当前channel的所有逻辑处理已经和某个NIO线程建立了绑定关系
     * 这里的NIO线程通常指的是 NioEventLoop
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 绑定到线程(NioEventLoop)时回调：channelRegistered()");
        super.channelRegistered(ctx);
    }

    /**
     * 当Channel的所有业务逻辑链准备完毕，链接被激活时
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 准备就绪时回调：channelActive()");
        super.channelActive(ctx);
    }

    /**
     * 客户端向服务端发送数据，表示有数据可读时，就会回调该方法
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channel 有数据可读时回调：channelRead()");
        super.channelRead(ctx, msg);
    }

    /**
     * 服务端每完整的读完一次数据，都会回调该方法
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 某次数据读完时回调：channelReadComplete()");
        super.channelReadComplete(ctx);
    }

    // ---断开链接时---

    /**
     * 该客户端与服务端的连接被关闭时回调
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 被关闭时回调：channelInactive()");
        super.channelInactive(ctx);
    }

    /**
     * 对应的NIO线程移除了对这个链接的处理
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 取消线程(NioEventLoop) 的绑定时回调: channelUnregistered()");
        super.channelUnregistered(ctx);
    }

    /**
     * 为该链接添加的所有业务逻辑Handler被移除时
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("逻辑处理器被移除时回调：handlerRemoved()");
        super.handlerRemoved(ctx);
    }
}
