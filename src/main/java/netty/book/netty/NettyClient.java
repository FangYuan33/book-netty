package netty.book.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.AttributeKey;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Netty 客户端
 *
 * @author FangYuan
 * @since 2023-03-26 10:13:48
 */
public class NettyClient {

    /**
     * 最大重连间隔
     */
    private static final int MAX_RETRY = 5;

    public static void main(String[] args) throws InterruptedException {
        // 启动一个netty客户端需要指定 线程模型 IO模型 业务处理逻辑

        // 负责客户端的启动
        Bootstrap bootstrap = new Bootstrap();
        // 客户端的线程模型
        NioEventLoopGroup group = new NioEventLoopGroup();

        // 指定线程组和NIO模型
        bootstrap.group(group).channel(NioSocketChannel.class)
                // handler() 方法 业务处理逻辑
                .handler(new ChannelInitializer<Channel>() {
                    @Override
                    protected void initChannel(Channel channel) throws Exception {
                        channel.pipeline().addLast(new StringEncoder());
                    }
                });

        // attr() 方法 用于给NioSocketChannel指定一个Map 按需从其中取值
        bootstrap.attr(AttributeKey.newInstance("clientName"), "nettyClient");
        // option() 方法用于指定一些TCP参数 CONNECT_TIMEOUT_MILLIS 指定链接超时的时间
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000);
        // SO_KEEPALIVE 表示是否开启TCP心跳机制
        bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
        // TCP_NODELAY 表示是否开启Nagle算法
        bootstrap.option(ChannelOption.TCP_NODELAY, true);

        // 建立连接
        Channel channel = connect(bootstrap, "127.0.0.1", 8080, MAX_RETRY);

        while (true) {
            channel.writeAndFlush(new Date() + ": Hello world!");
            Thread.sleep(1000);
        }
    }

    /**
     * 建立连接的方法，使用监听器来进行重试
     */
    private static Channel connect(Bootstrap bootstrap, String host, int port, int retry) {
        return bootstrap.connect(host, port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("连接成功!");
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
        }).channel();
    }
}
