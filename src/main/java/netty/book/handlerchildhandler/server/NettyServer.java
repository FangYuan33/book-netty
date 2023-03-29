package netty.book.handlerchildhandler.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import netty.book.inoutbound.in.InboundHandlerA;
import netty.book.inoutbound.in.InboundHandlerB;
import netty.book.inoutbound.out.OutboundHandlerA;
import netty.book.inoutbound.out.OutboundHandlerB;

/**
 * Netty 服务端
 *
 * @author FangYuan
 * @since 2023-03-26 14:32:28
 */
public class NettyServer {
    public static void main(String[] args) {
        ServerBootstrap serverBootstrap = new ServerBootstrap();

        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();

        serverBootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<NioSocketChannel>() {
                    @Override
                    protected void initChannel(NioSocketChannel ch) throws Exception {
                        // 处理读数据的逻辑
                        ch.pipeline().addLast(new InboundHandlerA()).addLast(new InboundHandlerB());

                        ch.pipeline().addLast(new FirstServerHandler());

                        // 处理写数据的逻辑
                        ch.pipeline().addLast(new OutboundHandlerA()).addLast(new OutboundHandlerB());
                    }
                });

        serverBootstrap.bind("127.0.0.1", 8080);
    }
}
