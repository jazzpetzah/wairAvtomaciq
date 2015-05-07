package com.wearezeta.auto.common.usrmgmt;

import java.util.Random;

public final class PhoneNumber {
	private static final int MAX_NUMBER_LENGTH = 16;
	private static final int MIN_NUMBER_LENGTH = 8;
	public static final String WIRE_COUNTRY_PREFIX = "+0";

	private String number;

	@Override
	public String toString() {
		return this.number;
	}

	public static class IncorrectPhoneNumberException extends Exception {

		private static final long serialVersionUID = 3131850198106711281L;

		public IncorrectPhoneNumberException(String msg) {
			super(msg);
		}
	}

	public PhoneNumber(String prefix, String number)
			throws IncorrectPhoneNumberException {
		this.number = formatNumber(prefix + number);
	}

	public PhoneNumber(String prefix) throws IncorrectPhoneNumberException {
		this.number = formatNumber(generateRandomNumber(prefix));
	}

	private String generateRandomNumber(String prefix) {
		final StringBuilder result = new StringBuilder();
		result.append(prefix);
		final Random rand = new Random();
		for (int i = 1; i <= MIN_NUMBER_LENGTH - prefix.length()
				+ rand.nextInt(MAX_NUMBER_LENGTH - MIN_NUMBER_LENGTH); i++) {
			result.append(Integer.toString(rand.nextInt(10)));
		}
		return result.toString();
	}

	private String formatNumber(String number)
			throws IncorrectPhoneNumberException {
		String resultNumber = number;
		if (!resultNumber.startsWith("+")) {
			resultNumber = "+" + resultNumber;
		}
		if (resultNumber.length() < MIN_NUMBER_LENGTH) {
			throw new IncorrectPhoneNumberException(String.format(
					"Phone number '%s' cannot be shorter than %s characters",
					resultNumber, MIN_NUMBER_LENGTH));
		}
		if (resultNumber.length() > MAX_NUMBER_LENGTH) {
			throw new IncorrectPhoneNumberException(String.format(
					"Phone number '%s' cannot be longer than %s characters",
					resultNumber, MAX_NUMBER_LENGTH));
		}
		return resultNumber;
	}
}
