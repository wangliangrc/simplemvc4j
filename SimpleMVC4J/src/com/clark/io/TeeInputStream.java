package com.clark.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * InputStream proxy that transparently writes a copy of all bytes read from the
 * proxied stream to a given OutputStream. Using {@link #skip(long)} or
 * {@link #mark(int)}/{@link #reset()} on the stream will result on some bytes
 * from the input stream being skipped or duplicated in the output stream.
 * <p>
 * The proxied input stream is closed when the {@link #close()} method is called
 * on this proxy. It is configurable whether the associated output stream will
 * also closed.
 * 
 * @version $Id: TeeInputStream.java 659817 2008-05-24 13:23:10Z niallp $
 * @since Commons IO 1.4
 */
public class TeeInputStream extends ProxyInputStream {

    /**
     * The output stream that will receive a copy of all bytes read from the
     * proxied input stream.
     */
    private final OutputStream branch;

    /**
     * Flag for closing also the associated output stream when this stream is
     * closed.
     */
    private final boolean closeBranch;

    /**
     * Creates a TeeInputStream that proxies the given {@link InputStream} and
     * copies all read bytes to the given {@link OutputStream}. The given output
     * stream will not be closed when this stream gets closed.
     * 
     * @param input
     *            input stream to be proxied
     * @param branch
     *            output stream that will receive a copy of all bytes read
     */
    public TeeInputStream(InputStream input, OutputStream branch) {
        this(input, branch, false);
    }

    /**
     * Creates a TeeInputStream that proxies the given {@link InputStream} and
     * copies all read bytes to the given {@link OutputStream}. The given output
     * stream will be closed when this stream gets closed if the closeBranch
     * parameter is <code>true</code>.
     * 
     * @param input
     *            input stream to be proxied
     * @param branch
     *            output stream that will receive a copy of all bytes read
     * @param closeBranch
     *            flag for closing also the output stream when this stream is
     *            closed
     */
    public TeeInputStream(InputStream input, OutputStream branch,
            boolean closeBranch) {
        super(input);
        this.branch = branch;
        this.closeBranch = closeBranch;
    }

    /**
     * Closes the proxied input stream and, if so configured, the associated
     * output stream. An exception thrown from one stream will not prevent
     * closing of the other stream.
     * 
     * @throws IOException
     *             if either of the streams could not be closed
     */
    @Override
    public void close() throws IOException {
        try {
            super.close();
        } finally {
            if (closeBranch) {
                branch.close();
            }
        }
    }

    /**
     * Reads a single byte from the proxied input stream and writes it to the
     * associated output stream.
     * 
     * @return next byte from the stream, or -1 if the stream has ended
     * @throws IOException
     *             if the stream could not be read (or written)
     */
    @Override
    public int read() throws IOException {
        int ch = super.read();
        if (ch != -1) {
            branch.write(ch);
        }
        return ch;
    }

    /**
     * Reads bytes from the proxied input stream and writes the read bytes to
     * the associated output stream.
     * 
     * @param bts
     *            byte buffer
     * @param st
     *            start offset within the buffer
     * @param end
     *            maximum number of bytes to read
     * @return number of bytes read, or -1 if the stream has ended
     * @throws IOException
     *             if the stream could not be read (or written)
     */
    @Override
    public int read(byte[] bts, int st, int end) throws IOException {
        int n = super.read(bts, st, end);
        if (n != -1) {
            branch.write(bts, st, n);
        }
        return n;
    }

    /**
     * Reads bytes from the proxied input stream and writes the read bytes to
     * the associated output stream.
     * 
     * @param bts
     *            byte buffer
     * @return number of bytes read, or -1 if the stream has ended
     * @throws IOException
     *             if the stream could not be read (or written)
     */
    @Override
    public int read(byte[] bts) throws IOException {
        int n = super.read(bts);
        if (n != -1) {
            branch.write(bts, 0, n);
        }
        return n;
    }

}
