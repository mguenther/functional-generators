package com.mgu.generator;

import static com.mgu.generator.CoreGen.choose;

/**
 * {@code Gen} implementations for {@link java.lang.Integer}.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class IntegerGen {

    public static Gen<Integer> evenGen(final int start, final int stopExclusive) {
        final int stop = stopExclusive % 2 == 0 ? stopExclusive - 1 : stopExclusive;
        return CoreGen.choose(start, stop).map(n -> n % 2 != 0 ? n + 1 : n);
    }

    public static Gen<Integer> oddGen(final int start, final int stopExclusive) {
        final int stop = stopExclusive % 2 != 0 ? stopExclusive - 1 : stopExclusive;
        return CoreGen.choose(start, stop).map(n -> n % 2 == 0 ? n + 1 : n);
    }
}