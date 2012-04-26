package com.clark.io;

import java.io.File;

/**
 * Convenience {@link FileAlterationListener} implementation that does nothing.
 * 
 * @see FileAlterationObserver
 * @version $Id: FileAlterationListenerAdaptor.java 1022311 2010-10-13 22:08:56Z
 *          niallp $
 * @since Commons IO 2.0
 */
public class FileAlterationListenerAdaptor implements FileAlterationListener {

    /**
     * File system observer started checking event.
     * 
     * @param observer
     *            The file system observer
     */
    public void onStart(final FileAlterationObserver observer) {
    }

    /**
     * Directory created Event.
     * 
     * @param directory
     *            The directory created
     */
    public void onDirectoryCreate(final File directory) {
    }

    /**
     * Directory changed Event.
     * 
     * @param directory
     *            The directory changed
     */
    public void onDirectoryChange(final File directory) {
    }

    /**
     * Directory deleted Event.
     * 
     * @param directory
     *            The directory deleted
     */
    public void onDirectoryDelete(final File directory) {
    }

    /**
     * File created Event.
     * 
     * @param file
     *            The file created
     */
    public void onFileCreate(final File file) {
    }

    /**
     * File changed Event.
     * 
     * @param file
     *            The file changed
     */
    public void onFileChange(final File file) {
    }

    /**
     * File deleted Event.
     * 
     * @param file
     *            The file deleted
     */
    public void onFileDelete(final File file) {
    }

    /**
     * File system observer finished checking event.
     * 
     * @param observer
     *            The file system observer
     */
    public void onStop(final FileAlterationObserver observer) {
    }

}
