package com.mgu.functional.generator;

import java.util.Random;

/**
 * {@code Gen} implementations for {@link java.lang.Boolean}.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class BooleanGen {

    public static Gen<Boolean> booleanGen() {
        final Random r = new Random();
        return () -> r.nextBoolean();
    }
}