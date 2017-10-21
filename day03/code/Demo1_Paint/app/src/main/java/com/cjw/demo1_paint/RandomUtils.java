package com.cjw.demo1_paint;

import java.math.BigDecimal;
import java.security.SecureRandom;
import java.util.UUID;

public class RandomUtils {

	private static SecureRandom sRandom = new SecureRandom();

	private RandomUtils() {
	}

	public static String getString() {
		return getString(-1);
	}

	public static String getString(int length) {
		String str = UUID.randomUUID().toString().replace("-", "");
		if (length > 0) {
			if (length <= str.length()) {
				return str.substring(0, length);
			} else {
				int lessLength = length - str.length();
				return str + getString(lessLength);
			}
		} else {
			return str;
		}
	}

	public static int getInt() {
		return sRandom.nextInt();
	}

	public static int getInt(int num) {
		return sRandom.nextInt(num);
	}

	public static double getDouble() {
		return getDouble(2);
	}

	public static double getDouble(int pointNum) {
		return getDouble(0, 1, pointNum);
	}

	public static double getDouble(double start, double end) {
		return getDouble(start, end, 2);
	}

	public static double getDouble(double start, double end, int pointNumber) {
		double temp = end - start;
		double value = sRandom.nextDouble() * temp + start;

		BigDecimal decimal = new BigDecimal(value);
		return decimal.setScale(pointNumber, BigDecimal.ROUND_HALF_UP)
				.doubleValue();
	}

	public static long getLong() {
		return sRandom.nextLong();
	}

	public static boolean getBoolean() {
		return sRandom.nextBoolean();
	}
}
