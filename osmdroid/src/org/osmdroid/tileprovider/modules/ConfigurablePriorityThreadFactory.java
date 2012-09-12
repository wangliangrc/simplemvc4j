package org.osmdroid.tileprovider.modules;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author Jastrzab
 */

public class ConfigurablePriorityThreadFactory implements ThreadFactory {

    private final int mPriority;
    private final String mName;
    private AtomicInteger id = new AtomicInteger(0);

    public ConfigurablePriorityThreadFactory(final int pPriority,
            final String pName) {
        mPriority = pPriority;
        mName = pName;
    }

    @Override
    public Thread newThread(final Runnable pRunnable) {
        final Thread thread = new Thread(pRunnable);
        thread.setPriority(mPriority);
        if (mName != null) {
            thread.setName(mName + id.incrementAndGet());
        }
        return thread;
    }

}
