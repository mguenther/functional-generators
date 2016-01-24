package com.mgu.functional;

public class Tuple3<A, B, C> {

    public final A a;

    public final B b;

    public final C c;

    public Tuple3(A a, B b, C c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    @Override
    public String toString() {
        return "Tuple3{" + "a=" + a + ", b=" + b + ", c=" + c + '}';
    }
}