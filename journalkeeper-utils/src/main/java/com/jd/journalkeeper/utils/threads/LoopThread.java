package com.jd.journalkeeper.utils.threads;


import com.jd.journalkeeper.utils.state.StateServer;

import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 一个后台线程，实现类似：
 * while(true){
 *     doWork();
 * }
 * 的线程。
 */
public abstract class LoopThread implements Runnable, StateServer {
    private Thread thread = null;
    private String name;
    protected long minSleep = 50L,maxSleep = 500L;
    private boolean daemon;
    private final Lock wakeupLock = new ReentrantLock();
    private final java.util.concurrent.locks.Condition wakeupCondition = wakeupLock.newCondition();

    /**
     * 每次循环需要执行的代码。
     */
    abstract void doWork() throws Throwable;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isDaemon() {
        return daemon;
    }

    public void setDaemon(boolean daemon) {
        this.daemon = daemon;
    }

    /**
     * doWork() 前判断是否满足条件。
     * @return true: 执行doWork。
     */
    protected boolean condition() {
        return true;
    }

    @Override
    public synchronized void start() {
        if(!isStarted()) {
            thread = new Thread(this);
            thread.setName(name == null ? "LoopThread": name);
            thread.setDaemon(daemon);
            thread.start();
        }
    }

    @Override
    public synchronized void stop() {


        while (isStarted()){
            thread.interrupt();
            try {
                Thread.sleep(10L);
            } catch (InterruptedException ignored) {}
        }

    }

    private boolean isStarted() {
        return serverState() == ServerState.RUNNING;
    }
    @Override
    public ServerState serverState() {
        return (thread!= null && thread.isAlive()) ? ServerState.RUNNING : ServerState.STOPPED;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {

            long t0 = System.currentTimeMillis();
            try {
                wakeupLock.lock();
                if(condition()) doWork();
                long t1 = System.currentTimeMillis();

                // 为了避免空转CPU高，如果执行时间过短，等一会儿再进行下一次循环
                if (t1 - t0 < minSleep) {
                    wakeupCondition.await(minSleep < maxSleep ? ThreadLocalRandom.current().nextLong(minSleep, maxSleep): minSleep, TimeUnit.MILLISECONDS);
                }

            } catch (InterruptedException i) {
                Thread.currentThread().interrupt();
            } catch (Throwable t) {
                if (!handleException(t)) {
                    break;
                }
            } finally {
                wakeupLock.unlock();
            }
        }
    }

    /**
     * 唤醒任务如果任务在Sleep
     */
    public synchronized void weakup() {
        if(wakeupLock.tryLock()) {
            try {
                wakeupCondition.signal();
            } finally {
                wakeupLock.unlock();
            }
        }
    }

    /**
     * 处理doWork()捕获的异常
     * @return true：继续循环，false：结束线程
     */
    protected boolean handleException(Throwable t) {
        return true;
    }

    public static Builder builder() {
        return new Builder();
    }

    public interface  Worker {
        void doWork() throws Throwable;
    }

    public interface ExceptionHandler {
        boolean handleException(Throwable t);
    }
    public interface ExceptionListener {
        void onException(Throwable t);
    }
    public interface Condition{
        boolean condition();
    }

    public static class Builder {
        private String name;
        private long minSleep = -1L,maxSleep = -1L;
        private Boolean daemon;
        private Worker worker;
        private ExceptionHandler exceptionHandler;
        private ExceptionListener exceptionListener;
        private Condition condition;

        public Builder doWork(Worker worker){
            this.worker = worker;
            return this;
        }

        public Builder handleException(ExceptionHandler exceptionHandler){
            this.exceptionHandler = exceptionHandler;
            return this;
        }

        public Builder onException(ExceptionListener exceptionListener){
            this.exceptionListener = exceptionListener;
            return this;
        }



        public Builder name(String name){
            this.name = name;
            return this;
        }

        public Builder sleepTime(long minSleep, long maxSleep){
            this.minSleep = minSleep;
            this.maxSleep = maxSleep;
            return this;
        }

        public Builder daemon(boolean daemon) {
            this.daemon = daemon;
            return this;
        }

        public Builder condition(Condition condition){
            this.condition = condition;
            return this;
        }

        public LoopThread build(){
            LoopThread loopThread = new LoopThread() {
                @Override
                void doWork() throws Throwable{
                    worker.doWork();
                }

                @Override
                protected boolean handleException(Throwable t) {
                    if(null != exceptionListener) exceptionListener.onException(t);
                    if(null != exceptionHandler) {
                        return exceptionHandler.handleException(t);
                    }else {
                        return super.handleException(t);
                    }
                }

                @Override
                protected boolean condition() {
                    if(null != condition) {
                        return condition.condition();
                    }else {
                        return super.condition();
                    }
                }
            };
            if(null != name) loopThread.setName(name);
            if(null != daemon) loopThread.setDaemon(daemon);
            if(this.minSleep >= 0) loopThread.minSleep = this.minSleep;
            if(this.maxSleep >= 0) loopThread.maxSleep = this.maxSleep;
            return loopThread;
        }


    }
}
