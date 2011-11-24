package com.clark.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Data written to this stream is forwarded to a stream that has been associated
 * with this thread.
 * 
 * @author <a href="mailto:peter@apache.org">Peter Donald</a>
 * @version $Revision: 736890 $ $Date: 2009-01-23 02:02:22 +0000 (Fri, 23 Jan
 *          2009) $
 */
public class DemuxInputStream extends InputStream {
    private final InheritableThreadLocal<InputStream> m_streams = new InheritableThreadLocal<InputStream>();

    /**
     * Bind the specified stream to the current thread.
     * 
     * @param input
     *            the stream to bind
     * @return the InputStream that was previously active
     */
    public InputStream bindStream(InputStream input) {
        InputStream oldValue = m_streams.get();
        m_streams.set(input);
        return oldValue;
    }

    /**
     * Closes stream associated with current thread.
     * 
     * @throws IOException
     *             if an error occurs
     */
    @Override
    public void close() throws IOException {
        InputStream input = m_streams.get();
        if (null != input) {
            input.close();
        }
    }

    /**
     * Read byte from stream associated with current thread.
     * 
     * @return the byte read from stream
     * @throws IOException
     *             if an error occurs
     */
    @Override
    public int read() throws IOException {
        InputStream input = m_streams.get();
        if (null != input) {
            return input.read();
        } else {
            return -1;
        }
    }
}
