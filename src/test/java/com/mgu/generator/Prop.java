package com.mgu.generator;

import java.util.function.Predicate;

public class Prop {

    private static final int DEFAULT_NUMBER_OF_INSTANCES = 100;

    public static <T> boolean forAll(final Gen<T> gen, final Predicate<T> predicate) {

        return CoreGen.listOfN(gen, DEFAULT_NUMBER_OF_INSTANCES)
                .sample()
                .stream()
                .allMatch(instance -> predicate.test(instance));
    }
}