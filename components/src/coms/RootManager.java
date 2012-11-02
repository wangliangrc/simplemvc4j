package coms;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import smvc.Subject;

public class RootManager extends Subject {
    private Process mRootProcess;
    private OutputStream mRootOutputStream;
    private boolean rooted;

    public static final String ROOT_SUCCESS = "ROOT_SUCCESS";
    public static final String ROOT_FAIL = "ROOT_FAIL";

    public static RootManager getInstance() {
        return Holder.sRootTask;
    }

    public void root() {
        if (!rooted) {
            new Thread(new RootRunnable()).start();
        }
    }

    public Process getRootProcess() {
        return mRootProcess;
    }

    public int exec(String cmd) throws IOException {
        if (mRootOutputStream != null) {
            mRootOutputStream.write((cmd + "\n").getBytes());
            mRootOutputStream.flush();
            return 0;
        }

        return -1;
    }

    public int exec(String... cmd) throws IOException {
        if (cmd != null && cmd.length > 0) {
            StringBuilder builder = new StringBuilder();
            for (int i = 0, len = cmd.length; i < len; ++i) {
                if (i != 0) {
                    builder.append(" ");
                }
                builder.append(cmd[i]);
            }
            return exec(builder.toString());
        }
        return -1;
    }

    public int exec(List<String> cmd) throws IOException {
        return exec(cmd.toArray(new String[0]));
    }

    private RootManager() {
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
                mRootOutputStream = mRootProcess.getOutputStream();
                rooted = true;
                success();
            } catch (Exception e) {
                mRootProcess = null;
                fail(e);
            }
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
