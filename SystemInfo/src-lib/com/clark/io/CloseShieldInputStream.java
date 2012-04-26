package com.clark.io;

import java.io.InputStream;

/**
 * Proxy stream that prevents the underlying input stream from being closed.
 * <p>
 * This class is typically used in cases where an input stream needs to be
 * passed to a component that wants to explicitly close the stream even if more
 * input would still be available to other components.
 * 
 * @version $Id: CloseShieldInputStream.java 659817 2008-05-24 13:23:10Z niallp
 *          $
 * @since Commons IO 1.4
 */
public class CloseShieldInputStream extends ProxyInputStream {

    /**
     * Creates a proxy that shields the given input stream from being closed.
     * 
     * @param in
     *            underlying input stream
     */
    public CloseShieldInputStream(InputStream in) {
        super(in);
    }

    /**
     * Replaces the underlying input stream with a {@link ClosedInputStream}
     * sentinel. The original input stream will remain open, but this proxy will
     * appear closed.
     */
    @Override
    public void close() {
        in = new ClosedInputStream();
    }

}
