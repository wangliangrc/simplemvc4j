package com.clark.mvc;

/**
 * 抽象接口，用于表示 UI 线程任务。
 * 
 * @author guangongbo
 * 
 */
public interface UIRunnable {
    /**
     * 返回调试用名称。
     * 
     * @return 返回调试用名称。
     */
    String getName();

    /**
     * 默认任务只能执行一次，所以该方法用于判断任务是否取消或者执行完毕。
     * 
     * @return 任务是否取消或者执行完毕。
     */
    boolean isClosed();

    /**
     * task 会运行在指定的 UI 线程中。
     * 
     * @param task
     */
    void runOnUIThread(Runnable task);

}
