package coms;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import smvc.Subject;
import android.util.Log;

public class RootManager extends Subject {
    static final String TAG = RootManager.class.getSimpleName();

    private Process mRootProcess;
    private OutputStream mRootOutputStream;
    private volatile boolean rooted;
    private volatile boolean rooting;

    public static final String ROOT_SUCCESS = "coms.RootManager.action.ROOT_SUCCESS";
    public static final String ROOT_FAIL = "coms.RootManager.action.ROOT_FAIL";

    public static RootManager getInstance() {
        return Holder.sRootTask;
    }

    public void root() {
        if (!rooted && !rooting) {
            new Thread(new RootRunnable()).start();
        }
    }

    public void destroy() {
        if(mRootProcess != null) {
            mRootProcess.destroy();
        }
        mRootOutputStream = null;
        rooted = false;
        rooting = false;
    }

    public boolean isRooted() {
        return rooted;
    }

    public synchronized void exec(String cmd) throws IOException,
            InterruptedException {
        if (rooted) {
            if (!cmd.endsWith("\n")) {
                cmd += "\n";
            }

            Log.i(TAG, "[shell]" + cmd);

            mRootOutputStream.write(cmd.getBytes());
            mRootOutputStream.flush();
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
                    new InputStreamReaderThread(mRootProcess.getInputStream())
                            .start();
                    new InputStreamReaderThread(mRootProcess.getErrorStream())
                            .start();
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

        public InputStreamReaderThread(InputStream mInputStream) {
            this.mInputStream = mInputStream;
        }

        @Override
        public void run() {
            Log.i(TAG, "InputStreamReaderThread(" + getId() + ") started!");

            byte[] buf = new byte[512];
            int res = -1;
            try {
                while (true) {
                    res = mInputStream.read(buf);
                    if (res == -1) {
                        break;
                    } else {
                        System.out.write(buf, 0, res);
                        System.out.flush();
                    }
                }
            } catch (IOException e) {
                Log.e(TAG, "", e);
            }

            Log.i(TAG, "InputStreamReaderThread(" + getId() + ") finished!");
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
