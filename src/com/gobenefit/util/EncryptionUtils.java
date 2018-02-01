package com.gobenefit.util;

import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.util.SimpleByteSource;

public class EncryptionUtils {

	private static final String FIXED_SALT_VALUE = "GoBeNeFit";

	public static String getSaltedHash(String dynamicSalt, String password) {
		Sha256Hash sha256Hash = new Sha256Hash(password,
				(new SimpleByteSource(FIXED_SALT_VALUE + dynamicSalt)).getBytes());
		return sha256Hash.toHex();
	}

}
