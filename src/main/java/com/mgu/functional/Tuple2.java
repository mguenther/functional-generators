package com.mgu.functional;

public class Tuple2<A, B> {

    public final A a;

    public final B b;

    public Tuple2(final A a, final B b) {
        this.a = a;
        this.b = b;
    }

    @Override
    public String toString() {
        return "Tuple2{" + "a=" + a + ", b=" + b + '}';
    }
}