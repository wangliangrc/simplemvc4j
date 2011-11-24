package com.clark.io;

import java.io.IOException;

/**
 * Subclasses IOException with the {@link Throwable} constructors missing before
 * Java 6. If you are using Java 6, consider this class deprecated and use
 * {@link IOException}.
 * 
 * @author <a href="http://commons.apache.org/io/">Apache Commons IO</a>
 * @version $Id: IOExceptionWithCause.java 651569 2008-04-25 10:42:55Z niallp $
 * @since Commons IO 1.4
 */
public class IOExceptionWithCause extends IOException {

    /**
     * Defines the serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Constructs a new instance with the given message and cause.
     * <p>
     * As specified in {@link Throwable}, the message in the given
     * <code>cause</code> is not used in this instance's message.
     * </p>
     * 
     * @param message
     *            the message (see {@link #getMessage()})
     * @param cause
     *            the cause (see {@link #getCause()}). A <code>null</code> value
     *            is allowed.
     */
    public IOExceptionWithCause(String message, Throwable cause) {
        super(message);
        this.initCause(cause);
    }

    /**
     * Constructs a new instance with the given cause.
     * <p>
     * The message is set to <code>cause==null ? null : cause.toString()</code>,
     * which by default contains the class and message of <code>cause</code>.
     * This constructor is useful for call sites that just wrap another
     * throwable.
     * </p>
     * 
     * @param cause
     *            the cause (see {@link #getCause()}). A <code>null</code> value
     *            is allowed.
     */
    public IOExceptionWithCause(Throwable cause) {
        super(cause == null ? null : cause.toString());
        this.initCause(cause);
    }

}
