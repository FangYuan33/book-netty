package netty.book.practice.command.impl;

import io.netty.channel.Channel;
import netty.book.practice.command.ConsoleCommand;
import netty.book.practice.protocol.request.JoinGroupRequestPacket;

import java.util.Scanner;

/**
 * 加入群组命令
 *
 * @author FangYuan
 * @since 2023-04-03 20:32:03
 */
public class JoinGroupConsoleCommand implements ConsoleCommand {
    @Override
    public void exec(Scanner scanner, Channel channel) {
        JoinGroupRequestPacket joinGroupRequestPacket = new JoinGroupRequestPacket();

        System.out.print("输入 groupId，加入群聊：");
        String groupId = scanner.next();

        joinGroupRequestPacket.setGroupId(groupId);
        channel.writeAndFlush(joinGroupRequestPacket);
    }
}
