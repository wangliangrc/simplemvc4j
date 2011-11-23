package com.clark.lang;

import java.io.File;
import java.io.Serializable;

/**
 * This filter accepts <code>File</code>s that are hidden.
 * <p>
 * Example, showing how to print out a list of the current directory's
 * <i>hidden</i> files:
 * 
 * <pre>
 * File dir = new File(&quot;.&quot;);
 * String[] files = dir.list(HiddenFileFilter.HIDDEN);
 * for (int i = 0; i &lt; files.length; i++) {
 *     System.out.println(files[i]);
 * }
 * </pre>
 * 
 * <p>
 * Example, showing how to print out a list of the current directory's
 * <i>visible</i> (i.e. not hidden) files:
 * 
 * <pre>
 * File dir = new File(&quot;.&quot;);
 * String[] files = dir.list(HiddenFileFilter.VISIBLE);
 * for (int i = 0; i &lt; files.length; i++) {
 *     System.out.println(files[i]);
 * }
 * </pre>
 * 
 * @since Commons IO 1.3
 * @version $Revision: 659817 $
 */
public class HiddenFileFilter extends AbstractFileFilter implements
        Serializable {

    private static final long serialVersionUID = 7309671563949345807L;

    /** Singleton instance of <i>hidden</i> filter */
    public static final IOFileFilter HIDDEN = new HiddenFileFilter();

    /** Singleton instance of <i>visible</i> filter */
    public static final IOFileFilter VISIBLE = new NotFileFilter(HIDDEN);

    /**
     * Restrictive consructor.
     */
    protected HiddenFileFilter() {
    }

    /**
     * Checks to see if the file is hidden.
     * 
     * @param file
     *            the File to check
     * @return <code>true</code> if the file is <i>hidden</i>, otherwise
     *         <code>false</code>.
     */
    @Override
    public boolean accept(File file) {
        return file.isHidden();
    }

}
