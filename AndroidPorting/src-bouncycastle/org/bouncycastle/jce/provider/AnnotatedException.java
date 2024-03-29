package org.bouncycastle.jce.provider;

import org.bouncycastle.jce.exception.ExtException;

public class AnnotatedException extends Exception implements ExtException {
    private static final long serialVersionUID = 352510134905599154L;
    private Throwable _underlyingException;

    AnnotatedException(String string, Throwable e) {
        super(string);

        _underlyingException = e;
    }

    AnnotatedException(String string) {
        this(string, null);
    }

    Throwable getUnderlyingException() {
        return _underlyingException;
    }

    public Throwable getCause() {
        return _underlyingException;
    }
}
