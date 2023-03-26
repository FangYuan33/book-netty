package netty.book.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.util.AttributeKey;

/**
 * Netty 服务端
 *
 * @author FangYuan
 * @since 2023-03-26 10:09:00
 */
public class NettyServer {
    public static void main(String[] args) {
        // 启动一个netty服务端需要指定 线程模型 IO模型 业务处理逻辑

        // 负责服务端的启动
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        // 以下两个对象可以看做是两个线程组
        // 负责监听端口，接受新的链接
        NioEventLoopGroup boss = new NioEventLoopGroup();
        // 负责读取数据
        NioEventLoopGroup worker = new NioEventLoopGroup();

        // 配置线程组并指定NIO模型
        serverBootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
                // 定义后续每个 新链接 的读写业务逻辑
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                        nioSocketChannel.pipeline().addLast(new StringDecoder())
                                .addLast(new SimpleChannelInboundHandler<String>() {
                                    @Override
                                    protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
                                        System.out.println(msg);
                                    }
                                });
                    }
                });

        // handler() 方法 用于指定服务端启动时的一些逻辑; 注意 childHandler() 方法是为每个新连接添加的处理逻辑
        serverBootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {

            }
        });
        // attr() 方法 用于给 NioServerSocketChannel 维护一个 Map
        serverBootstrap.attr(AttributeKey.newInstance("serverName"), "nettyServer");
        // childAttr() 方法 可以为每个链接都指定自定义的属性
        serverBootstrap.childAttr(AttributeKey.newInstance("clientKey"), "clientValue");
        // option() 方法 用于给服务端Channel设置一些TCP参数 SO_BACKLOG 表示系统用于临时存放已完成三次握手的请求的队列的最大长度
        serverBootstrap.option(ChannelOption.SO_BACKLOG, 1024);
        // childOption() 方法 用于给每个链接都设置一些TCP参数，SO_KEEPALIVE 表示是否开启TCP心跳机制
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        // TCP_NODELAY 表示是否开启Nagle算法
        serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);

        bind(serverBootstrap, 8888);
    }

    /**
     * 以端口号递增的形式尝试绑定端口号
     */
    private static void bind(ServerBootstrap serverBootstrap, int port) {
        // bind 方法是异步的，为其添加监听器
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println("端口[" + port + "]绑定成功!");
            } else {
                System.err.println("端口[" + port + "]绑定失败!");
                bind(serverBootstrap, port + 1);
            }
        });
    }
}
