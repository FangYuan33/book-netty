package netty.book.practice.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import netty.book.practice.handler.SplitHandler;
import netty.book.practice.handler.server.AuthHandler;
import netty.book.practice.handler.server.CreateGroupHandler;
import netty.book.practice.handler.server.LoginHandler;
import netty.book.practice.handler.server.MessageHandler;
import netty.book.practice.serialize.codec.PacketDecoder;
import netty.book.practice.serialize.codec.PacketEncoder;

import java.util.Date;

/**
 * Netty 服务端
 *
 * @author FangYuan
 * @since 2023-03-28 20:35:21
 */
public class NettyServer {

    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        serverBootstrap.group(new NioEventLoopGroup(), new NioEventLoopGroup()).channel(NioServerSocketChannel.class)
                .option(ChannelOption.SO_BACKLOG, 1024).option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) {
                        socketChannel.pipeline()
//                                .addLast(new LifeCycleHandler())
                                // 解决粘包和半包问题
                                .addLast(new SplitHandler())
                                .addLast(new PacketDecoder())
                                .addLast(new LoginHandler(), new AuthHandler(), new MessageHandler())
                                .addLast(new CreateGroupHandler())
                                .addLast(new PacketEncoder());
                    }
                });

        bind(serverBootstrap, 8080);
    }

    /**
     * 绑定端口号，绑定失败后重试
     */
    private static void bind(ServerBootstrap serverBootstrap, int port) {
        serverBootstrap.bind(port).addListener(future -> {
            if (future.isSuccess()) {
                System.out.println(new Date() + ": 端口[" + port + "]绑定成功!");
            } else {
                System.err.println("端口[" + port + "]绑定失败!");
                bind(serverBootstrap, port + 1);
            }
        });
    }
}
