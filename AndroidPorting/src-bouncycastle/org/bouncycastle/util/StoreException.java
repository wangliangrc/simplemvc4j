package org.bouncycastle.util;

public class StoreException extends RuntimeException {
    private static final long serialVersionUID = -5658663376883841716L;
    private Throwable _e;

    public StoreException(String s, Throwable e) {
        super(s);
        _e = e;
    }

    public Throwable getCause() {
        return _e;
    }
}
