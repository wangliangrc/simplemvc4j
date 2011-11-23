package com.clark.io;

import java.io.File;
import java.io.Serializable;

/**
 * A file filter that always returns false.
 * 
 * @since Commons IO 1.0
 * @version $Revision: 1005099 $ $Date: 2010-10-06 17:13:01 +0100 (Wed, 06 Oct
 *          2010) $
 * 
 * @author Stephen Colebourne
 * @see FileFilterUtils#falseFileFilter()
 */
public class FalseFileFilter implements IOFileFilter, Serializable {

    private static final long serialVersionUID = 2747906732402200058L;
    /**
     * Singleton instance of false filter.
     * 
     * @since Commons IO 1.3
     */
    public static final IOFileFilter FALSE = new FalseFileFilter();
    /**
     * Singleton instance of false filter. Please use the identical
     * FalseFileFilter.FALSE constant. The new name is more JDK 1.5 friendly as
     * it doesn't clash with other values when using static imports.
     */
    public static final IOFileFilter INSTANCE = FALSE;

    /**
     * Restrictive consructor.
     */
    protected FalseFileFilter() {
    }

    /**
     * Returns false.
     * 
     * @param file
     *            the file to check
     * @return false
     */
    public boolean accept(File file) {
        return false;
    }

    /**
     * Returns false.
     * 
     * @param dir
     *            the directory to check
     * @param name
     *            the filename
     * @return false
     */
    public boolean accept(File dir, String name) {
        return false;
    }

}
