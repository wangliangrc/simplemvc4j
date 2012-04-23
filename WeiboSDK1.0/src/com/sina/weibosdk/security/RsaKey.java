package com.sina.weibosdk.security;

import java.security.Key;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

class RSAHelper {

	public static PublicKey getPublicKey(String key) throws Exception {
		byte[] keyBytes;
		keyBytes = Base64.decode(key.getBytes());// (new
													// BASE64Decoder()).decodeBuffer(key);

		X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PublicKey publicKey = keyFactory.generatePublic(keySpec);
		return publicKey;
	}

	public static PrivateKey getPrivateKey(String key) throws Exception {
		byte[] keyBytes;
		keyBytes = Base64.decode(key.getBytes());

		PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
		return privateKey;
	}

	public static String getKeyString(Key key) throws Exception {
		byte[] keyBytes = key.getEncoded();
		String s = new String(Base64.encode(keyBytes));// (new
														// BASE64Encoder()).encode(keyBytes);
		return s;
	}

}

public class RsaKey {
	//
	// private PublicKey getPublicKey(String modulus, String publicExponent)
	// throws Exception {
	//
	// BigInteger m = new BigInteger(modulus);
	//
	// BigInteger e = new BigInteger(publicExponent);
	//
	// RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
	//
	// KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	// PublicKey publicKey = keyFactory.generatePublic(keySpec);
	//
	// return publicKey;
	//
	// }
	//
	//
	// private PrivateKey getPrivateKey(String modulus, String privateExponent)
	// throws Exception {
	//
	// BigInteger m = new BigInteger(modulus);
	//
	// BigInteger e = new BigInteger(privateExponent);
	//
	// RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
	//
	// KeyFactory keyFactory = KeyFactory.getInstance("RSA");
	//
	// PrivateKey privateKey = keyFactory.generatePrivate(keySpec);
	//
	// return privateKey;
	//
	// }

	public static final String publicKeyString = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC"
			+ "46y69c1rmEk6btBLCPgxJkCxdDcAH9k7kBLffgG1KWqUErj"
			+ "dv+aMkEZmBaprEW846YEwBn60gyBih3KU518fL3F+sv2b6xEe"
			+ "OxgjWO+NPgSWmT3q1up95HmmLHlgVwqTKqRUHd8+Tr43D5h+J8"
			+ "T69etX0YNdT5ACvm+Ar0HdarwIDAQAB";

	public String encrypt(String src) throws Exception {
		Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding"); // "RSA/ECB/PKCS1Padding"
		PublicKey publicKey = RSAHelper.getPublicKey(publicKeyString);
		byte[] plainText = src.getBytes();
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] enBytes = cipher.doFinal(plainText);
		String result = new String(Base64.encode(enBytes));
		return result;
	}

}