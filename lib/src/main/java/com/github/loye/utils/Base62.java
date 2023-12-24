package com.github.loye.utils;

import java.io.ByteArrayOutputStream;

public class Base62 {

    // [A-Za-z0-9]
    private static final byte[] ALPHABET = new byte[] {
            65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90,
            97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118,
            119, 120, 121, 122,
            48, 49, 50, 51, 52, 53, 54, 55, 56, 57 };
    private static final byte[] LOOKUP = new byte[256];
    private static final int TARGET_BASE = 62;

    static {
        for (int i = 0; i < ALPHABET.length; ++i) {
            LOOKUP[ALPHABET[i]] = (byte) (i & 0xFF);
        }
    }

    public static byte[] encode(byte[] source) {
        byte[] indices = convert(source, 256, TARGET_BASE);
        return translate(indices, ALPHABET);
    }

    public static byte[] decode(byte[] encoded) {
        byte[] translated = translate(encoded, LOOKUP);
        return convert(translated, TARGET_BASE, 256);
    }

    private static byte[] translate(byte[] indices, byte[] dictionary) {
        byte[] result = new byte[indices.length];
        for (int i = 0; i < indices.length; ++i) {
            result[i] = dictionary[indices[i]];
        }
        return result;
    }

    // convert message from source base to target base
    private static byte[] convert(byte[] message, int sourceBase, int targetBase) {
        int outputLength = estimateOutputLength(message.length, sourceBase, targetBase);
        ByteArrayOutputStream out = new ByteArrayOutputStream(outputLength);

        ByteArrayOutputStream quotient;
        for (byte[] source = message; source.length > 0; source = quotient.toByteArray()) {
            quotient = new ByteArrayOutputStream(source.length);
            int remainder = 0;
            for (byte b : source) {
                int accumulator = (b & 0xFF) + remainder * sourceBase;
                int digit = (accumulator - accumulator % targetBase) / targetBase;
                remainder = accumulator % targetBase;
                if (quotient.size() > 0 || digit > 0) {
                    quotient.write(digit);
                }
            }
            out.write(remainder);
        }
        for (int i = 0; i < message.length - 1 && message[i] == 0; ++i) {
            out.write(0);
        }
        return reverse(out.toByteArray());
    }

    private static int estimateOutputLength(int inputLength, int sourceBase, int targetBase) {
        return (int) Math.ceil((Math.log(sourceBase) / Math.log(targetBase)) * (double) inputLength);
    }

    private static byte[] reverse(byte[] source) {
        int length = source.length;
        byte[] reversed = new byte[length];
        for (int i = 0; i < length; ++i) {
            reversed[length - i - 1] = source[i];
        }
        return reversed;
    }
}