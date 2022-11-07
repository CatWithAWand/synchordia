package com.catwithawand.synchordia.util;

import java.math.BigInteger;
import java.security.SecureRandom;

public class RandomUtils {

  private static final SecureRandom randomGenerator = new SecureRandom();

  public static String randomAlphanumeric(int length) {
    byte[] tokens = new byte[length];
    randomGenerator.nextBytes(tokens);

    return new BigInteger(1, tokens).toString(16).substring(0, length);
  }

}
