package com.mgu.functional;

import java.util.Objects;

@FunctionalInterface
public interface Predicate3<T, U, V> {

    boolean test(T t, U u, V v);

    default Predicate3<T, U, V> and(Predicate3<? super T, ? super U, ? super V> other) {
        Objects.requireNonNull(other);
        return (T t, U u, V v) -> test(t, u, v) && other.test(t, u, v);
    }

    default Predicate3<T, U, V> negate() {
        return (T t, U u, V v) -> !test(t, u, v);
    }

    default Predicate3<T, U, V> or(Predicate3<? super T, ? super U, ? super V> other) {
        Objects.requireNonNull(other);
        return (T t, U u, V v) -> test(t, u, v) || other.test(t, u, v);
    }
}
