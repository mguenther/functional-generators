package com.mgu.functional;

public class Tuples {

    public static <A, B> Tuple2<A, B> of(final A a, final B b) {
        return new Tuple2<>(a, b);
    }

    public static <A, B, C> Tuple3<A, B, C> of(final A a, final B b, final C c) {
        return new Tuple3<>(a, b, c);
    }

    public static <A, B, C, D> Tuple4<A, B, C, D> of(final A a, final B b, final C c, final D d) {
        return new Tuple4<>(a, b, c, d);
    }
}