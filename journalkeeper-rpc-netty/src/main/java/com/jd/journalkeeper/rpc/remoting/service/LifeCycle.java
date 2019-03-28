package com.jd.journalkeeper.rpc.remoting.service;

/**
 * 生命周期管理
 */
public interface LifeCycle extends Online {

    /**
     * 启动
     *
     * @throws Exception
     */
    void start() throws Exception;


    /**
     * 停止
     */
    void stop();
}
