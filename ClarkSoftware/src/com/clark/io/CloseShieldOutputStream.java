package com.clark.io;

import java.io.OutputStream;

/**
 * Proxy stream that prevents the underlying output stream from being closed.
 * <p>
 * This class is typically used in cases where an output stream needs to be
 * passed to a component that wants to explicitly close the stream even if other
 * components would still use the stream for output.
 * 
 * @version $Id: CloseShieldOutputStream.java 659817 2008-05-24 13:23:10Z niallp
 *          $
 * @since Commons IO 1.4
 */
public class CloseShieldOutputStream extends ProxyOutputStream {

    /**
     * Creates a proxy that shields the given output stream from being closed.
     * 
     * @param out
     *            underlying output stream
     */
    public CloseShieldOutputStream(OutputStream out) {
        super(out);
    }

    /**
     * Replaces the underlying output stream with a {@link ClosedOutputStream}
     * sentinel. The original output stream will remain open, but this proxy
     * will appear closed.
     */
    @Override
    public void close() {
        out = new ClosedOutputStream();
    }

}
