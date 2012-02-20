package org.bouncycastle.util.io.pem;

import java.io.IOException;

public class PemGenerationException extends IOException {
    private static final long serialVersionUID = 4810175937322908252L;
    private Throwable cause;

    public PemGenerationException(String message, Throwable cause) {
        super(message);
        this.cause = cause;
    }

    public PemGenerationException(String message) {
        super(message);
    }

    public Throwable getCause() {
        return cause;
    }
}
