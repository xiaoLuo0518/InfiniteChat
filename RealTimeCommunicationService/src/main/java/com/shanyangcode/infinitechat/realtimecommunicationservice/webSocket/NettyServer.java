package com.shanyangcode.infinitechat.realtimecommunicationservice.webSocket;

import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.exception.NacosException;
import com.alibaba.nacos.api.naming.NamingService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.NettyRuntime;
import io.netty.util.concurrent.Future;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.InetAddress;
import java.net.UnknownHostException;


/**
 * @ClassName NettyServer
 * @Description
 * @Author XiaoLuo
 * @Date 2025/7/28 20:11
 */

@Configuration
@Slf4j
@RequiredArgsConstructor
public class NettyServer {

    @Value("${netty.port}")
    private int port;

    @Value("${netty.name}")
    private String serverName;

    @Autowired
    private final NacosServiceManager nacosServiceManager;

    @Autowired
    private final StringRedisTemplate redisTemplate;

    @Autowired
    private DiscoveryClient discoveryClient;


    //主线程组，处理连接请求
    private NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);
    //工作线程组，处理网络读写
    private NioEventLoopGroup workerGroup = new NioEventLoopGroup(NettyRuntime.availableProcessors());


    @PostConstruct
    public void start() throws InterruptedException, UnknownHostException, NacosException {
        run();
        NamingService namingService = nacosServiceManager.getNamingService();
        namingService.registerInstance(this.serverName, InetAddress.getLocalHost().getHostAddress(), this.port);
    }

    public void run() throws InterruptedException {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
                //指定服务器用哪种通道类型来接收客户端连接。基于 NIO（Java 的非阻塞 I/O）服务端套接字通道
                .channel(NioServerSocketChannel.class)
                //服务端全连接队列长度上限
                .option(ChannelOption.SO_BACKLOG, 128)
                //给每一个被接受的客户端连接设置参数。
                .childOption(ChannelOption.SO_KEEPALIVE, true)
                // 给 bossGroup 的 ServerChannel 添加一个日志处理器。
                .handler(new LoggingHandler(LogLevel.INFO))

                //设置子通道处理器（客户端连接的Pipeline初始化器）
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();
                        pipeline.addLast(new IdleStateHandler(5 * 60, 0, 0));
                        pipeline.addLast(new HttpServerCodec());
                        pipeline.addLast(new HttpObjectAggregator(8192));
                        //token和userId的获取 使用 NettyUtils.setAttr 把这些信息 绑定到当前 Channel 的 Attribute 中
                        pipeline.addLast(new WebSocketTokenAuthHeader());
                        //处理 WebSocket 协议升级（HTTP → WebSocket）。
                        pipeline.addLast(new WebSocketServerProtocolHandler("/ws"));
                        pipeline.addLast(new MessageInBoundHandler(redisTemplate));
                    }
                });
        serverBootstrap.bind(port).sync();
    }

    @PreDestroy
    public void destroy() {
        Future<?> future = bossGroup.shutdownGracefully();
        Future<?> future1 = workerGroup.shutdownGracefully();

        future.syncUninterruptibly();
        future1.syncUninterruptibly();

        log.info("关闭 ws server 成功....");
    }
}
