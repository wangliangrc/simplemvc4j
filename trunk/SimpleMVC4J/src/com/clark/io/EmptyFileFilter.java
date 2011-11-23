package com.clark.io;

import java.io.File;
import java.io.Serializable;

/**
 * This filter accepts files or directories that are empty.
 * <p>
 * If the <code>File</code> is a directory it checks that it contains no files.
 * <p>
 * Example, showing how to print out a list of the current directory's empty
 * files/directories:
 * 
 * <pre>
 * File dir = new File(&quot;.&quot;);
 * String[] files = dir.list(EmptyFileFilter.EMPTY);
 * for (int i = 0; i &lt; files.length; i++) {
 *     System.out.println(files[i]);
 * }
 * </pre>
 * 
 * <p>
 * Example, showing how to print out a list of the current directory's non-empty
 * files/directories:
 * 
 * <pre>
 * File dir = new File(&quot;.&quot;);
 * String[] files = dir.list(EmptyFileFilter.NOT_EMPTY);
 * for (int i = 0; i &lt; files.length; i++) {
 *     System.out.println(files[i]);
 * }
 * </pre>
 * 
 * @since Commons IO 1.3
 * @version $Revision: 659817 $
 */
public class EmptyFileFilter extends AbstractFileFilter implements Serializable {

    private static final long serialVersionUID = -5553208766021678527L;

    /** Singleton instance of <i>empty</i> filter */
    public static final IOFileFilter EMPTY = new EmptyFileFilter();

    /** Singleton instance of <i>not-empty</i> filter */
    public static final IOFileFilter NOT_EMPTY = new NotFileFilter(EMPTY);

    /**
     * Restrictive consructor.
     */
    protected EmptyFileFilter() {
    }

    /**
     * Checks to see if the file is empty.
     * 
     * @param file
     *            the file or directory to check
     * @return <code>true</code> if the file or directory is <i>empty</i>,
     *         otherwise <code>false</code>.
     */
    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            return (files == null || files.length == 0);
        } else {
            return (file.length() == 0);
        }
    }

}
