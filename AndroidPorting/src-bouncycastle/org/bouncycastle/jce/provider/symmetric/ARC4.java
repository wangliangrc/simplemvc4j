package org.bouncycastle.jce.provider.symmetric;

import java.util.HashMap;

import org.bouncycastle.crypto.CipherKeyGenerator;
import org.bouncycastle.crypto.engines.RC4Engine;
import org.bouncycastle.jce.provider.JCEKeyGenerator;
import org.bouncycastle.jce.provider.JCEStreamCipher;

public final class ARC4 {
    private ARC4() {
    }

    public static class Base extends JCEStreamCipher {
        public Base() {
            super(new RC4Engine(), 0);
        }
    }

    public static class KeyGen extends JCEKeyGenerator {
        public KeyGen() {
            // BEGIN android-changed
            super("ARC4", 128, new CipherKeyGenerator());
            // END android-changed
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public static class Mappings extends HashMap {
        private static final long serialVersionUID = 8687873169238755613L;

        public Mappings() {
            put("Cipher.ARC4",
                    "org.bouncycastle.jce.provider.symmetric.ARC4$Base");
            put("Alg.Alias.Cipher.1.2.840.113549.3.4", "ARC4");
            put("Alg.Alias.Cipher.ARCFOUR", "ARC4");
            put("Alg.Alias.Cipher.RC4", "ARC4");
            put("KeyGenerator.ARC4",
                    "org.bouncycastle.jce.provider.symmetric.ARC4$KeyGen");
            put("Alg.Alias.KeyGenerator.RC4", "ARC4");
            put("Alg.Alias.KeyGenerator.1.2.840.113549.3.4", "ARC4");
        }
    }
}
