package coms;

import java.util.Observable;

public class RootTask extends Observable {
    public static enum ResultType {
        OK, FAIL
    }

    private Process mRootProcess;
    private boolean rooted;

    public static RootTask getInstance() {
        return Holder.sRootTask;
    }

    public void root() {

    }

    private RootTask() {
    }

    private class RootRunnable implements Runnable {

        @Override
        public void run() {
            final Runtime runtime = Runtime.getRuntime();
            if (runtime == null) {
                return;
            }
            try {
                mRootProcess = runtime.exec("su");
                rooted = true;
            } catch (Exception e) {
                mRootProcess = null;
                fail();
            }
        }

    }

    private static class Holder {
        static RootTask sRootTask = new RootTask();
    }

    private void fail() {
        notifyObservers(ResultType.FAIL);
    }
}
