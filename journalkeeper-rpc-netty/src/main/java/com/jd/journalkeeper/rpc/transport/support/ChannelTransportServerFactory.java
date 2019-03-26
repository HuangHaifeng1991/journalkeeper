package com.jd.journalkeeper.rpc.transport.support;

import com.jd.journalkeeper.rpc.transport.TransportServer;
import com.jd.journalkeeper.rpc.transport.TransportServerFactory;
import com.jd.journalkeeper.rpc.transport.config.ServerConfig;
import io.netty.channel.ChannelHandler;

/**
 * 自定义channelhandler
 * author: gaohaoxiang
 * email: gaohaoxiang@jd.com
 * date: 2018/9/25
 */
public class ChannelTransportServerFactory implements TransportServerFactory {

    private ChannelHandler channelHandler;

    public ChannelTransportServerFactory(ChannelHandler channelHandler) {
        this.channelHandler = channelHandler;
    }

    @Override
    public TransportServer bind(ServerConfig serverConfig) {
        return bind(serverConfig, serverConfig.getHost(), serverConfig.getPort());
    }

    @Override
    public TransportServer bind(ServerConfig serverConfig, String host) {
        return bind(serverConfig, host, serverConfig.getPort());
    }

    @Override
    public TransportServer bind(ServerConfig serverConfig, String host, int port) {
        return new ChannelTransportServer(channelHandler, serverConfig, host, port);
    }
}