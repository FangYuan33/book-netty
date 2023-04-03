package netty.book.practice.command.impl;

import io.netty.channel.Channel;
import netty.book.practice.command.ConsoleCommand;
import netty.book.practice.protocol.request.CreateGroupRequestPacket;

import java.util.Arrays;
import java.util.Scanner;

/**
 * 创建群组的命令
 *
 * @author FangYuan
 * @since 2023-04-02 22:47:38
 */
public class CreateGroupConsoleCommand implements ConsoleCommand {

    /**
     * 创建群组用户名分割符号
     */
    private static final String USER_NAME_SPLIT_CHAR = ",";

    @Override
    public void exec(Scanner scanner, Channel channel) {
        // 群组创建请求
        CreateGroupRequestPacket packet = new CreateGroupRequestPacket();

        System.out.print("【拉人群聊】输入 userName 列表，userName 之间英文逗号隔开：");
        String userNames = scanner.next();
        packet.setUserNameList(Arrays.asList(userNames.split(USER_NAME_SPLIT_CHAR)));

        channel.writeAndFlush(packet);
    }
}
