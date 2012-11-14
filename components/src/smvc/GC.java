package smvc;

import java.io.Closeable;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

import smvc.animation.Animator;
import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Bitmap;

/**
 * 关闭资源的工具方法集合。
 * 
 * @author guangongbo
 * 
 */
public class GC {

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
            }
        }
    }

    public static void close(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    public static void close(Socket socket) {
        if (socket != null) {
            try {
                socket.close();
            } catch (IOException e) {
            }
        }
    }

    public static void close(ServerSocket serverSocket) {
        if (serverSocket != null) {
            try {
                serverSocket.close();
            } catch (IOException e) {
            }
        }
    }

    public static void shutdown(ExecutorService executorService) {
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    public static void recycle(Bitmap bitmap) {
        if (bitmap != null && !bitmap.isRecycled()) {
            bitmap.recycle();
        }
    }

    public static void dismiss(Dialog dialog) {
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
    }

    public static void cancel(AT<?, ?, ?> task) {
        if (task != null
                && task.getStatus().compareTo(AT.Status.PRE_FINISHED) < 0) {
            task.cancel(true);
        }
    }

    public static void cancel(Animator animator) {
        if (animator != null && animator.isStarted()) {
            animator.cancel();
        }
    }
}
