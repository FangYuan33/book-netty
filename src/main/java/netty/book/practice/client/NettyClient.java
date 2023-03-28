package netty.book.practice.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import netty.book.practice.handler.client.ClientHandler;

import java.util.Date;
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
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        socketChannel.pipeline().addLast(new ClientHandler());
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
}
