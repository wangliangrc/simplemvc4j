package com.clark.lang;

import java.io.File;
import java.io.Serializable;

/**
 * This filter accepts <code>File</code>s that can be read.
 * <p>
 * Example, showing how to print out a list of the current directory's
 * <i>readable</i> files:
 * 
 * <pre>
 * File dir = new File(&quot;.&quot;);
 * String[] files = dir.list(CanReadFileFilter.CAN_READ);
 * for (int i = 0; i &lt; files.length; i++) {
 *     System.out.println(files[i]);
 * }
 * </pre>
 * 
 * <p>
 * Example, showing how to print out a list of the current directory's
 * <i>un-readable</i> files:
 * 
 * <pre>
 * File dir = new File(&quot;.&quot;);
 * String[] files = dir.list(CanReadFileFilter.CANNOT_READ);
 * for (int i = 0; i &lt; files.length; i++) {
 *     System.out.println(files[i]);
 * }
 * </pre>
 * 
 * <p>
 * Example, showing how to print out a list of the current directory's
 * <i>read-only</i> files:
 * 
 * <pre>
 * File dir = new File(&quot;.&quot;);
 * String[] files = dir.list(CanReadFileFilter.READ_ONLY);
 * for (int i = 0; i &lt; files.length; i++) {
 *     System.out.println(files[i]);
 * }
 * </pre>
 * 
 * @since Commons IO 1.3
 * @version $Revision: 659817 $
 */
public class CanReadFileFilter extends AbstractFileFilter implements
        Serializable {

    private static final long serialVersionUID = 3345188302981413239L;

    /** Singleton instance of <i>readable</i> filter */
    public static final IOFileFilter CAN_READ = new CanReadFileFilter();

    /** Singleton instance of not <i>readable</i> filter */
    public static final IOFileFilter CANNOT_READ = new NotFileFilter(CAN_READ);

    /** Singleton instance of <i>read-only</i> filter */
    public static final IOFileFilter READ_ONLY = new AndFileFilter(CAN_READ,
            CanWriteFileFilter.CANNOT_WRITE);

    /**
     * Restrictive consructor.
     */
    protected CanReadFileFilter() {
    }

    /**
     * Checks to see if the file can be read.
     * 
     * @param file
     *            the File to check.
     * @return <code>true</code> if the file can be read, otherwise
     *         <code>false</code>.
     */
    @Override
    public boolean accept(File file) {
        return file.canRead();
    }

}
