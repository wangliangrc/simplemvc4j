package org.bouncycastle.jce.exception;

import java.security.cert.CertPath;
import java.security.cert.CertPathBuilderException;

public class ExtCertPathBuilderException extends CertPathBuilderException
        implements ExtException {
    private static final long serialVersionUID = -4668338258566227923L;
    private Throwable cause;

    public ExtCertPathBuilderException(String message, Throwable cause) {
        super(message);
        this.cause = cause;
    }

    public ExtCertPathBuilderException(String msg, Throwable cause,
            CertPath certPath, int index) {
        super(msg, cause);
        this.cause = cause;
    }

    public Throwable getCause() {
        return cause;
    }
}
