package org.bouncycastle.openssl;

import java.io.IOException;

public class PasswordException extends IOException {
    private static final long serialVersionUID = -5071684799002847621L;

    public PasswordException(String msg) {
        super(msg);
    }
}
