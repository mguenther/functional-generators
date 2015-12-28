package com.mgu.generator;

/**
 * {@code Gen} implementations for {@link java.lang.Character}.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class CharGen {

    private static final char[] NUMERIC_ALPHABET = "0123456789".toCharArray();

    private static final char[] ALPHABET_UPPER_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray();

    private static final char[] ALPHABET_LOWER_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase().toCharArray();

    private static final char[] NON_NUMERIC_ALPHABET = new String("ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase()).toCharArray();

    private static final char[] ALPHANUMERIC_ALPHABET = new String("ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase() + "0123456789").toCharArray();

    public static Gen<Character> numCharGen() {
        return charFromAlphabetGen(NUMERIC_ALPHABET);
    }

    public static Gen<Character> alphaUpperCharGen() {
        return charFromAlphabetGen(ALPHABET_UPPER_CHARS);
    }

    public static Gen<Character> alphaLowerCharGen() {
        return charFromAlphabetGen(ALPHABET_LOWER_CHARS);
    }

    public static Gen<Character> alphaCharGen() {
        return charFromAlphabetGen(NON_NUMERIC_ALPHABET);
    }

    public static Gen<Character> alphaNumCharGen() {
        return charFromAlphabetGen(ALPHANUMERIC_ALPHABET);
    }

    private static Gen<Character> charFromAlphabetGen(final char[] alphabet) {
        return () -> {
            final int index = (int) (Math.random() * alphabet.length + 1);
            return alphabet[index];
        };
    }
}