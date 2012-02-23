package com.clark.io;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Data written to this stream is forwarded to a stream that has been associated
 * with this thread.
 * 
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision: 736890 $ $Date: 2009-01-23 02:02:22 +0000 (Fri, 23 Jan
 *          2009) $
 */
public class DemuxOutputStream extends OutputStream {
    private final InheritableThreadLocal<OutputStream> m_streams = new InheritableThreadLocal<OutputStream>();

    /**
     * Bind the specified stream to the current thread.
     * 
     * @param output
     *            the stream to bind
     * @return the OutputStream that was previously active
     */
    public OutputStream bindStream(OutputStream output) {
        OutputStream stream = m_streams.get();
        m_streams.set(output);
        return stream;
    }

    /**
     * Closes stream associated with current thread.
     * 
     * @throws IOException
     *             if an error occurs
     */
    @Override
    public void close() throws IOException {
        OutputStream output = m_streams.get();
        if (null != output) {
            output.close();
        }
    }

    /**
     * Flushes stream associated with current thread.
     * 
     * @throws IOException
     *             if an error occurs
     */
    @Override
    public void flush() throws IOException {
        OutputStream output = m_streams.get();
        if (null != output) {
            output.flush();
        }
    }

    /**
     * Writes byte to stream associated with current thread.
     * 
     * @param ch
     *            the byte to write to stream
     * @throws IOException
     *             if an error occurs
     */
    @Override
    public void write(int ch) throws IOException {
        OutputStream output = m_streams.get();
        if (null != output) {
            output.write(ch);
        }
    }
}
