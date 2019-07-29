/**
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jd.journalkeeper.rpc.remoting.transport.command.support;

import com.jd.journalkeeper.rpc.remoting.protocol.ExceptionHandlerProvider;
import com.jd.journalkeeper.rpc.remoting.protocol.Protocol;
import com.jd.journalkeeper.rpc.remoting.transport.RequestBarrier;
import com.jd.journalkeeper.rpc.remoting.transport.command.CommandDispatcher;
import com.jd.journalkeeper.rpc.remoting.transport.command.CommandDispatcherFactory;
import com.jd.journalkeeper.rpc.remoting.transport.command.handler.CommandHandlerFactory;
import com.jd.journalkeeper.rpc.remoting.transport.command.handler.ExceptionHandler;
import com.jd.journalkeeper.rpc.remoting.transport.command.handler.filter.CommandHandlerFilterFactory;
import com.jd.journalkeeper.rpc.remoting.transport.config.TransportConfig;

/**
 * 默认命令调度器工厂
 * author: gaohaoxiang
 * email: gaohaoxiang@jd.com
 * date: 2018/8/16
 */
public class DefaultCommandDispatcherFactory implements CommandDispatcherFactory {

    private TransportConfig transportConfig;
    private RequestBarrier requestBarrier;
    private CommandHandlerFilterFactory commandHandlerFilterFactory;
    private ExceptionHandler exceptionHandler;

    public DefaultCommandDispatcherFactory(TransportConfig transportConfig, RequestBarrier requestBarrier, CommandHandlerFilterFactory commandHandlerFilterFactory, ExceptionHandler exceptionHandler) {
        this.transportConfig = transportConfig;
        this.requestBarrier = requestBarrier;
        this.commandHandlerFilterFactory = commandHandlerFilterFactory;
        this.exceptionHandler = exceptionHandler;
    }

    @Override
    public CommandDispatcher getCommandDispatcher(Protocol protocol) {
        ExceptionHandler exceptionHandler = getExceptionHandler(protocol);
        CommandHandlerFactory commandHandlerFactory = protocol.createCommandHandlerFactory();
        RequestHandler requestHandler = new RequestHandler(commandHandlerFactory, commandHandlerFilterFactory, exceptionHandler);
        ResponseHandler responseHandler = new ResponseHandler(transportConfig, requestBarrier, exceptionHandler);
        return new DefaultCommandDispatcher(requestBarrier, requestHandler, responseHandler);
    }

    protected ExceptionHandler getExceptionHandler(Protocol protocol) {
        if (protocol instanceof ExceptionHandlerProvider) {
            return ((ExceptionHandlerProvider) protocol).getExceptionHandler();
        } else {
            return this.exceptionHandler;
        }
    }
}