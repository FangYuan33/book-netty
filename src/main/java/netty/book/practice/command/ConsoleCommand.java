package netty.book.practice.command;

import io.netty.channel.Channel;

import java.util.Scanner;

/**
 * 客户端控制台命令通用接口
 *
 * @author FangYuan
 * @since 2023-04-02 22:17:00
 */
public interface ConsoleCommand {

    /**
     * 执行命令的通用方法
     *
     * @param channel 客户端通信channel
     */
    void exec(Scanner scanner, Channel channel);
}
