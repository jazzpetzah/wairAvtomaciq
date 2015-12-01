package com.wearezeta.auto.common;

import java.lang.reflect.Field;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Random;
import java.util.UUID;

import javax.crypto.*;
import javax.crypto.spec.*;

import org.apache.commons.codec.binary.Base64;

public class InvitationLinkGenerator {
	private static Long TimeZero = 1388534400000L; // 2014.1.1 00:00:00 UTC
	private static byte Secret[] = { 0x64, 0x68, (byte) 0xca, (byte) 0xee,
			0x5c, 0x0, 0x25, (byte) 0xf5, 0x68, (byte) 0xe4, (byte) 0xd0,
			(byte) 0x85, (byte) 0xf8, 0x38, 0x28, 0x6a, (byte) 0x8a,
			(byte) 0x98, 0x6d, 0x2d, (byte) 0xfa, 0x67, 0x5e, 0x48,
			(byte) 0xa3, (byte) 0xed, 0x2a, (byte) 0xef, (byte) 0xdd,
			(byte) 0xaf, (byte) 0xe8, (byte) 0xc1 };
	private static byte[] IV = new byte[] { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
			0, 0, 0, 0 };

	private static Cipher getCipher(int mode) throws Exception {
		Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
		cipher.init(mode, new SecretKeySpec(Secret, "AES"),
				new IvParameterSpec(IV));
		return cipher;
	}

	private static Cipher encipher() throws Exception {
		return getCipher(Cipher.ENCRYPT_MODE);
	}

	private static String encodeToken(String userId) throws Exception {
		Long currentTime = System.currentTimeMillis();
		byte[] bytes = new byte[32];
		new Random().nextBytes(bytes);
		ByteBuffer buffer = ByteBuffer.wrap(bytes);
		buffer.order(ByteOrder.BIG_ENDIAN);
		buffer.position(14);
		buffer.putShort(getTimestamp(currentTime));
		UUID uuid = UUID.fromString(userId);
		buffer.putLong(uuid.getMostSignificantBits());
		buffer.putLong(uuid.getLeastSignificantBits());
		Cipher thisCipher = encipher();
		byte[] cipherBytes = thisCipher.doFinal(bytes);
		byte[] bytesEncoded = Base64.encodeBase64(cipherBytes, false, true);
		return new String(bytesEncoded);
	}

	private static short getTimestamp(Long currentTime) {
		return (short) (Math.max(0, (currentTime - TimeZero) / 1000 / 3600));
	}

	public static String getInvitationUrl(String uuid) throws Exception {
		final String currentBackendType = CommonUtils
				.getBackendType(InvitationLinkGenerator.class);
		switch (currentBackendType.toLowerCase()) {
		case "staging":
		case "benny":
		case "gregor":
		case "rene":
		case "lipis":
		case "chris":
			return String.format("https://staging-website.zinfra.io/c/%s",
					encodeToken(uuid));
		case "production":
			return String
					.format("https://app.wire.com/c/%s", encodeToken(uuid));
		default:
			throw new RuntimeException(String.format(
					"Non supported backend type '%s'", currentBackendType));
		}
	}

	public static String getInvitationToken(String uuid) throws Exception {
		return encodeToken(uuid);
	}

	static {
		// https://github.com/jruby/jruby/wiki/UnlimitedStrengthCrypto
		try {
			final Class<?> securityClass = Class
					.forName("javax.crypto.JceSecurity");
			final Field restrictedField = securityClass
					.getDeclaredField("isRestricted");
			restrictedField.setAccessible(true);
			restrictedField.set(null, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
