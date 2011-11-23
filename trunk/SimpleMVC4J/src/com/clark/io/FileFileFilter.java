package com.clark.io;

import java.io.File;
import java.io.Serializable;

/**
 * This filter accepts <code>File</code>s that are files (not directories).
 * <p>
 * For example, here is how to print out a list of the real files within the
 * current directory:
 * 
 * <pre>
 * File dir = new File(&quot;.&quot;);
 * String[] files = dir.list(FileFileFilter.FILE);
 * for (int i = 0; i &lt; files.length; i++) {
 *     System.out.println(files[i]);
 * }
 * </pre>
 * 
 * @since Commons IO 1.3
 * @version $Revision:587916 $ $Date: 2010-10-06 17:13:01 +0100 (Wed, 06 Oct
 *          2010) $
 * @see FileFilterUtils#fileFileFilter()
 */
public class FileFileFilter extends AbstractFileFilter implements Serializable {

    private static final long serialVersionUID = -8702522306678960442L;
    /** Singleton instance of file filter */
    public static final IOFileFilter FILE = new FileFileFilter();

    /**
     * Restrictive consructor.
     */
    protected FileFileFilter() {
    }

    /**
     * Checks to see if the file is a file.
     * 
     * @param file
     *            the File to check
     * @return true if the file is a file
     */
    @Override
    public boolean accept(File file) {
        return file.isFile();
    }

}
