package com.clark.io;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;

/**
 * An interface which brings the FileFilter and FilenameFilter interfaces
 * together.
 * 
 * @since Commons IO 1.0
 * @version $Revision: 471628 $ $Date: 2006-11-06 04:06:45 +0000 (Mon, 06 Nov
 *          2006) $
 * 
 * @author Stephen Colebourne
 */
public interface IOFileFilter extends FileFilter, FilenameFilter {

    /**
     * Checks to see if the File should be accepted by this filter.
     * <p>
     * Defined in {@link java.io.FileFilter}.
     * 
     * @param file
     *            the File to check
     * @return true if this file matches the test
     */
    public boolean accept(File file);

    /**
     * Checks to see if the File should be accepted by this filter.
     * <p>
     * Defined in {@link java.io.FilenameFilter}.
     * 
     * @param dir
     *            the directory File to check
     * @param name
     *            the filename within the directory to check
     * @return true if this file matches the test
     */
    public boolean accept(File dir, String name);

}
