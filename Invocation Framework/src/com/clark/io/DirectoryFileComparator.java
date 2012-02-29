package com.clark.io;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Compare two files using the {@link File#isDirectory()} method.
 * <p>
 * This comparator can be used to sort lists or arrays by directories and files.
 * <p>
 * Example of sorting a list of files/directories using the
 * {@link #DIRECTORY_COMPARATOR} singleton instance:
 * 
 * <pre>
 *       List&lt;File&gt; list = ...
 *       DirectoryFileComparator.DIRECTORY_COMPARATOR.sort(list);
 * </pre>
 * <p>
 * Example of doing a <i>reverse</i> sort of an array of files/directories using
 * the {@link #DIRECTORY_REVERSE} singleton instance:
 * 
 * <pre>
 *       File[] array = ...
 *       DirectoryFileComparator.DIRECTORY_REVERSE.sort(array);
 * </pre>
 * <p>
 * 
 * @version $Revision$ $Date$
 * @since Commons IO 2.0
 */
public class DirectoryFileComparator extends AbstractFileComparator implements
        Serializable {

    private static final long serialVersionUID = -302804300068924450L;

    /** Singleton default comparator instance */
    public static final Comparator<File> DIRECTORY_COMPARATOR = new DirectoryFileComparator();

    /** Singleton reverse default comparator instance */
    public static final Comparator<File> DIRECTORY_REVERSE = new ReverseComparator(
            DIRECTORY_COMPARATOR);

    /**
     * Compare the two files using the {@link File#isDirectory()} method.
     * 
     * @param file1
     *            The first file to compare
     * @param file2
     *            The second file to compare
     * @return the result of calling file1's {@link File#compareTo(File)} with
     *         file2 as the parameter.
     */
    public int compare(File file1, File file2) {
        return (getType(file1) - getType(file2));
    }

    /**
     * Convert type to numeric value.
     * 
     * @param file
     *            The file
     * @return 1 for directories and 2 for files
     */
    private int getType(File file) {
        if (file.isDirectory()) {
            return 1;
        } else {
            return 2;
        }
    }
}
