package com.clark.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * Broken input stream. This stream always throws an {@link IOException} from
 * all the {@link InputStream} methods where the exception is declared.
 * <p>
 * This class is mostly useful for testing error handling in code that uses an
 * input stream.
 * 
 * @since Commons IO 2.0
 */
public class BrokenInputStream extends InputStream {

    /**
     * The exception that is thrown by all methods of this class.
     */
    private final IOException exception;

    /**
     * Creates a new stream that always throws the given exception.
     * 
     * @param exception
     *            the exception to be thrown
     */
    public BrokenInputStream(IOException exception) {
        this.exception = exception;
    }

    /**
     * Creates a new stream that always throws an {@link IOException}
     */
    public BrokenInputStream() {
        this(new IOException("Broken input stream"));
    }

    /**
     * Throws the configured exception.
     * 
     * @return nothing
     * @throws IOException
     *             always thrown
     */
    @Override
    public int read() throws IOException {
        throw exception;
    }

    /**
     * Throws the configured exception.
     * 
     * @return nothing
     * @throws IOException
     *             always thrown
     */
    @Override
    public int available() throws IOException {
        throw exception;
    }

    /**
     * Throws the configured exception.
     * 
     * @param n
     *            ignored
     * @return nothing
     * @throws IOException
     *             always thrown
     */
    @Override
    public long skip(long n) throws IOException {
        throw exception;
    }

    /**
     * Throws the configured exception.
     * 
     * @throws IOException
     *             always thrown
     */
    @Override
    public void reset() throws IOException {
        throw exception;
    }

    /**
     * Throws the configured exception.
     * 
     * @throws IOException
     *             always thrown
     */
    @Override
    public void close() throws IOException {
        throw exception;
    }

}
