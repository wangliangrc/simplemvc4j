package com.clark.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Closed output stream. This stream throws an exception on all attempts to
 * write something to the stream.
 * <p>
 * Typically uses of this class include testing for corner cases in methods that
 * accept an output stream and acting as a sentinel value instead of a
 * <code>null</code> output stream.
 * 
 * @version $Id: ClosedOutputStream.java 659817 2008-05-24 13:23:10Z niallp $
 * @since Commons IO 1.4
 */
public class ClosedOutputStream extends OutputStream {

    /**
     * A singleton.
     */
    public static final ClosedOutputStream CLOSED_OUTPUT_STREAM = new ClosedOutputStream();

    /**
     * Throws an {@link IOException} to indicate that the stream is closed.
     * 
     * @param b
     *            ignored
     * @throws IOException
     *             always thrown
     */
    @Override
    public void write(int b) throws IOException {
        throw new IOException("write(" + b + ") failed: stream is closed");
    }

}
