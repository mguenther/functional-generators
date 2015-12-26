package com.mgu.test.generator;

import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A {@code Gen}erator is something that knows how to generate values of type {@code T}. How it does
 * that (leveraging randomness, ...) is open to the actual implementation of this interface.
 *
 * @param <T>
 *     type of the generated object
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
@FunctionalInterface
public interface Gen<T> {

    /**
     * @return
     *      generated value of type {@code T}
     */
    T sample();

    /**
     * Combinator function that returns a new {@code Gen} of type {@code U} which closes over
     * this {@code Gen}. Upon application, it evaluates this {@code Gen} and applies the given
     * {@link Function} on it to obtain a value of {@code U}.
     *
     * @param mapper
     *      maps the output of this {@code Gen} to a value of type {@code U}
     * @param <U>
     *      target type of the outer {@code Gen} returned by this method
     * @return
     *      outer {@code Gen} that generates values of type {@code U}
     */
    default <U> Gen<U> map(final Function<? super T, ? extends U> mapper) {
        return () -> mapper.apply(sample());
    }

    /**
     * Combinator function that takes a sample from this {@code Gen} and feeds it to the
     * given {@code Gen}. This essentially combines two {@code Gen} instances that have a
     * dependency between them.
     *
     * @param mapper
     *      feeds the output of this {@code Gen} into the provided {@code Gen} of target type
     *      {@code U}
     * @param <U>
     *      target type of the outer {@code Gen} returned by this method
     * @return
     *      outer {@code Gen} that generates values of type {@code U}
     */
    default <U> Gen<U> flatMap(final Function<? super T, Gen<U>> mapper) {
        return mapper.apply(sample());
    }

    /**
     * Combinator function that generates samples until it finds a sample that satisfies
     * the given {@code Predicate}. Please note that this implementation does not prevent
     * infinite loops when searching for an admissible sample. If there is no instance
     * in the domain of generated values for {@code this} generator, this method will
     * fail to terminate.
     *
     * @param predicate
     *      generated values will be tested against the given {@code predicate}
     * @return
     *      {@code Gen} that only yields values of type {@code T} that satisfy
     *      the given {@code Predicate}
     */
    default Gen<T> suchThat(final Predicate<? super T> predicate) {
        return () -> Stream
                .iterate(this, t -> t)
                .map(Gen::sample)
                .filter(predicate)
                .findFirst()
                .get();
    }
}