package com.clark.io;

import java.io.File;
import java.io.Serializable;

/**
 * This filter accepts <code>File</code>s that can be written to.
 * <p>
 * Example, showing how to print out a list of the current directory's
 * <i>writable</i> files:
 * 
 * <pre>
 * File dir = new File(&quot;.&quot;);
 * String[] files = dir.list(CanWriteFileFilter.CAN_WRITE);
 * for (int i = 0; i &lt; files.length; i++) {
 *     System.out.println(files[i]);
 * }
 * </pre>
 * 
 * <p>
 * Example, showing how to print out a list of the current directory's
 * <i>un-writable</i> files:
 * 
 * <pre>
 * File dir = new File(&quot;.&quot;);
 * String[] files = dir.list(CanWriteFileFilter.CANNOT_WRITE);
 * for (int i = 0; i &lt; files.length; i++) {
 *     System.out.println(files[i]);
 * }
 * </pre>
 * 
 * <p>
 * <b>N.B.</b> For read-only files, use <code>CanReadFileFilter.READ_ONLY</code>.
 * 
 * @since Commons IO 1.3
 * @version $Revision: 659817 $
 */
public class CanWriteFileFilter extends AbstractFileFilter implements
        Serializable {

    private static final long serialVersionUID = 6288194277911457880L;

    /** Singleton instance of <i>writable</i> filter */
    public static final IOFileFilter CAN_WRITE = new CanWriteFileFilter();

    /** Singleton instance of not <i>writable</i> filter */
    public static final IOFileFilter CANNOT_WRITE = new NotFileFilter(CAN_WRITE);

    /**
     * Restrictive consructor.
     */
    protected CanWriteFileFilter() {
    }

    /**
     * Checks to see if the file can be written to.
     * 
     * @param file
     *            the File to check
     * @return <code>true</code> if the file can be written to, otherwise
     *         <code>false</code>.
     */
    @Override
    public boolean accept(File file) {
        return file.canWrite();
    }

}
