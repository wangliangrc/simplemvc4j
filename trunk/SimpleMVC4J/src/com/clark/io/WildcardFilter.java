package com.clark.io;

import static com.clark.func.Functions.wildcardMatch;

import java.io.File;
import java.io.Serializable;
import java.util.List;

import org.apache.commons.io.FilenameUtils;

/**
 * Filters files using the supplied wildcards.
 * <p>
 * This filter selects files, but not directories, based on one or more
 * wildcards and using case-sensitive comparison.
 * <p>
 * The wildcard matcher uses the characters '?' and '*' to represent a single or
 * multiple wildcard characters. This is the same as often found on Dos/Unix
 * command lines. The extension check is case-sensitive. See
 * {@link FilenameUtils#wildcardMatch(String, String)} for more information.
 * <p>
 * For example:
 * 
 * <pre>
 * File dir = new File(&quot;.&quot;);
 * FileFilter fileFilter = new WildcardFilter(&quot;*test*.java&tilde;*&tilde;&quot;);
 * File[] files = dir.listFiles(fileFilter);
 * for (int i = 0; i &lt; files.length; i++) {
 *     System.out.println(files[i]);
 * }
 * </pre>
 * 
 * @author Jason Anderson
 * @version $Revision: 1004077 $ $Date: 2010-10-04 01:58:42 +0100 (Mon, 04 Oct
 *          2010) $
 * @since Commons IO 1.1
 * @deprecated Use WilcardFileFilter. Deprecated as this class performs
 *             directory filtering which it shouldn't do, but that can't be
 *             removed due to compatability.
 */
@Deprecated
public class WildcardFilter extends AbstractFileFilter implements Serializable {

    private static final long serialVersionUID = -5260416163031217058L;
    /** The wildcards that will be used to match filenames. */
    private final String[] wildcards;

    /**
     * Construct a new case-sensitive wildcard filter for a single wildcard.
     * 
     * @param wildcard
     *            the wildcard to match
     * @throws IllegalArgumentException
     *             if the pattern is null
     */
    public WildcardFilter(String wildcard) {
        if (wildcard == null) {
            throw new IllegalArgumentException("The wildcard must not be null");
        }
        this.wildcards = new String[] { wildcard };
    }

    /**
     * Construct a new case-sensitive wildcard filter for an array of wildcards.
     * 
     * @param wildcards
     *            the array of wildcards to match
     * @throws IllegalArgumentException
     *             if the pattern array is null
     */
    public WildcardFilter(String[] wildcards) {
        if (wildcards == null) {
            throw new IllegalArgumentException(
                    "The wildcard array must not be null");
        }
        this.wildcards = new String[wildcards.length];
        System.arraycopy(wildcards, 0, this.wildcards, 0, wildcards.length);
    }

    /**
     * Construct a new case-sensitive wildcard filter for a list of wildcards.
     * 
     * @param wildcards
     *            the list of wildcards to match
     * @throws IllegalArgumentException
     *             if the pattern list is null
     * @throws ClassCastException
     *             if the list does not contain Strings
     */
    public WildcardFilter(List<String> wildcards) {
        if (wildcards == null) {
            throw new IllegalArgumentException(
                    "The wildcard list must not be null");
        }
        this.wildcards = wildcards.toArray(new String[wildcards.size()]);
    }

    // -----------------------------------------------------------------------
    /**
     * Checks to see if the filename matches one of the wildcards.
     * 
     * @param dir
     *            the file directory
     * @param name
     *            the filename
     * @return true if the filename matches one of the wildcards
     */
    @Override
    public boolean accept(File dir, String name) {
        if (dir != null && new File(dir, name).isDirectory()) {
            return false;
        }

        for (String wildcard : wildcards) {
            if (wildcardMatch(name, wildcard)) {
                return true;
            }
        }

        return false;
    }

    /**
     * Checks to see if the filename matches one of the wildcards.
     * 
     * @param file
     *            the file to check
     * @return true if the filename matches one of the wildcards
     */
    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return false;
        }

        for (String wildcard : wildcards) {
            if (wildcardMatch(file.getName(), wildcard)) {
                return true;
            }
        }

        return false;
    }

}
