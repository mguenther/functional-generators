package com.mgu.functional.property.example.monoid;

public interface Monoid<A> {

    A op(A a1, A a2);

    A zero();
}