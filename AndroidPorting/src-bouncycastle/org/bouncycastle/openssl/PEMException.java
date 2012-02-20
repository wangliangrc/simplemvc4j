package org.bouncycastle.openssl;

import java.io.IOException;

public class PEMException extends IOException {
    private static final long serialVersionUID = 5420181554425242221L;
    Exception underlying;

    public PEMException(String message) {
        super(message);
    }

    public PEMException(String message, Exception underlying) {
        super(message);
        this.underlying = underlying;
    }

    public Exception getUnderlyingException() {
        return underlying;
    }

    public Throwable getCause() {
        return underlying;
    }
}
