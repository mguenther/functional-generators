package com.mgu.test.generator;

import static com.mgu.test.generator.CoreGen.choose;
import static com.mgu.test.generator.CoreGen.listOfN;

/**
 * {@code Gen} implementations for {@link java.lang.String}.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class StringGen {

    public static Gen<String> asciiString(final int length) {
        return listOfN(choose(0, 127), length)
                .map(list -> list
                        .stream()
                        .map(n -> Character.toChars(n))
                        .map(c -> String.valueOf(c))
                        .reduce("", String::concat, String::concat));
    }
}