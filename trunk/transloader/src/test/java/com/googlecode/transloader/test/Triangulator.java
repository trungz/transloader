package com.googlecode.transloader.test;

import java.util.Random;

public final class Triangulator {
	private static final Random RANDOM = new Random(System.currentTimeMillis());
	private static final byte[] ONE_BYTE_BUFFER = new byte[1];

	private Triangulator() {
	}

	public static String anyString() {
		return anyDouble() + "";
	}

	public static boolean eitherBoolean() {
		return RANDOM.nextBoolean();
	}

	public static byte anyByte() {
		RANDOM.nextBytes(ONE_BYTE_BUFFER);
		return ONE_BYTE_BUFFER[0];
	}

	public static char anyChar() {
		return (char) anyByte();
	}

	public static short anyShort() {
		return (short) anyByte();
	}

	public static int anyInt() {
		return RANDOM.nextInt();
	}

	public static int anyIntFromZeroTo(int upperBound) {
		return RANDOM.nextInt(upperBound);
	}

	public static Integer anyInteger() {
		return new Integer(anyInt());
	}

	public static long anyLong() {
		return RANDOM.nextLong();
	}

	public static float anyFloat() {
		return RANDOM.nextFloat();
	}

	public static double anyDouble() {
		return RANDOM.nextDouble();
	}
}
