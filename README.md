# 跟闪电侠学Netty：Netty即时聊天实战与底层原理

Netty是一个异步基于**事件驱动**的**高性能网络通信**框架，可以看做是对NIO和BIO的封装，并提供了简单易用的API、Handler和工具类等，
用以快速开发高性能、高可靠性的网络服务端和客户端程序。

### 1. 创建服务端

服务端启动需要创建 `ServerBootstrap` 对象，并完成**初始化线程模型**，**配置IO模型**和**添加业务处理逻辑（Handler）**。在添加业务处理逻辑时，
调用的是 `childHandler()` 方法添加了一个 `ChannelInitializer` Handler，代码示例如下

```java
// 负责服务端的启动
ServerBootstrap serverBootstrap = new ServerBootstrap();

// 以下两个对象可以看做是两个线程组
// boss线程组负责监听端口，接受新的连接
NioEventLoopGroup boss = new NioEventLoopGroup();
// worker线程组负责读取数据
NioEventLoopGroup worker = new NioEventLoopGroup();

// 配置线程组并指定NIO模型
serverBootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
        // 定义后续每个 新连接 的读写业务逻辑
        .childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
                nioSocketChannel.pipeline()
                        // 添加业务处理逻辑
                        .addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
                                System.out.println(msg);
                            }
                        });
            }
        });
```

其中 `nioSocketChannel.pipeline()` 获取 `PipeLine` 对象，调用方法 `addLast()` 添加需要的业务处理逻辑，这里采用的是**责任链模式**，
会将每个Handler作为一个节点进行处理

#### 1.1 创建客户端

客户端与服务端启动类似，不同的是，客户端需要创建 `Bootstrap` 对象来启动，并指定一个客户端线程组，
相同的是都需要完成**初始化线程模型**，**配置IO模型**和**添加业务处理逻辑（Handler）**， 示例代码如下

```java
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
                channel.pipeline()
                    // 添加业务处理逻辑
                    .addLast(new SimpleChannelInboundHandler<String>() {
                            @Override
                            protected void channelRead0(ChannelHandlerContext channelHandlerContext, String msg) throws Exception {
                                System.out.println(msg);
                            }
                    });
            }
        });
```

### ChannelInboundHandlerAdapter 和 ChannelOutboundHandlerAdapter

在Netty框架里，每个连接对应着一个 `Channel`，而这个 `Channel` 的所有处理逻辑都在一个叫作 `ChannelPipeline` 的对象里。
`ChannelPipeline` 是一个双向链表，使用的是责任链模式，每个链表节点中封装着 `Handler`，并且可以获取Channel相关的上下文信息（ChannelHandlerContext）

有两种不同的Handler实现，分别用来处理读数据(ChannelInboundHandlerAdapter)和写数据(ChannelOutboundHandlerAdapter)，
如果Handler按如下顺序配置，它的执行逻辑顺序如下图所示

```java
serverBootstrap.group(boss, worker).channel(NioServerSocketChannel.class)
        .childHandler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                // 处理读数据的逻辑
                ch.pipeline().addLast(new InboundHandlerA()).addLast(new InboundHandlerB());

                // 处理写数据的逻辑
                ch.pipeline().addLast(new OutboundHandlerA()).addLast(new OutboundHandlerB());
            }
        });
```

![](images/inoutbound.jpg)

InboundHandler的执行顺序与添加的责任链节点顺序一致，而OutboundHandler的执行顺序则相反。

### SimpleChannelInboundHandler

`SimpleChannelInboundHandler` 是 `ChannelInboundHandlerAdapter` 的实现类，`SimpleChannelInboundHandler` 能够指定泛型，
这样在处理业务逻辑时，便无需再进行如下逻辑，它会在 `SimpleChannelInboundHandler` 的 `channelRead()` 方法中进行，它是一个模版方法，
我们仅仅需要实现 `channelRead0()` 方法即可

```java
if (message instanceof XXMessage) {
    // process
} else {
    ctx.fireChannelRead(message);   
}
```

### ByteToMessageDecoder 和 MessageToByteEncoder

`ByteToMessageDecoder` 用于将接受到的二进制数据解码成Java对象，ByteBuf默认情况下使用的是堆外内存，
而 `ByteToMessageDecoder` 会**自动**帮我们做**内存释放**

`MessageToByteEncoder` 用于将Java对象编码成二进制数据的ByteBuf，同样Netty会帮我们进行内存释放

### 粘包和半包

即使我们发送消息的时候是以 `ByteBuf` 的形式发送的，但是到了底层操作系统，仍然是以字节流的形式对数据进行发送的，而且服务端也以字节流的形式读取，
因此在服务端对字节流进行拼接时，可能就会造成发送时 `ByteBuf` 与读取时的 `ByteBuf` 不对等，这就会造成粘包和半包的现象

为了解决以上的问题，就需要在数据不足的时候等待读取，直到数据足够时，构成一个完整的数据包并进行业务处理。
一般用基于长度的拆包器 `LengthFieldBasedFrameDecoder` 来进行拆包工作，代码实例如下，其中的三个参数比较重要，第一个参数指定是数据包的最大长度，
第二个参数代表偏移多个字节能找到数据长度信息，第三个参数指的是长度信息的字节数

```java
public class SplitHandler extends LengthFieldBasedFrameDecoder {

    /**
     * 拆包处理器的方法
     */
    public SplitHandler() {
        // 最大长度 数据长度偏移量 表示该数据长度的字段字节数
        super(Integer.MAX_VALUE, 7, 4);
    }
}
```

将该Handler添加到责任链的头结点即可，如下

```java
socketChannel.pipeline().addLast(new SplitHandler())
        .addLast(new PacketDecoder()).addLast(new LoginHandler())
        .addLast(new MessageHandler()).addLast(new PacketEncoder());
```

### ChannelHandler的生命周期

```java
public class LifeCycleHandler extends ChannelInboundHandlerAdapter {
    /**
     * 当检测到新连接之后，调用 ch.pipeline().addLast(...); 之后的回调
     * 表示当前channel中成功添加了 Handler
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        System.out.println("逻辑处理器被添加时回调：handlerAdded()");
        super.handlerAdded(ctx);
    }

    /**
     * 表示当前channel的所有逻辑处理已经和某个NIO线程建立了绑定关系
     * 这里的NIO线程通常指的是 NioEventLoop
     */
    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 绑定到线程(NioEventLoop)时回调：channelRegistered()");
        super.channelRegistered(ctx);
    }

    /**
     * 当Channel的所有业务逻辑链准备完毕，链接被激活时
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 准备就绪时回调：channelActive()");
        super.channelActive(ctx);
    }

    /**
     * 客户端向服务端发送数据，表示有数据可读时，就会回调该方法
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channel 有数据可读时回调：channelRead()");
        super.channelRead(ctx, msg);
    }

    /**
     * 服务端每完整的读完一次数据，都会回调该方法
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 某次数据读完时回调：channelReadComplete()");
        super.channelReadComplete(ctx);
    }

    // ---断开链接时---

    /**
     * 该客户端与服务端的连接被关闭时回调
     */
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 被关闭时回调：channelInactive()");
        super.channelInactive(ctx);
    }

    /**
     * 对应的NIO线程移除了对这个链接的处理
     */
    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        System.out.println("channel 取消线程(NioEventLoop) 的绑定时回调: channelUnregistered()");
        super.channelUnregistered(ctx);
    }

    /**
     * 为该链接添加的所有业务逻辑Handler被移除时
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println("逻辑处理器被移除时回调：handlerRemoved()");
        super.handlerRemoved(ctx);
    }
}
```

### 优化

Netty 在每次有新连接到来的时候，都会调用 `ChannelInitializer` 的 `initChannel()` 方法，会将其中相关的 `Handler` 都创建一次，
如果其中的 `Handler` 是无状态能够通用的，可以将其改成单例，这样就能够在每次连接建立时，避免多次创建相同的对象。

`io.netty.channel.ChannelPipelineException: netty.book.practice.handler.server.LoginHandler is not a @Sharable handler, 
so can't be added or removed multiple times`

`SplitHanlder` 不能进行单例处理，因为它的内部实现与每个 `Channel` 都有关，每个 `SplitHandler` 都需要维持每个 `Channel` 读到的数据，
即它是有状态的。

#### 减少NIO线程阻塞

对于耗时的业务操作，需要将它们都丢到**业务线程池中去处理**，因为单个NIO线程会管理很多 `Channel` ，
只要有一个 `Channel` 中的 `Handler` 的 `channelRead()` 方法被业务逻辑阻塞，那么它就会拖慢绑定在该NIO线程上的其他所有 `Channel`。

```java
protected void channelRead(ChannelHandlerContext ctx, Object message) {
    threadPool.submit(new Runnable() {
        // 耗时的业务处理逻辑
        doSomethingSependTooMuchTime();
        
        writeAndFlush();
    });
}
```