package com.mgu.generator.example.monoid;

import java.util.LinkedList;
import java.util.List;

public class ListMonoid<A> implements Monoid<List<A>> {

    @Override
    public List<A> op(final List<A> a1, final List<A> a2) {
        final List<A> combined = new LinkedList<>();
        combined.addAll(a1);
        combined.addAll(a2);
        return combined;
    }

    @Override
    public List<A> zero() {
        return new LinkedList<>();
    }
}