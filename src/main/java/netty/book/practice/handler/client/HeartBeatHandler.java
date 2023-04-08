package netty.book.practice.handler.client;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import netty.book.practice.protocol.request.HeartBeatRequestPacket;

import java.util.concurrent.TimeUnit;

/**
 * 客户端发送心跳处理的Handler
 *
 * @author FangYuan
 * @since 2023-04-08 15:11:57
 */
public class HeartBeatHandler extends ChannelInboundHandlerAdapter {

    /**
     * 心跳时间间隔
     */
    private static final int HEARTBEAT_INTERVAL = 5;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        scheduleSendHeartBeat(ctx);
        super.channelActive(ctx);
    }

    /**
     * 定时任务发送心跳
     */
    private void scheduleSendHeartBeat(ChannelHandlerContext ctx) {
        ctx.executor().schedule(() -> {
            if (ctx.channel().isActive()) {
                ctx.writeAndFlush(new HeartBeatRequestPacket());
                scheduleSendHeartBeat(ctx);
            }
        }, HEARTBEAT_INTERVAL, TimeUnit.SECONDS);
    }
}
