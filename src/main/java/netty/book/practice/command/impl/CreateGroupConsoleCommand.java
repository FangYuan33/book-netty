package netty.book.practice.command.impl;

import io.netty.channel.Channel;
import netty.book.practice.command.ConsoleCommand;

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

    }
}
