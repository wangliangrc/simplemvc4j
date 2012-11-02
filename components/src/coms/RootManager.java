package coms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import smvc.Subject;

public class RootManager extends Subject {
    private Process mRootProcess;
    private OutputStream mRootOutputStream;
    private volatile boolean rooted;
    private volatile boolean rooting;

    public static final String ROOT_SUCCESS = RootManager.class
            .getCanonicalName() + ".action.ROOT_SUCCESS";
    public static final String ROOT_FAIL = RootManager.class.getCanonicalName()
            + ".action.ROOT_FAIL";

    public static RootManager getInstance() {
        return Holder.sRootTask;
    }

    public void root() {
        if (!rooted && !rooting) {
            new Thread(new RootRunnable()).start();
        }
    }

    public boolean isRooted() {
        return rooted;
    }

    public void exec(String cmd) throws IOException, InterruptedException {
        if (rooted) {
            final CountDownLatch countDownLatch = new CountDownLatch(2);
            new InputStreamReaderThread(mRootProcess.getInputStream(),
                    countDownLatch).start();
            new InputStreamReaderThread(mRootProcess.getErrorStream(),
                    countDownLatch).start();

            mRootOutputStream.write((cmd + "\n").getBytes());
            mRootOutputStream.flush();

            countDownLatch.await();
        } else {
            throw new IllegalStateException(
                    "You should root first before calling exec()!");
        }

    }

    public void exec(String... cmd) throws IOException, InterruptedException {
        if (cmd != null && cmd.length > 0) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0, len = cmd.length; i < len; ++i) {
                if (i != 0) {
                    builder.append(" ");
                }
                builder.append(cmd[i]);
            }
            exec(builder.toString());
        } else {
            throw new IllegalArgumentException("cmd can't be null!");
        }

    }

    public void exec(List<String> cmd) throws IOException, InterruptedException {
        exec(cmd.toArray(new String[0]));
    }

    private RootManager() {
    }

    private class RootRunnable implements Runnable {

        @Override
        public void run() {
            rooting = true;

            final Runtime runtime = Runtime.getRuntime();
            if (runtime != null) {
                try {
                    mRootProcess = runtime.exec("su");
                    mRootOutputStream = mRootProcess.getOutputStream();
                    rooted = true;
                    success();
                } catch (Exception e) {
                    mRootProcess = null;
                    fail(e);
                }
            }

            rooting = false;
        }

    }

    private static class InputStreamReaderThread extends Thread {
        private InputStream mInputStream;
        private CountDownLatch mCountDownLatch;

        public InputStreamReaderThread(InputStream mInputStream,
                CountDownLatch mCountDownLatch) {
            this.mInputStream = mInputStream;
            this.mCountDownLatch = mCountDownLatch;
        }

        @Override
        public void run() {
            byte[] buf = new byte[512];
            int res = -1;
            try {
                while ((res = mInputStream.read(buf)) != -1) {
                    System.out.write(buf, 0, res);
                }
                System.out.flush();
            } catch (IOException e) {
            }

            mCountDownLatch.countDown();
        }
    }

    private static class Holder {
        static RootManager sRootTask = new RootManager();
    }

    private void fail(Throwable tr) {
        update(ROOT_FAIL, tr);
    }

    public void success() {
        update(ROOT_SUCCESS, null);
    }
}
