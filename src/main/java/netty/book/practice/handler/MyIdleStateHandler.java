package netty.book.practice.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;

import java.util.concurrent.TimeUnit;

/**
 * 读超时，关闭连接处理的Handler
 *
 * @author FangYuan
 * @since 2023-04-08 14:35:34
 */
public class MyIdleStateHandler extends IdleStateHandler {

    private static final int READER_IDLE_TIME = 15;

    public MyIdleStateHandler() {
        // 读超时时间、写超时时间、读写超时时间 指定0值不判断超时
        super(READER_IDLE_TIME, 0, 0, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) {
        System.out.println(READER_IDLE_TIME + "秒内没有读到数据，关闭连接");
        ctx.channel().close();
    }
}
