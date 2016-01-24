package com.mgu.functional;

import java.util.Objects;

@FunctionalInterface
public interface Predicate4<T, U, V, W> {

    boolean test(T t, U u, V v, W w);

    default Predicate4<T, U, V, W> and(final Predicate4<? super T, ? super U, ? super V, ? super W> other) {
        Objects.requireNonNull(other);
        return (T t, U u, V v, W w) -> test(t, u, v, w) && other.test(t, u, v, w);
    }

    default Predicate4<T, U, V, W> negate() {
        return (T t, U u, V v, W w) -> !test(t, u, v, w);
    }

    default Predicate4<T, U, V, W> or(final Predicate4<? super T, ? super U, ? super V, ? super W> other) {
        Objects.requireNonNull(other);
        return (T t, U u, V v, W w) -> test(t, u, v, w) || other.test(t, u, v, w);
    }
}