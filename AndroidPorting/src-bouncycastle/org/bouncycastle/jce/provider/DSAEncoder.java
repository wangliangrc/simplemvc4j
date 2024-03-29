package org.bouncycastle.jce.provider;

import java.io.IOException;
import java.math.BigInteger;

public interface DSAEncoder {
    byte[] encode(BigInteger r, BigInteger s) throws IOException;

    BigInteger[] decode(byte[] sig) throws IOException;
}
