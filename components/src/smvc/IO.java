package smvc;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;

import android.text.TextUtils;

/**
 * 输入输出的相关工具方法。
 * 
 * @author guangongbo
 *
 */
public class IO {

    /**
     * 将输入流中的内容拷贝到输出流中。<br />
     * 注意：本方法没有关闭输入流或者输出流。
     * 
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    public static void copy(InputStream inputStream, OutputStream outputStream)
            throws IOException {
        if (!(inputStream instanceof BufferedInputStream)) {
            inputStream = new BufferedInputStream(inputStream);
        }
        if (!(outputStream instanceof BufferedOutputStream)) {
            outputStream = new BufferedOutputStream(outputStream);
        }
        int op = -1;
        byte[] buf = new byte[512];
        while ((op = inputStream.read(buf)) != -1) {
            outputStream.write(buf, 0, op);
        }
        outputStream.flush();
    }

    /**
     * 将输入流中的内容拷贝到输出流中并关闭输入流和输出流。
     * 
     * @param inputStream
     * @param outputStream
     * @throws IOException
     */
    public static void copyAndClose(InputStream inputStream,
            OutputStream outputStream) throws IOException {
        try {
            copy(inputStream, outputStream);
        } finally {
            GC.close(inputStream);
            GC.close(outputStream);
        }
    }

    /**
     * 将输入流转为字节数组。
     * 
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(InputStream inputStream)
            throws IOException {
        if (!(inputStream instanceof BufferedInputStream)) {
            inputStream = new BufferedInputStream(inputStream);
        }
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream(
                512);
        copyAndClose(inputStream, outputStream);
        return outputStream.toByteArray();
    }

    /**
     * 将文件内容转为字节数组。
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(File file) throws IOException {
        return toByteArray(new FileInputStream(file));
    }

    /**
     * 将文件内容转为字节数组。
     * 
     * @param path
     * @return
     * @throws IOException
     */
    public static byte[] toByteArray(String path) throws IOException {
        return toByteArray(new File(path));
    }

    /**
     * 将输入流转为系统默认字符集的字符串。
     * 
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static String toString(InputStream inputStream) throws IOException {
        return new String(toByteArray(inputStream));
    }

    /**
     * 将文件内容转为系统默认字符集的字符串。
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public static String toString(File file) throws IOException {
        return toString(new FileInputStream(file));
    }

    /**
     * 将文件内容转为系统默认字符集的字符串。
     * 
     * @param file
     * @return
     * @throws IOException
     */
    public static String toString(String path) throws IOException {
        return toString(new File(path));
    }

    /**
     * 将输入流转为指定字符集的字符串。
     * 
     * @param inputStream
     * @param charsetName
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static String toString(InputStream inputStream, String charsetName)
            throws IOException {
        return new String(toByteArray(inputStream), charsetName);
    }

    /**
     * 将文件内容转为指定字符集的字符串。
     * 
     * @param inputStream
     * @param charsetName
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static String toString(File file, String charsetName)
            throws IOException {
        return toString(new FileInputStream(file), charsetName);
    }

    /**
     * 将文件内容转为指定字符集的字符串。
     * 
     * @param inputStream
     * @param charsetName
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static String toString(String path, String charsetName)
            throws IOException {
        return toString(new File(path), charsetName);
    }

    /**
     * 将输入流转为 UTF-8 字符串。<br />
     * 注意：调用本方法的时候必须保证系统支持 UTF-8 字符集编码。
     * 
     * @param inputStream
     * @return
     * @throws IOException
     * @throws RuntimeException 如果系统不支持UTF-8字符集
     */
    public static String toUTF8String(InputStream inputStream)
            throws IOException {
        try {
            return toString(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Unsupported UTF-8 charset!");
        }
    }

    /**
     * 将文件内容转为 UTF-8 字符串。<br />
     * 注意：调用本方法的时候必须保证系统支持 UTF-8 字符集编码。
     * 
     * @param inputStream
     * @return
     * @throws IOException
     * @throws RuntimeException 如果系统不支持UTF-8字符集
     */
    public static String toUTF8String(File file) throws IOException {
        return toUTF8String(new FileInputStream(file));
    }

    /**
     * 将文件内容转为 UTF-8 字符串。<br />
     * 注意：调用本方法的时候必须保证系统支持 UTF-8 字符集编码。
     * 
     * @param inputStream
     * @return
     * @throws IOException
     * @throws RuntimeException 如果系统不支持UTF-8字符集
     */
    public static String toUTF8String(String path) throws IOException {
        return toUTF8String(new File(path));
    }

    /**
     * 将输入流转为 File 对象。如果文件已经存在会被覆盖。
     * 
     * @param inputStream
     * @param destFilePath 指定输出文件的位置
     * @return
     * @throws IOException
     */
    public static File toFile(InputStream inputStream, String destFilePath)
            throws IOException {
        File aFile = new File(destFilePath);
        File parent = aFile.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("Can't create dir "
                    + parent.getAbsolutePath());
        }
        copyAndClose(inputStream, new FileOutputStream(aFile));
        return aFile;
    }

    /**
     * 将输入流转为 File 对象。可以追加内容。
     * 
     * @param inputStream
     * @param destFilePath
     * @return
     * @throws IOException
     */
    public static File appendFile(InputStream inputStream, String destFilePath)
            throws IOException {
        File aFile = new File(destFilePath);
        File parent = aFile.getParentFile();
        if (parent != null && !parent.exists() && !parent.mkdirs()) {
            throw new IOException("Can't create dir "
                    + parent.getAbsolutePath());
        }
        copyAndClose(inputStream, new FileOutputStream(aFile, true));
        return aFile;
    }

    /**
     * 判断指定路径文件是否存在
     * 
     * @param path
     * @return
     */
    public static boolean exist(String path) {
        return !TextUtils.isEmpty(path) && new File(path).exists();
    }

    /**
     * 判断指定路径文件是否存在
     * 
     * @param file
     * @return
     */
    public static boolean exist(File file) {
        return file != null && file.exists();
    }

    /**
     * 判断指定路径文件是否存在
     * 
     * @param parent
     * @param name
     * @return
     */
    public static boolean exist(File parent, String name) {
        return parent != null && !TextUtils.isEmpty(name)
                && exist(new File(parent, name));
    }

    /**
     * 判断指定路径文件是否存在
     * 
     * @param parent
     * @param name
     * @return
     */
    public static boolean exist(String parent, String name) {
        return !TextUtils.isEmpty(name) && !TextUtils.isEmpty(parent)
                && exist(new File(parent, name));
    }
}
