package smvc;

/**
 * 字符串的工具方法。
 * 
 * @author guangongbo
 *
 */
public class TX {

    /**
     * 如果字符串为 null或者 "" 则返回true，否则返回false。
     * 
     * @param txt
     * @return
     */
    public static boolean isEmpty(CharSequence txt) {
        return txt == null || txt.length() == 0;
    }

    /**
     * 如果字符串为 null或者 "" 则返回false，否则返回true。
     * 
     * @param txt
     * @return
     */
    public static boolean isNotEmpty(CharSequence txt) {
        return !isEmpty(txt);
    }

    /**
     * 去点字符串两边的空白。<br />
     * 注意：如果输入为 null则返回""。
     * 
     * @param txt
     * @return
     */
    public static String trim(CharSequence txt) {
        if (txt == null) {
            return "";
        } else {
            return txt.toString().trim();
        }
    }

    /**
     * 返回  trim(txt).length() == 0 的结果。
     * 
     * @param txt
     * @return
     */
    public static boolean isBlank(CharSequence txt) {
        return trim(txt).length() == 0;
    }

    /**
     * 返回  trim(txt).length() == 0 的相反结果。
     * 
     * @param txt
     * @return
     */
    public static boolean isNotBlank(CharSequence txt) {
        return !isBlank(txt);
    }
}
