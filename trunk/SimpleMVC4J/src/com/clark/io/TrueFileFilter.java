package com.clark.io;

import java.io.File;
import java.io.Serializable;

import org.apache.commons.io.filefilter.FileFilterUtils;

/**
 * A file filter that always returns true.
 * 
 * @since Commons IO 1.0
 * @version $Revision: 1005099 $ $Date: 2010-10-06 17:13:01 +0100 (Wed, 06 Oct
 *          2010) $
 * 
 * @author Stephen Colebourne
 * @see FileFilterUtils#trueFileFilter()
 */
public class TrueFileFilter implements IOFileFilter, Serializable {

    private static final long serialVersionUID = 5618533957037878556L;
    /**
     * Singleton instance of true filter.
     * 
     * @since Commons IO 1.3
     */
    public static final IOFileFilter TRUE = new TrueFileFilter();
    /**
     * Singleton instance of true filter. Please use the identical
     * TrueFileFilter.TRUE constant. The new name is more JDK 1.5 friendly as it
     * doesn't clash with other values when using static imports.
     */
    public static final IOFileFilter INSTANCE = TRUE;

    /**
     * Restrictive consructor.
     */
    protected TrueFileFilter() {
    }

    /**
     * Returns true.
     * 
     * @param file
     *            the file to check
     * @return true
     */
    public boolean accept(File file) {
        return true;
    }

    /**
     * Returns true.
     * 
     * @param dir
     *            the directory to check
     * @param name
     *            the filename
     * @return true
     */
    public boolean accept(File dir, String name) {
        return true;
    }

}
