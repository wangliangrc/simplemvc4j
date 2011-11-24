package com.clark.io;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Compare the <b>last modified date/time</b> of two files for order (see
 * {@link File#lastModified()}).
 * <p>
 * This comparator can be used to sort lists or arrays of files by their last
 * modified date/time.
 * <p>
 * Example of sorting a list of files using the {@link #LASTMODIFIED_COMPARATOR}
 * singleton instance:
 * 
 * <pre>
 *       List&lt;File&gt; list = ...
 *       LastModifiedFileComparator.LASTMODIFIED_COMPARATOR.sort(list);
 * </pre>
 * <p>
 * Example of doing a <i>reverse</i> sort of an array of files using the
 * {@link #LASTMODIFIED_REVERSE} singleton instance:
 * 
 * <pre>
 *       File[] array = ...
 *       LastModifiedFileComparator.LASTMODIFIED_REVERSE.sort(array);
 * </pre>
 * <p>
 * 
 * @version $Revision: 721626 $ $Date: 2008-11-29 04:46:54 +0000 (Sat, 29 Nov
 *          2008) $
 * @since Commons IO 1.4
 */
public class LastModifiedFileComparator extends AbstractFileComparator
        implements Serializable {

    private static final long serialVersionUID = 2835434934449458459L;

    /** Last modified comparator instance */
    public static final Comparator<File> LASTMODIFIED_COMPARATOR = new LastModifiedFileComparator();

    /** Reverse last modified comparator instance */
    public static final Comparator<File> LASTMODIFIED_REVERSE = new ReverseComparator(
            LASTMODIFIED_COMPARATOR);

    /**
     * Compare the last the last modified date/time of two files.
     * 
     * @param file1
     *            The first file to compare
     * @param file2
     *            The second file to compare
     * @return a negative value if the first file's lastmodified date/time is
     *         less than the second, zero if the lastmodified date/time are the
     *         same and a positive value if the first files lastmodified
     *         date/time is greater than the second file.
     * 
     */
    public int compare(File file1, File file2) {
        long result = file1.lastModified() - file2.lastModified();
        if (result < 0) {
            return -1;
        } else if (result > 0) {
            return 1;
        } else {
            return 0;
        }
    }
}
