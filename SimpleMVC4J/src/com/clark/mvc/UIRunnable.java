package com.clark.mvc;

/**
 * 抽象接口，用于表示 UI 线程。
 * 
 * @author guangongbo
 * 
 */
public interface UIRunnable {

    /**
     * task 会运行在指定的 UI 线程中。
     * 
     * @param task
     */
    void runOnUIThread(Runnable task);

}
