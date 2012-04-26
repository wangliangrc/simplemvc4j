package com.clark.io;

import java.util.List;

/**
 * Defines operations for conditional file filters.
 * 
 * @since Commons IO 1.1
 * @version $Revision: 619103 $ $Date: 2008-02-06 19:01:17 +0000 (Wed, 06 Feb
 *          2008) $
 * 
 * @author Steven Caswell
 */
public interface ConditionalFileFilter {

    /**
     * Adds the specified file filter to the list of file filters at the end of
     * the list.
     * 
     * @param ioFileFilter
     *            the filter to be added
     * @since Commons IO 1.1
     */
    public void addFileFilter(IOFileFilter ioFileFilter);

    /**
     * Returns this conditional file filter's list of file filters.
     * 
     * @return the file filter list
     * @since Commons IO 1.1
     */
    public List<IOFileFilter> getFileFilters();

    /**
     * Removes the specified file filter.
     * 
     * @param ioFileFilter
     *            filter to be removed
     * @return <code>true</code> if the filter was found in the list,
     *         <code>false</code> otherwise
     * @since Commons IO 1.1
     */
    public boolean removeFileFilter(IOFileFilter ioFileFilter);

    /**
     * Sets the list of file filters, replacing any previously configured file
     * filters on this filter.
     * 
     * @param fileFilters
     *            the list of filters
     * @since Commons IO 1.1
     */
    public void setFileFilters(List<IOFileFilter> fileFilters);

}
