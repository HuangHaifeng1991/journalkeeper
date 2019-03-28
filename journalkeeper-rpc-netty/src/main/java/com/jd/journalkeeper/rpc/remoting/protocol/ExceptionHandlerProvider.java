package com.jd.journalkeeper.rpc.remoting.protocol;

import com.jd.journalkeeper.rpc.remoting.transport.command.handler.ExceptionHandler;

/**
 * 异常处理提供器
 * author: gaohaoxiang
 * email: gaohaoxiang@jd.com
 * date: 2018/8/28
 */
public interface ExceptionHandlerProvider {

    ExceptionHandler getExceptionHandler();
}