package netty.book.practice.protocol.response;

import netty.book.practice.protocol.Packet;
import netty.book.practice.protocol.command.Command;

/**
 * 心跳响应对象对象
 *
 * @author FangYuan
 * @since 2023-04-08 15:06:49
 */
public class HeartBeatResponsePacket extends Packet {
    @Override
    public Command getCommand() {
        return Command.HEART_BEAT_RESPONSE;
    }
}
