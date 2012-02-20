package org.bouncycastle.jce.provider;

import java.security.cert.CRLException;

class ExtCRLException extends CRLException {
    private static final long serialVersionUID = -3257885400501138925L;
    Throwable cause;

    ExtCRLException(String message, Throwable cause) {
        super(message);
        this.cause = cause;
    }

    public Throwable getCause() {
        return cause;
    }
}
