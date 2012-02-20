package org.bouncycastle.openssl;

import java.io.IOException;

public class EncryptionException extends IOException {
    private static final long serialVersionUID = 4380914365950352861L;
    private Throwable cause;

    public EncryptionException(String msg) {
        super(msg);
    }

    public EncryptionException(String msg, Throwable ex) {
        super(msg);
        this.cause = ex;
    }

    public Throwable getCause() {
        return cause;
    }
}