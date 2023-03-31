package netty.book.practice.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import netty.book.practice.handler.SplitHandler;
import netty.book.practice.handler.client.LoginHandler;
import netty.book.practice.handler.client.MessageHandler;
import netty.book.practice.protocol.request.MessageRequestPacket;
import netty.book.practice.serialize.codec.PacketDecoder;
import netty.book.practice.serialize.codec.PacketEncoder;
import netty.book.practice.util.LoginUtil;

import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * Netty 客户端
 *
 * @author wangyilong13
 * @since 2023-03-28 20:25:46
 */
public class NettyClient {

    /**
     * 最大重连间隔
     */
    private static final int MAX_RETRY = 5;

    private static final String HOST = "127.0.0.1";

    public static void main(String[] args) {
        Bootstrap bootstrap = new Bootstrap();

        bootstrap.group(new NioEventLoopGroup()).channel(NioSocketChannel.class)
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline().addLast(new SplitHandler()).addLast(new PacketDecoder())
                                .addLast(new LoginHandler()).addLast(new MessageHandler()).addLast(new PacketEncoder());
                    }
                });

        connect(bootstrap, HOST, 8080, 5);
    }

    /**
     * 链接服务端 失败后不断重试
     */
    private static void connect(Bootstrap bootstrap, String host, int port, int retry) {
        bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(new Date() + ": 连接成功!");

                // 链接成功后开启控制台读取消息
//                startConsoleThread(((ChannelFuture) future).channel());

                // 测试粘包和半包
//                testPackage(((ChannelFuture) future).channel());
            } else if (retry == 0) {
                System.err.println("重试次数已用完，放弃连接！");
            } else {
                // 第几次重连
                int order = (MAX_RETRY - retry) + 1;
                // 定时任务下次执行重连的时间
                int delay = 1 << order;
                System.err.println(new Date() + ": 连接失败，第" + order + "次重连……");

                bootstrap.config().group().schedule(() -> connect(bootstrap, host, port, retry - 1),
                        delay, TimeUnit.SECONDS);
            }
        });
    }

    /**
     * 开启读取控制台消息的线程
     */
    private static void startConsoleThread(Channel channel) {
        new Thread(() -> {
            while (!Thread.interrupted()) {
                if (LoginUtil.hasLogin(channel)) {
                    System.out.println("输入消息发送至服务端: ");

                    Scanner scanner = new Scanner(System.in);
                    String line = scanner.nextLine();

                    MessageRequestPacket messageRequestPacket = new MessageRequestPacket();
                    messageRequestPacket.setMessage(line);
                    channel.writeAndFlush(messageRequestPacket);
                }
            }
        }).start();
    }

    /**
     * 测试粘包和半包
     */
    private static void testPackage(Channel channel) {
        MessageRequestPacket messageRequestPacket = new MessageRequestPacket();

        for (int i = 0; i < 100; i++) {
            messageRequestPacket.setMessage("你好哇，李银河" + i);
            channel.writeAndFlush(messageRequestPacket);
        }
    }
}
