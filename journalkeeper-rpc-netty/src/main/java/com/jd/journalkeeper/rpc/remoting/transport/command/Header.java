package com.jd.journalkeeper.rpc.remoting.transport.command;


/**
 * @author hexiaofeng
 * @date 16-6-22.
 */
public interface Header {

    boolean isOneWay();
    void setOneWay(boolean isOneWay);
    /**
     * 状态
     *
     * @return
     */
    int getStatus();

    /**
     * 状态
     *
     * @param status
     */
    void setStatus(int status);

    /**
     * error
     *
     * @return
     */
    String getError();

    /**
     * error
     *
     * @param msg
     */
    void setError(String msg);

    /**
     * 请求ID
     *
     * @return
     */
    int getRequestId();

    /**
     * 请求ID
     *
     * @param requestId
     */
    void setRequestId(int requestId);

    /**
     * 获取数据包方向
     *
     * @return 数据包方向
     */
    Direction getDirection();

    /**
     * 设置数据包方向
     *
     * @param direction 方向
     */
    void setDirection(Direction direction);

    /**
     * 设置版本号
     *
     * @param version
     */
    void setVersion(int version);

    /**
     * 版本号
     *
     * @return
     */
    int getVersion();

    /**
     * 设置类型
     *
     * @param type
     */
    void setType(int type);

    /**
     * 类型
     *
     * @return
     */
    int getType();
}
