package com.stp.utils;

import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;

public class NumberUtils {

    private static final Map<String, BigDecimal> SUFFIXES = new LinkedHashMap<>();

    static {
        SUFFIXES.put("Q", new BigDecimal("1000000000000000"));
        SUFFIXES.put("T", new BigDecimal("1000000000000"));
        SUFFIXES.put("B", new BigDecimal("1000000000"));
        SUFFIXES.put("M", new BigDecimal("1000000"));
        SUFFIXES.put("K", new BigDecimal("1000"));
    }

    public static BigDecimal parseAmountWithSuffix(String input) throws NumberFormatException {
        input = input.toUpperCase().trim();
        BigDecimal multiplier = BigDecimal.ONE;

        for (Map.Entry<String, BigDecimal> entry : SUFFIXES.entrySet()) {
            if (input.endsWith(entry.getKey())) {
                multiplier = entry.getValue();
                input = input.substring(0, input.length() - entry.getKey().length());
                break;
            }
        }

        BigDecimal value = new BigDecimal(input);
        BigDecimal result = value.multiply(multiplier);

        if (result.signum() < 0) {
            throw new NumberFormatException("Número negativo no permitido");
        }

        return result;
    }

    public static String formatWithSuffix(BigDecimal number) {
        BigDecimal abs = number.abs();

        for (Map.Entry<String, BigDecimal> entry : SUFFIXES.entrySet()) {
            if (abs.compareTo(entry.getValue()) >= 0) {
                BigDecimal divided = number.divide(entry.getValue());
                String formatted = divided.stripTrailingZeros().scale() <= 0
                        ? divided.toBigInteger().toString()
                        : divided.setScale(1, BigDecimal.ROUND_DOWN).toPlainString();
                return formatted + entry.getKey();
            }
        }

        return number.toPlainString();
    }
}
