package io.journalkeeper.core.server;

/**
 * @author LiYue
 * Date: 2019-08-14
 */
interface CallbackBelt<R> {
    void callbackBefore(long position);
    void put(Callback<R> callback);
    boolean setResult(long position, R result);
}
