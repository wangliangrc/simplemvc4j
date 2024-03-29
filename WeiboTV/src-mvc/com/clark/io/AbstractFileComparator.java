package com.clark.io;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Abstract file {@link Comparator} which provides sorting for file arrays and
 * lists.
 * 
 * @version $Revision: 1003647 $ $Date: 2010-10-01 21:53:59 +0100 (Fri, 01 Oct
 *          2010) $
 * @since Commons IO 2.0
 */
abstract class AbstractFileComparator implements Comparator<File> {

    /**
     * Sort an array of files.
     * <p>
     * This method uses {@link Arrays#sort(Object[], Comparator)} and returns
     * the original array.
     * 
     * @param files
     *            The files to sort, may be null
     * @return The sorted array
     * @since Commons IO 2.0
     */
    public File[] sort(File... files) {
        if (files != null) {
            Arrays.sort(files, this);
        }
        return files;
    }

    /**
     * Sort a List of files.
     * <p>
     * This method uses {@link Collections#sort(List, Comparator)} and returns
     * the original list.
     * 
     * @param files
     *            The files to sort, may be null
     * @return The sorted list
     * @since Commons IO 2.0
     */
    public List<File> sort(List<File> files) {
        if (files != null) {
            Collections.sort(files, this);
        }
        return files;
    }

    /**
     * String representation of this file comparator.
     * 
     * @return String representation of this file comparator
     */
    @Override
    public String toString() {
        return getClass().getSimpleName();
    }
}
