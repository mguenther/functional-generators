package com.mgu.generator.example.monoid;

public class StringMonoid implements Monoid<String> {

    @Override
    public String op(final String a1, final String a2) {
        return a1 + a2;
    }

    @Override
    public String zero() {
        return "";
    }
}