package com.clark.io;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Compare the <b>names</b> of two files for order (see {@link File#getName()}).
 * <p>
 * This comparator can be used to sort lists or arrays of files by their name
 * either in a case-sensitive, case-insensitive or system dependant case
 * sensitive way. A number of singleton instances are provided for the various
 * case sensitivity options (using {@link IOCase}) and the reverse of those
 * options.
 * <p>
 * Example of a <i>case-sensitive</i> file name sort using the
 * {@link #NAME_COMPARATOR} singleton instance:
 * 
 * <pre>
 *       List&lt;File&gt; list = ...
 *       NameFileComparator.NAME_COMPARATOR.sort(list);
 * </pre>
 * <p>
 * Example of a <i>reverse case-insensitive</i> file name sort using the
 * {@link #NAME_INSENSITIVE_REVERSE} singleton instance:
 * 
 * <pre>
 *       File[] array = ...
 *       NameFileComparator.NAME_INSENSITIVE_REVERSE.sort(array);
 * </pre>
 * <p>
 * 
 * @version $Revision: 723942 $ $Date: 2008-12-06 01:24:06 +0000 (Sat, 06 Dec
 *          2008) $
 * @since Commons IO 1.4
 */
public class NameFileComparator extends AbstractFileComparator implements
        Serializable {

    private static final long serialVersionUID = -3139132056158636471L;

    /** Case-sensitive name comparator instance (see {@link IOCase#SENSITIVE}) */
    public static final Comparator<File> NAME_COMPARATOR = new NameFileComparator();

    /**
     * Reverse case-sensitive name comparator instance (see
     * {@link IOCase#SENSITIVE})
     */
    public static final Comparator<File> NAME_REVERSE = new ReverseComparator(
            NAME_COMPARATOR);

    /**
     * Case-insensitive name comparator instance (see {@link IOCase#INSENSITIVE}
     * )
     */
    public static final Comparator<File> NAME_INSENSITIVE_COMPARATOR = new NameFileComparator(
            IOCase.INSENSITIVE);

    /**
     * Reverse case-insensitive name comparator instance (see
     * {@link IOCase#INSENSITIVE})
     */
    public static final Comparator<File> NAME_INSENSITIVE_REVERSE = new ReverseComparator(
            NAME_INSENSITIVE_COMPARATOR);

    /** System sensitive name comparator instance (see {@link IOCase#SYSTEM}) */
    public static final Comparator<File> NAME_SYSTEM_COMPARATOR = new NameFileComparator(
            IOCase.SYSTEM);

    /**
     * Reverse system sensitive name comparator instance (see
     * {@link IOCase#SYSTEM})
     */
    public static final Comparator<File> NAME_SYSTEM_REVERSE = new ReverseComparator(
            NAME_SYSTEM_COMPARATOR);

    /** Whether the comparison is case sensitive. */
    private final IOCase caseSensitivity;

    /**
     * Construct a case sensitive file name comparator instance.
     */
    public NameFileComparator() {
        this.caseSensitivity = IOCase.SENSITIVE;
    }

    /**
     * Construct a file name comparator instance with the specified
     * case-sensitivity.
     * 
     * @param caseSensitivity
     *            how to handle case sensitivity, null means case-sensitive
     */
    public NameFileComparator(IOCase caseSensitivity) {
        this.caseSensitivity = caseSensitivity == null ? IOCase.SENSITIVE
                : caseSensitivity;
    }

    /**
     * Compare the names of two files with the specified case sensitivity.
     * 
     * @param file1
     *            The first file to compare
     * @param file2
     *            The second file to compare
     * @return a negative value if the first file's name is less than the
     *         second, zero if the names are the same and a positive value if
     *         the first files name is greater than the second file.
     */
    public int compare(File file1, File file2) {
        return caseSensitivity.checkCompareTo(file1.getName(), file2.getName());
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
