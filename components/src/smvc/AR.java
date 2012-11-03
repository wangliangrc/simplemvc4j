package smvc;

/**
 * 数组的工具方法
 * 
 * @author guangongbo
 *
 */
public class AR {
    public static final byte[] EMPTY_BYTE_ARRAY = new byte[0];
    public static final short[] EMPTY_SHORT_ARRAY = new short[0];
    public static final char[] EMPTY_CHAR_ARRAY = new char[0];
    public static final int[] EMPTY_INT_ARRAY = new int[0];
    public static final long[] EMPTY_LONG_ARRAY = new long[0];
    public static final float[] EMPTY_FLOAT_ARRAY = new float[0];
    public static final double[] EMPTY_DOUBLE_ARRAY = new double[0];

    public static boolean isEmpty(byte[] bs) {
        return bs == null || bs.length == 0;
    }

    public static boolean isNotEmpty(byte[] bs) {
        return !isEmpty(bs);
    }

    public static boolean isEmpty(short[] s) {
        return s == null || s.length == 0;
    }

    public static boolean isNotEmpty(short[] s) {
        return !isEmpty(s);
    }

    public static boolean isEmpty(char[] cs) {
        return cs == null || cs.length == 0;
    }

    public static boolean isNotEmpty(char[] cs) {
        return !isEmpty(cs);
    }

    public static boolean isEmpty(int[] is) {
        return is == null || is.length == 0;
    }

    public static boolean isNotEmpty(int[] is) {
        return !isEmpty(is);
    }

    public static boolean isEmpty(long[] ls) {
        return ls == null || ls.length == 0;
    }

    public static boolean isNotEmpty(long[] ls) {
        return !isEmpty(ls);
    }

    public static boolean isEmpty(float[] fs) {
        return fs == null || fs.length == 0;
    }

    public static boolean isNotEmpty(float[] fs) {
        return !isEmpty(fs);
    }

    public static boolean isEmpty(double[] ds) {
        return ds == null || ds.length == 0;
    }

    public static boolean isNotEmpty(double[] ds) {
        return !isEmpty(ds);
    }

    public static boolean isEmpty(Object[] objects) {
        return objects == null || objects.length == 0;
    }

    public static boolean isNotEmpty(Object[] objects) {
        return !isEmpty(objects);
    }
}
