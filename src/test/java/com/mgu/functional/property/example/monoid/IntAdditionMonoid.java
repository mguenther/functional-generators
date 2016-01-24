package com.mgu.functional.property.example.monoid;

public class IntAdditionMonoid implements Monoid<Integer> {

    @Override
    public Integer op(final Integer a1, final Integer a2) {
        return a1 + a2;
    }

    @Override
    public Integer zero() {
        return 0;
    }
}
