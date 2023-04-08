package netty.book.practice.handler.server;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import netty.book.practice.protocol.request.HeartBeatRequestPacket;
import netty.book.practice.protocol.response.HeartBeatResponsePacket;

/**
 * 服务端心跳处理Handler
 *
 * @author FangYuan
 * @since 2023-04-08 15:06:08
 */
public class HeartBeatHandler extends SimpleChannelInboundHandler<HeartBeatRequestPacket> {

    /**
     * 单例
     */
    public static final HeartBeatHandler HEART_BEAT_HANDLER = new HeartBeatHandler();

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, HeartBeatRequestPacket msg) throws Exception {
        // 直接回写心跳ACK
        ctx.writeAndFlush(new HeartBeatResponsePacket());
    }
}
