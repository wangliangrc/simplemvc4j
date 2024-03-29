package com.clark.io;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Compare the <b>path</b> of two files for order (see {@link File#getPath()}).
 * <p>
 * This comparator can be used to sort lists or arrays of files by their path
 * either in a case-sensitive, case-insensitive or system dependant case
 * sensitive way. A number of singleton instances are provided for the various
 * case sensitivity options (using {@link IOCase}) and the reverse of those
 * options.
 * <p>
 * Example of a <i>case-sensitive</i> file path sort using the
 * {@link #PATH_COMPARATOR} singleton instance:
 * 
 * <pre>
 *       List&lt;File&gt; list = ...
 *       PathFileComparator.PATH_COMPARATOR.sort(list);
 * </pre>
 * <p>
 * Example of a <i>reverse case-insensitive</i> file path sort using the
 * {@link #PATH_INSENSITIVE_REVERSE} singleton instance:
 * 
 * <pre>
 *       File[] array = ...
 *       PathFileComparator.PATH_INSENSITIVE_REVERSE.sort(array);
 * </pre>
 * <p>
 * 
 * @version $Revision: 723942 $ $Date: 2008-12-06 01:24:06 +0000 (Sat, 06 Dec
 *          2008) $
 * @since Commons IO 1.4
 */
public class PathFileComparator extends AbstractFileComparator implements
        Serializable {

    private static final long serialVersionUID = 5713143396695840492L;

    /** Case-sensitive path comparator instance (see {@link IOCase#SENSITIVE}) */
    public static final Comparator<File> PATH_COMPARATOR = new PathFileComparator();

    /**
     * Reverse case-sensitive path comparator instance (see
     * {@link IOCase#SENSITIVE})
     */
    public static final Comparator<File> PATH_REVERSE = new ReverseComparator(
            PATH_COMPARATOR);

    /**
     * Case-insensitive path comparator instance (see {@link IOCase#INSENSITIVE}
     * )
     */
    public static final Comparator<File> PATH_INSENSITIVE_COMPARATOR = new PathFileComparator(
            IOCase.INSENSITIVE);

    /**
     * Reverse case-insensitive path comparator instance (see
     * {@link IOCase#INSENSITIVE})
     */
    public static final Comparator<File> PATH_INSENSITIVE_REVERSE = new ReverseComparator(
            PATH_INSENSITIVE_COMPARATOR);

    /** System sensitive path comparator instance (see {@link IOCase#SYSTEM}) */
    public static final Comparator<File> PATH_SYSTEM_COMPARATOR = new PathFileComparator(
            IOCase.SYSTEM);

    /**
     * Reverse system sensitive path comparator instance (see
     * {@link IOCase#SYSTEM})
     */
    public static final Comparator<File> PATH_SYSTEM_REVERSE = new ReverseComparator(
            PATH_SYSTEM_COMPARATOR);

    /** Whether the comparison is case sensitive. */
    private final IOCase caseSensitivity;

    /**
     * Construct a case sensitive file path comparator instance.
     */
    public PathFileComparator() {
        this.caseSensitivity = IOCase.SENSITIVE;
    }

    /**
     * Construct a file path comparator instance with the specified
     * case-sensitivity.
     * 
     * @param caseSensitivity
     *            how to handle case sensitivity, null means case-sensitive
     */
    public PathFileComparator(IOCase caseSensitivity) {
        this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE
                : caseSensitivity;
    }

    /**
     * Compare the paths of two files the specified case sensitivity.
     * 
     * @param file1
     *            The first file to compare
     * @param file2
     *            The second file to compare
     * @return a negative value if the first file's path is less than the
     *         second, zero if the paths are the same and a positive value if
     *         the first files path is greater than the second file.
     * 
     */
    public int compare(File file1, File file2) {
        return caseSensitivity.checkCompareTo(file1.getPath(), file2.getPath());
    }

    /**
     * String representation of this file comparator.
     * 
     * @return String representation of this file comparator
     */
    @Override
    public String toString() {
        return super.toString() + "[caseSensitivity=" + caseSensitivity + "]";
    }
}
