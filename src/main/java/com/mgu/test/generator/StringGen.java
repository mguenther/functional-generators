package com.mgu.test.generator;

import static com.mgu.test.generator.CoreGen.choose;
import static com.mgu.test.generator.CoreGen.listOfN;

/**
 * {@code Gen} implementations for {@link java.lang.String}.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class StringGen {

    private static final String ALPHABET = "ABCDEFGHIJKLMNOPQRSTUVWXYZ" + "ABCDEFGHIJKLMNOPQRSTUVWXYZ".toLowerCase() + "0123456789";

    public static Gen<String> asciiStringGen(final int length) {
        return listOfN(choose(0, 127), length)
                .map(list -> list
                        .stream()
                        .map(n -> Character.toChars(n))
                        .map(c -> String.valueOf(c))
                        .reduce("", String::concat, String::concat));
    }

    public static Gen<String> alphaNumStringGen(final int length) {
        return listOfN(choose(0, ALPHABET.length()-1), length)
                .map(list -> list
                .stream()
                .map(n -> ALPHABET.charAt(n))
                .map(c -> String.valueOf(c))
                .reduce("", String::concat, String::concat));
    }
}