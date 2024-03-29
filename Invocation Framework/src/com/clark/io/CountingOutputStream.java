package com.clark.io;

import java.io.OutputStream;

/**
 * A decorating output stream that counts the number of bytes that have passed
 * through the stream so far.
 * <p>
 * A typical use case would be during debugging, to ensure that data is being
 * written as expected.
 * 
 * @version $Id: CountingOutputStream.java 1004079 2010-10-04 01:11:27Z niallp $
 */
public class CountingOutputStream extends ProxyOutputStream {

    /** The count of bytes that have passed. */
    private long count = 0;

    /**
     * Constructs a new CountingOutputStream.
     * 
     * @param out
     *            the OutputStream to write to
     */
    public CountingOutputStream(OutputStream out) {
        super(out);
    }

    // -----------------------------------------------------------------------

    /**
     * Updates the count with the number of bytes that are being written.
     * 
     * @param n
     *            number of bytes to be written to the stream
     * @since Commons IO 2.0
     */
    @Override
    protected synchronized void beforeWrite(int n) {
        count += n;
    }

    // -----------------------------------------------------------------------
    /**
     * The number of bytes that have passed through this stream.
     * <p>
     * NOTE: From v1.3 this method throws an ArithmeticException if the count is
     * greater than can be expressed by an <code>int</code>. See
     * {@link #getByteCount()} for a method using a <code>long</code>.
     * 
     * @return the number of bytes accumulated
     * @throws ArithmeticException
     *             if the byte count is too large
     */
    public int getCount() {
        long result = getByteCount();
        if (result > Integer.MAX_VALUE) {
            throw new ArithmeticException("The byte count " + result
                    + " is too large to be converted to an int");
        }
        return (int) result;
    }

    /**
     * Set the byte count back to 0.
     * <p>
     * NOTE: From v1.3 this method throws an ArithmeticException if the count is
     * greater than can be expressed by an <code>int</code>. See
     * {@link #resetByteCount()} for a method using a <code>long</code>.
     * 
     * @return the count previous to resetting
     * @throws ArithmeticException
     *             if the byte count is too large
     */
    public int resetCount() {
        long result = resetByteCount();
        if (result > Integer.MAX_VALUE) {
            throw new ArithmeticException("The byte count " + result
                    + " is too large to be converted to an int");
        }
        return (int) result;
    }

    /**
     * The number of bytes that have passed through this stream.
     * <p>
     * NOTE: This method is an alternative for <code>getCount()</code>. It was
     * added because that method returns an integer which will result in
     * incorrect count for files over 2GB.
     * 
     * @return the number of bytes accumulated
     * @since Commons IO 1.3
     */
    public synchronized long getByteCount() {
        return this.count;
    }

    /**
     * Set the byte count back to 0.
     * <p>
     * NOTE: This method is an alternative for <code>resetCount()</code>. It was
     * added because that method returns an integer which will result in
     * incorrect count for files over 2GB.
     * 
     * @return the count previous to resetting
     * @since Commons IO 1.3
     */
    public synchronized long resetByteCount() {
        long tmp = this.count;
        this.count = 0;
        return tmp;
    }

}
