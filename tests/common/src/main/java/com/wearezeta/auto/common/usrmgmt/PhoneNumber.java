package com.wearezeta.auto.common.usrmgmt;

import java.math.BigInteger;
import java.util.Random;

public final class PhoneNumber {
    private static final int MAX_NUMBER_LENGTH = 14;
    // This is, actually, 8, but duplicated phone numbers are possible if we have low digits count
    private static final int MIN_NUMBER_LENGTH = 10;
    public static final String WIRE_COUNTRY_PREFIX = "+0";

    private String number;
    private String prefix = WIRE_COUNTRY_PREFIX;

    @Override
    public String toString() {
        return this.prefix + this.number;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof String) {
            return (this.prefix + this.number).equals(other);
        } else if (other instanceof PhoneNumber) {
            return this.number.equals(((PhoneNumber) other).number) && this.prefix.equals(((PhoneNumber) other).prefix);
        }
        return false;
    }

    public PhoneNumber(String prefix, String number) {
        this.prefix = prefix;
        this.number = number;
    }

    public PhoneNumber(String prefix) {
        this.prefix = prefix;
        this.number = generateRandomNumber(this.prefix.replace("+", "").length());
    }

    public PhoneNumber(String prefix, int digitsCount) {
        this.prefix = prefix;
        this.number = generateRandomNumber(this.prefix.replace("+", "").length(), digitsCount);
    }

    public PhoneNumber(int digitsCount) {
        this.number = generateRandomNumber(this.prefix.replace("+", "").length(), digitsCount);
    }

    private static final Random rand = new Random();

    private static String generateRandomNumber(final int prefixLen) {
        return generateRandomNumber(prefixLen, MIN_NUMBER_LENGTH + rand.nextInt(MAX_NUMBER_LENGTH - MIN_NUMBER_LENGTH));
    }

    private static String generateRandomNumber(final int prefixLen, final int digitsCount) {
        assert digitsCount > 1 : "Phone number digits count should be greater than one";
        final StringBuilder result = new StringBuilder();
        // The very first digit should not be Zero
        result.append(Integer.toString(1 + rand.nextInt(9)));
        for (int i = 0; i < digitsCount - prefixLen - 1; i++) {
            result.append(Integer.toString(rand.nextInt(10)));
        }
        return result.toString();
    }

    public static PhoneNumber increasedBy(PhoneNumber srcNumber, BigInteger by) {
        final BigInteger srcNumberAsBigInt = new BigInteger(srcNumber.withoutPrefix());
        final String increasedNumber = srcNumberAsBigInt.add(by).toString();
        return new PhoneNumber(srcNumber.getPrefix(), increasedNumber);
    }

    public String withoutPrefix() {
        return this.number;
    }

    public String getPrefix() {
        return prefix;
    }
}
