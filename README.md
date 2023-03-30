# 跟闪电侠学Netty：Netty即时聊天实战与底层原理

Netty是一个异步基于**事件驱动**的**高性能网络通信**框架

### 服务端和客户端的启动流程

服务端启动需要创建 `ServerBootstrap` 对象，需要**初始化线程模型**，**配置IO模型**和**添加业务处理逻辑**

```java
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
```

客户端与服务端启动类似，创建的是 `Bootstrap` 对象，同样需要初始化以上三个对象信息

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
                channel.pipeline().addLast(new StringEncoder());
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
