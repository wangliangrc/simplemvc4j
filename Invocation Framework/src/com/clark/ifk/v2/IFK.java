package com.clark.ifk.v2;

public abstract class IFK {
    public static final int MAX_PRIORITY = 0;
    public static final int DEFAULT_PRIORITY = 50;
    public static final int MIN_PRIORITY = 100;

    public static final int THREAD_STRATEGY_UI = 1;
    public static final int THREAD_STRATEGY_BACKGROUND = 2;
    public static final int THREAD_STRATEGY_SYNCHRONOUS = 3;

    protected int validatePriority(int p) {
        if (p < MAX_PRIORITY) {
            p = MAX_PRIORITY;
        } else if (p > MIN_PRIORITY) {
            p = MIN_PRIORITY;
        }

        return p;
    }

    protected int validateThreadStrategy(int ts) {
        if (ts < THREAD_STRATEGY_UI && ts > THREAD_STRATEGY_SYNCHRONOUS) {
            ts = THREAD_STRATEGY_UI;
        }
        return ts;
    }
}
