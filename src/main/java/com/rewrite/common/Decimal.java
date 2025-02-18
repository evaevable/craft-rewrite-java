package com.rewrite.common;

public class Decimal {
    public static final int MIN_DIGIT = 32;
    public static final int MAX_DIGIT = 126;
    public static final int MID_DIGIT = MIN_DIGIT + (MAX_DIGIT - MIN_DIGIT) / 2 + 1;
    public static final int MIN_BASE = 2;
    public static final int MAX_BASE = MAX_DIGIT - MIN_DIGIT + 1;

    private final int base;
    private String fractionalDigits;

    public Decimal(int base, String fractionalDigits) {
        if (base < MIN_BASE || base > MAX_BASE) {
            throw new IllegalArgumentException("Invalid base: " + base);
        }
        this.base = base;
        this.fractionalDigits = fractionalDigits;
    }

    public int getBase() {
        return this.base;
    }

    public String getFractionalDigits() {
        return this.fractionalDigits;
    }

    public void align(Decimal other) {
        int maxLength = Math.max(
                this.fractionalDigits.length(),
                other.fractionalDigits.length()
        );
        this.fractionalDigits = padEnd(this.fractionalDigits, maxLength, (char)MIN_DIGIT);
        other.fractionalDigits = padEnd(other.fractionalDigits, maxLength, (char)MIN_DIGIT);
    }

    // 前缀递增操作符
    public Decimal increment() {
        final int maxDigit = MIN_DIGIT + base - 1;
        if(this.fractionalDigits.chars().allMatch(c -> c == maxDigit)) {
            this.fractionalDigits += (char) (MIN_DIGIT + 1);
            return this;
        }
        char[] digits = this.fractionalDigits.toCharArray();
        for (int i = digits.length - 1; i >= 0; i--) {
            if(digits[i] < maxDigit){
                digits[i]++;
                this.fractionalDigits = new String(digits);
                return this;
            } else {
                digits[i] = (char) MIN_DIGIT;
            }
        }
        this.fractionalDigits = new String(digits);
        return this;
    }

    //后缀递减操作符
    public Decimal decrement() {
        final int maxDigit = MIN_DIGIT + base - 1;
        if(this.fractionalDigits.endsWith(String.valueOf((char) (MIN_DIGIT + 1)))) {
            boolean allMIN = true;
            for(int i = 0; i < this.fractionalDigits.length(); i++) {
                if(this.fractionalDigits.charAt(i) != MIN_DIGIT){
                    allMIN = false;
                    break;
                }
            }
            if(allMIN) {
                this.fractionalDigits = this.fractionalDigits.substring(0, this.fractionalDigits.length() - 1) + (char) MIN_DIGIT;
                this.fractionalDigits += (char) maxDigit;
                return this;
            }
        }

        char[] digits = this.fractionalDigits.toCharArray();
        for(int i = digits.length - 1; i >= 0; i--) {
            if(digits[i] > MIN_DIGIT) {
                digits[i]--;
                this.fractionalDigits = new String(digits);
                return this;
            } else {
                digits[i] = (char) maxDigit;
            }
        }
        return this;
    }

    public Decimal postfixIncrement() {
        Decimal temp = new Decimal(this.base, this.fractionalDigits);
        this.increment();
        return temp;
    }

    public Decimal postfixDecrement() {
        Decimal temp = new Decimal(this.base, this.fractionalDigits);
        this.decrement();
        return temp;
    }

    public boolean lessThan(Decimal other) {
        if (this.base != other.base) throw new IllegalArgumentException("Different base");

        int maxLength = Math.max(this.fractionalDigits.length(), other.fractionalDigits.length());
        for (int i = 0; i < maxLength; i++) {
            char a = i < this.fractionalDigits.length() ? this.fractionalDigits.charAt(i) : (char) MIN_DIGIT;
            char b = i < other.fractionalDigits.length() ? other.fractionalDigits.charAt(i) : (char) MIN_DIGIT;

            if (a != b) return a < b;
        }
        return false;
    }

    public boolean greaterThan(Decimal other) {
        return other.lessThan(this);
    }

    public boolean lessThanOrEqual(Decimal other) {
        return !this.greaterThan(other);
    }

    public boolean greaterThanOrEqual(Decimal other) {
        return !this.lessThan(other);
    }

    public boolean equals(Decimal other) {
        return !this.lessThan(other) && !this.greaterThan(other);
    }

    private String padEnd(String fractionalDigits, int length, char padChar) {
        if(fractionalDigits.length() >= length) return fractionalDigits;
        StringBuilder sb = new StringBuilder(fractionalDigits);
        while (sb.length() < length) sb.append(padChar);
        return sb.toString();
    }
}
