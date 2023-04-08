package netty.book.practice.protocol.request;

import netty.book.practice.protocol.Packet;
import netty.book.practice.protocol.command.Command;

/**
 * 客户端心跳请求对象
 *
 * @author FangYuan
 * @since 2023-04-08 15:02:09
 */
public class HeartBeatRequestPacket extends Packet {
    @Override
    public Command getCommand() {
        return Command.HEART_BEAT_REQUEST;
    }
}
