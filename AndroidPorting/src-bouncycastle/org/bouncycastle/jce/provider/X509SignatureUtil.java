package org.bouncycastle.jce.provider;

import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.GeneralSecurityException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.PSSParameterSpec;

import org.bouncycastle.asn1.ASN1Null;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DEREncodable;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.nist.NISTObjectIdentifiers;
import org.bouncycastle.asn1.oiw.OIWObjectIdentifiers;
import org.bouncycastle.asn1.pkcs.PKCSObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x9.X9ObjectIdentifiers;

class X509SignatureUtil {
    // BEGIN android-changed
    private static final ASN1Null derNull = DERNull.INSTANCE;

    // END android-changed

    static void setSignatureParameters(Signature signature, DEREncodable params)
            throws NoSuchAlgorithmException, SignatureException,
            InvalidKeyException {
        if (params != null && !derNull.equals(params)) {
            AlgorithmParameters sigParams = AlgorithmParameters.getInstance(
                    signature.getAlgorithm(), signature.getProvider());

            try {
                sigParams.init(params.getDERObject().getDEREncoded());
            } catch (IOException e) {
                throw new SignatureException(
                        "IOException decoding parameters: " + e.getMessage());
            }

            if (signature.getAlgorithm().endsWith("MGF1")) {
                try {
                    signature.setParameter(sigParams
                            .getParameterSpec(PSSParameterSpec.class));
                } catch (GeneralSecurityException e) {
                    throw new SignatureException(
                            "Exception extracting parameters: "
                                    + e.getMessage());
                }
            }
        }
    }

    @SuppressWarnings("deprecation")
    static String getSignatureName(AlgorithmIdentifier sigAlgId) {
        DEREncodable params = sigAlgId.getParameters();

        if (params != null && !derNull.equals(params)) {
            // BEGIN android-removed
            // if
            // (sigAlgId.getObjectId().equals(PKCSObjectIdentifiers.id_RSASSA_PSS))
            // {
            // RSASSAPSSparams rsaParams = RSASSAPSSparams.getInstance(params);
            //
            // return
            // getDigestAlgName(rsaParams.getHashAlgorithm().getObjectId()) +
            // "withRSAandMGF1";
            // }
            // END android-removed
            if (sigAlgId.getObjectId().equals(
                    X9ObjectIdentifiers.ecdsa_with_SHA2)) {
                ASN1Sequence ecDsaParams = ASN1Sequence.getInstance(params);

                return getDigestAlgName((DERObjectIdentifier) ecDsaParams
                        .getObjectAt(0)) + "withECDSA";
            }
        }

        return sigAlgId.getObjectId().getId();
    }

    /**
     * Return the digest algorithm using one of the standard JCA string
     * representations rather the the algorithm identifier (if possible).
     */
    private static String getDigestAlgName(DERObjectIdentifier digestAlgOID) {
        if (PKCSObjectIdentifiers.md5.equals(digestAlgOID)) {
            return "MD5";
        } else if (OIWObjectIdentifiers.idSHA1.equals(digestAlgOID)) {
            return "SHA1";
        }
        // BEGIN android-removed
        // else if (NISTObjectIdentifiers.id_sha224.equals(digestAlgOID))
        // {
        // return "SHA224";
        // }
        // END android-removed
        else if (NISTObjectIdentifiers.id_sha256.equals(digestAlgOID)) {
            return "SHA256";
        } else if (NISTObjectIdentifiers.id_sha384.equals(digestAlgOID)) {
            return "SHA384";
        } else if (NISTObjectIdentifiers.id_sha512.equals(digestAlgOID)) {
            return "SHA512";
        }
        // BEGIN android-removed
        // else if (TeleTrusTObjectIdentifiers.ripemd128.equals(digestAlgOID))
        // {
        // return "RIPEMD128";
        // }
        // else if (TeleTrusTObjectIdentifiers.ripemd160.equals(digestAlgOID))
        // {
        // return "RIPEMD160";
        // }
        // else if (TeleTrusTObjectIdentifiers.ripemd256.equals(digestAlgOID))
        // {
        // return "RIPEMD256";
        // }
        // else if (CryptoProObjectIdentifiers.gostR3411.equals(digestAlgOID))
        // {
        // return "GOST3411";
        // }
        // END android-removed
        else {
            return digestAlgOID.getId();
        }
    }
}
