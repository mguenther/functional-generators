package com.mgu.generator.example.monoid;

public class IntMultiplicationMonoid implements Monoid<Integer> {

    @Override
    public Integer op(final Integer a1, final Integer a2) {
        return a1 * a2;
    }

    @Override
    public Integer zero() {
        return 1;
    }
}