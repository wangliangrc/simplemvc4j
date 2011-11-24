package com.clark.io;

import java.io.File;
import java.io.Serializable;
import java.util.Comparator;

/**
 * Reverses the result of comparing two objects using the delegate
 * {@link Comparator}.
 * 
 * @version $Revision: 1021884 $ $Date: 2010-10-12 19:49:16 +0100 (Tue, 12 Oct
 *          2010) $
 * @since Commons IO 1.4
 */
class ReverseComparator extends AbstractFileComparator implements Serializable {

    private static final long serialVersionUID = -126790279246801337L;
    private final Comparator<File> delegate;

    /**
     * Construct an instance with the sepecified delegate {@link Comparator}.
     * 
     * @param delegate
     *            The comparator to delegate to
     */
    public ReverseComparator(Comparator<File> delegate) {
        if (delegate == null) {
            throw new IllegalArgumentException("Delegate comparator is missing");
        }
        this.delegate = delegate;
    }

    /**
     * Compare using the delegate Comparator, but reversing the result.
     * 
     * @param file1
     *            The first file to compare
     * @param file2
     *            The second file to compare
     * @return the result from the delegate
     *         {@link Comparator#compare(Object, Object)} reversing the value
     *         (i.e. positive becomes negative and vice versa)
     */
    public int compare(File file1, File file2) {
        return delegate.compare(file2, file1); // parameters switched round
    }

    /**
     * String representation of this file comparator.
     * 
     * @return String representation of this file comparator
     */
    @Override
    public String toString() {
        return super.toString() + "[" + delegate.toString() + "]";
    }

}
