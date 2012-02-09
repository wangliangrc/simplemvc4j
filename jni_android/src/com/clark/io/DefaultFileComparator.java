package com.clark.io;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Compare two files using the <b>default</b> {@link File#compareTo(File)}
 * method.
 * <p>
 * This comparator can be used to sort lists or arrays of files by using the
 * default file comparison.
 * <p>
 * Example of sorting a list of files using the {@link #DEFAULT_COMPARATOR}
 * singleton instance:
 * 
 * <pre>
 *       List&lt;File&gt; list = ...
 *       DefaultFileComparator.DEFAULT_COMPARATOR.sort(list);
 * </pre>
 * <p>
 * Example of doing a <i>reverse</i> sort of an array of files using the
 * {@link #DEFAULT_REVERSE} singleton instance:
 * 
 * <pre>
 *       File[] array = ...
 *       DefaultFileComparator.DEFAULT_REVERSE.sort(array);
 * </pre>
 * <p>
 * 
 * @version $Revision: 721626 $ $Date: 2008-11-29 04:46:54 +0000 (Sat, 29 Nov
 *          2008) $
 * @since Commons IO 1.4
 */
public class DefaultFileComparator extends AbstractFileComparator implements
        Serializable {

    private static final long serialVersionUID = 2516859156294370894L;

    /** Singleton default comparator instance */
    public static final Comparator<File> DEFAULT_COMPARATOR = new DefaultFileComparator();

    /** Singleton reverse default comparator instance */
    public static final Comparator<File> DEFAULT_REVERSE = new ReverseComparator(
            DEFAULT_COMPARATOR);

    /**
     * Compare the two files using the {@link File#compareTo(File)} method.
     * 
     * @param file1
     *            The first file to compare
     * @param file2
     *            The second file to compare
     * @return the result of calling file1's {@link File#compareTo(File)} with
     *         file2 as the parameter.
     */
    public int compare(File file1, File file2) {
        return file1.compareTo(file2);
    }
}
