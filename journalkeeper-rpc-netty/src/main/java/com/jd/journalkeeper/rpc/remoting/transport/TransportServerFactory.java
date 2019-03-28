package com.jd.journalkeeper.rpc.remoting.transport;

import com.jd.journalkeeper.rpc.remoting.transport.config.ServerConfig;

/**
 * 通信工厂
 * author: gaohaoxiang
 * email: gaohaoxiang@jd.com
 * date: 2018/8/13
 */
public interface TransportServerFactory {

    TransportServer bind(ServerConfig serverConfig);

    TransportServer bind(ServerConfig serverConfig, String host);

    TransportServer bind(ServerConfig serverConfig, String host, int port);
}