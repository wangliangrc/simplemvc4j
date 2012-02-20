package org.bouncycastle.util.io;

import java.io.IOException;

public class StreamOverflowException extends IOException {
    private static final long serialVersionUID = -176261482598306484L;

    public StreamOverflowException(String msg) {
        super(msg);
    }
}
