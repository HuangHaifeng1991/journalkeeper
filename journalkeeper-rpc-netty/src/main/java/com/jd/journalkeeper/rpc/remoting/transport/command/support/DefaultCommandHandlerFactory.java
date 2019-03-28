package com.jd.journalkeeper.rpc.remoting.transport.command.support;

import com.jd.journalkeeper.rpc.remoting.transport.command.Command;
import com.jd.journalkeeper.rpc.remoting.transport.command.Type;
import com.jd.journalkeeper.rpc.remoting.transport.command.Types;
import com.jd.journalkeeper.rpc.remoting.transport.command.handler.CommandHandler;
import com.jd.journalkeeper.rpc.remoting.transport.command.handler.CommandHandlerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * 默认命令处理工厂
 * author: gaohaoxiang
 * email: gaohaoxiang@jd.com
 * date: 2018/8/13
 */
public class DefaultCommandHandlerFactory implements CommandHandlerFactory {

    protected static final Logger logger = LoggerFactory.getLogger(DefaultCommandHandlerFactory.class);

    private Map<Integer /** type **/, CommandHandler> commandHandlerMapper = new HashMap<>();

    @Override
    public CommandHandler getHandler(Command command) {
        int commandType = command.getHeader().getType();
        return commandHandlerMapper.get(commandType);
    }

    public void register(CommandHandler commandHandler) {
        if (commandHandler instanceof Types) {
            Types types = (Types) commandHandler;
            for (int type : types.types()) {
                register(type, commandHandler);
            }
        } else if (commandHandler instanceof Type) {
            register(((Type) commandHandler).type(), commandHandler);
        } else {
            logger.warn("unsupported command type, commandHandler: {}", commandHandler);
        }
    }

    public void register(int type, CommandHandler commandHandler) {
        commandHandlerMapper.put(type, commandHandler);
    }
}