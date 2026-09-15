package com.kailin.util;

import java.util.concurrent.*;

/**
 * @Author yangSong
 * @Date 2020/7/21 12:23
 * @Version 1.0
 */
public class ThreadPoolUtils {

    private static ThreadLocal<ThreadPoolExecutor> threadPool;
    public static final int CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() + 1;
    public static final int MAX_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 2;
    public static final int KEEP_ALIVE_TIME = 1000;
    public static final int BLOCK_QUEUE_SIZE = 1000;


    /**
     * 无返回结果
     *
     * @param runnable
     */
    public static void executor(Runnable runnable) {
        getThreadPoolExecutor().execute(runnable);
    }

    /**
     * 有返回结果
     *
     * @param callable
     * @param <T>
     * @return
     */
    public static <T> Future<T> submit(Callable<T> callable) {
        return getThreadPoolExecutor().submit(callable);
    }

    /**
     * 获取线程池对象
     *
     * @return
     */
    public static ThreadPoolExecutor getThreadPoolExecutor() {
        ThreadPoolExecutor threadPoolExecutor = threadPool.get();
        if (threadPoolExecutor == null) {
            synchronized (ThreadPoolUtils.class) {
                threadPoolExecutor = getNewThreadPool();
            }
        }
        threadPool.remove();
        return threadPoolExecutor;
    }

    public static ThreadPoolExecutor getNewThreadPool() {
        return new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE_TIME, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(BLOCK_QUEUE_SIZE), new ThreadPoolExecutor.CallerRunsPolicy());
    }
}
