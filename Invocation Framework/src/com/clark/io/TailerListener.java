package com.clark.io;

/**
 * Listener for events from a {@link Tailer}.
 * 
 * @version $Id: TailerListener.java 1002921 2010-09-30 01:33:38Z sebb $
 * @since Commons IO 2.0
 */
public interface TailerListener {

    /**
     * The tailer will call this method during construction, giving the listener
     * a method of stopping the tailer.
     * 
     * @param tailer
     *            the tailer.
     */
    public void init(Tailer tailer);

    /**
     * This method is called if the tailed file is not found.
     * <p>
     * <b>Note:</b> this is called from the tailer thread.
     */
    public void fileNotFound();

    /**
     * Called if a file rotation is detected.
     * 
     * This method is called before the file is reopened, and fileNotFound may
     * be called if the new file has not yet been created.
     * <p>
     * <b>Note:</b> this is called from the tailer thread.
     */
    public void fileRotated();

    /**
     * Handles a line from a Tailer.
     * <p>
     * <b>Note:</b> this is called from the tailer thread.
     * 
     * @param line
     *            the line.
     */
    public void handle(String line);

    /**
     * Handles an Exception .
     * <p>
     * <b>Note:</b> this is called from the tailer thread.
     * 
     * @param ex
     *            the exception.
     */
    public void handle(Exception ex);

}
