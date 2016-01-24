package com.mgu.functional;

import java.util.Objects;

/**
 * Represents a predicate (boolean-valued function) of three arguments. This is
 * the three-arity specialization of {@link java.util.function.Predicate}.
 *
 * @param <T>
 *     the type of the first argument to the predicate
 * @param <U>
 *     the type of the second argument to the predicate
 * @param <V>
 *     the type of the third argument to the predicate
 * @param <W>
 *     the type of the fourth argument to the predicate
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
@FunctionalInterface
public interface Predicate4<T, U, V, W> {

    /**
     * Evaluates this predicate on the given arguments.
     *
     * @param t
     *      the first input argument
     * @param u
     *      the second input argument
     * @param v
     *      the third input argument
     * @param w
     *      the fourth input argument
     * @return
     *      {@code true} if the input arguments match the predicate,
     *      otherwise {@code false}
     */
    boolean test(T t, U u, V v, W w);

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * AND of this predicate and another. When evaluating the composed predicate,
     * if this predicate is {@code false}, then {@code other} predicate is not
     * evaluated.
     *
     * Any exceptions thrown during evaluation of either predicate are relayed
     * to the caller; if evaluation of this predicate throws an exception, the
     * {@code other} predicate will not be evaluated.
     *
     * @param other
     *      a predicate that will be logically-ANDed with this predicate
     * @return
     *      a composed predicate that represents the short-circuiting logical
     *      AND of this predicate and the {@code other} predicate
     * @throws NullPointerException
     *      if other is null
     */
    default Predicate4<T, U, V, W> and(final Predicate4<? super T, ? super U, ? super V, ? super W> other) {
        Objects.requireNonNull(other);
        return (T t, U u, V v, W w) -> test(t, u, v, w) && other.test(t, u, v, w);
    }

    /**
     * Returns the predicate that represents the logical negation of this
     * predicate.
     *
     * @return
     *      a predicate that represents the logical negation of this predicate
     */
    default Predicate4<T, U, V, W> negate() {
        return (T t, U u, V v, W w) -> !test(t, u, v, w);
    }

    /**
     * Returns a composed predicate that represents a short-circuiting logical
     * OR of this predicate and another. When evaluating the composed predicate,
     * if thise predicate is {@code true}, then the {@code other} predicate is
     * not evaluated.
     *
     * Any exceptions thrown during evaluation of either predicate are relayed
     * to othe caller; if evaluation of this predicate throws an exception, the
     * {@code other} predicate will not be evaluated.
     *
     * @param other
     *      a predicate that will be logically-ORed with this predicate
     * @return
     *      a composed predicate that represents the short-circuiting logical
     *      OR of this predicate and the {@code other} predicate
     * @throws NullPointerException
     *      if other is null
     */
    default Predicate4<T, U, V, W> or(final Predicate4<? super T, ? super U, ? super V, ? super W> other) {
        Objects.requireNonNull(other);
        return (T t, U u, V v, W w) -> test(t, u, v, w) || other.test(t, u, v, w);
    }
}