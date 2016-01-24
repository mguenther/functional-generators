package com.mgu.generator.example.monoid;

import java.util.Optional;

public class OptionalMonoid<A> implements Monoid<Optional<A>> {

    @Override
    public Optional<A> op(final Optional<A> a1, final Optional<A> a2) {
        if (a1.isPresent()) {
            return a1;
        } else {
            return a2;
        }
    }

    @Override
    public Optional<A> zero() {
        return Optional.<A>empty();
    }
}