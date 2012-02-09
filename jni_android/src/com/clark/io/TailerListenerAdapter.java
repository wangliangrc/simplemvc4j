package com.clark.io;

/**
 * {@link TailerListener} Adapter.
 * 
 * @version $Id: TailerListenerAdapter.java 1002844 2010-09-29 20:56:23Z niallp
 *          $
 * @since Commons IO 2.0
 */
public class TailerListenerAdapter implements TailerListener {

    /**
     * The tailer will call this method during construction, giving the listener
     * a method of stopping the tailer.
     * 
     * @param tailer
     *            the tailer.
     */
    public void init(Tailer tailer) {
    }

    /**
     * This method is called if the tailed file is not found.
     */
    public void fileNotFound() {
    }

    /**
     * Called if a file rotation is detected.
     * 
     * This method is called before the file is reopened, and fileNotFound may
     * be called if the new file has not yet been created.
     */
    public void fileRotated() {
    }

    /**
     * Handles a line from a Tailer.
     * 
     * @param line
     *            the line.
     */
    public void handle(String line) {
    }

    /**
     * Handles an Exception .
     * 
     * @param ex
     *            the exception.
     */
    public void handle(Exception ex) {
    }

}
