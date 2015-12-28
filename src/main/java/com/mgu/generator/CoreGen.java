package com.mgu.generator;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.lang.Math.max;
import static java.lang.Math.random;

/**
 * This class provides some basic generators and utilities that can be used to construct more complex
 * {@code Gen}erators.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class CoreGen {

    /**
     * @return
     *      {@code Gen} that always generates {@code value}.
     */
    public static <T> Gen<T> constant(final T value) {
        return () -> value;
    }

    /**
     * @return
     *      {@code Gen} that randomly returns one of the provided values.
     */
    @SafeVarargs
    public static <T> Gen<T> oneOf(final T... values) {
        return oneOf(Arrays.asList(values));
    }

    /**
     * @return
     *      {@code Gen} that randomly returns one of the provided values
     */
    public static <T> Gen<T> oneOf(final List<T> values) {
        return () -> {
            final int index = (int) (Math.random() * values.size());
            return values.get(index);
        };
    }

    /**
     * @return
     *      randomly returns one of the provided {@code Gen}
     */
    @SafeVarargs
    public static <T> Gen<T> oneOf(final Gen<T>... gs) {
        final List<Gen<T>> gsAsList = Arrays.asList(gs);
        return () -> {
            final int index = (int) (Math.random() * gsAsList.size());
            return gsAsList.get(index).sample();
        };
    }

    /**
     * @return
     *      {@code Gen} that makes the result of the provided {@code Gen}
     *      optional. The result is either a value of type {@code T} or
     *      {@code Optional.empty} with equal probability
     */
    public static <T> Gen<Optional<T>> optional(final Gen<T> gen) {
        return oneOf(some(gen), Optional::empty);
    }

    /**
     * @return
     *      {@code Gen} that makes the result of the provided {@code Gen}
     *      optional
     */
    public static <T> Gen<Optional<T>> some(final Gen<T> gen) {
        return () -> Optional.ofNullable(gen.sample());
    }

    /**
     * Generates a list of instances of type {@code T}. The resulting list may be empty.
     *
     * @param maxLength
     *      the maximum length of the generated {@code List}
     * @return
     *      {@code Gen<List<T>>} which uses the given {@code Gen<T>} to create elements of
     *      generated lists
     */
    public static <T> Gen<List<T>> listOf(final Gen<T> genT, final int maxLength) {
        return () -> {
            final int length = (int) (random() * max(0, maxLength));
            return Stream.iterate(genT, t -> t)
                    .limit(length)
                    .map(Gen::sample)
                    .collect(Collectors.toList());
        };
    }

    /**
     * Generates a list of instances of type {@code T}. The resulting list contains at least
     * a single element.
     *
     * @param maxLength
     *      the maximum length of the generated {@code List}
     * @return
     *      {@code Gen<List<T>>} which uses the given {@code Gen<T>} to create elements of
     *      generated lists
     */
    public static <T> Gen<List<T>> nonEmptyListOf(final Gen<T> genT, final int maxLength) {
        final int sanitizedMaxLength = max(1, maxLength);
        return () -> {
            final int  length = (int) (random() * sanitizedMaxLength + 1);
            return Stream.iterate(genT, t -> t)
                    .limit(length)
                    .map(Gen::sample)
                    .collect(Collectors.toList());
        };
    }

    /**
     * Generates a list of length {@code length} of instances of type {@code T}.
     *
     * @param length
     *      the length of the resulting list
     * @return
     *      {@code Gen<List<T>>} which uses the given {@code Gen<T>} to create elements of
     *      generated lists. The list will contain exactly {@code length} elements
     */
    public static <T> Gen<List<T>> listOfN(final Gen<T> genT, final int length) {
        return () -> Stream.iterate(genT, t -> t)
                .limit(length)
                .map(Gen::sample)
                .collect(Collectors.toList());
    }

    /**
     * @return
     *      {@code Gen} that returns a non-negative integer
     */
    public static Gen<Integer> nonNegativeInteger() {
        final Random r = new Random();
        return () -> {
            final int i = r.nextInt();
            return i < 0 ? -(i + 1) : i;
        };
    }

    /**
     * @param start
     *      refers to the (including) endpoint on the left side
     * @param stopExclusive
     *      refers to the (excluding) endpoint on the right side
     * @return
     *      {@code Gen} that returns an {@link Integer} from within the range {@code [start, stopExclusive)}
     */
    public static Gen<Integer> choose(final int start, final int stopExclusive) {
        return nonNegativeInteger().map(n -> start + (n % (stopExclusive - start)));
    }

    /**
     * @return
     *      {@code Gen} that returns a {@link Double} from within the range {@code [0.0, 1.0]}
     */
    public static Gen<Double> normalizedDouble() {
        return nonNegativeInteger().map(n -> n / ((double) Integer.MAX_VALUE + 1));
    }

    /**
     * @param start
     *      refers to the (including) endpoint on the left side
     * @param stopExclusive
     *      refers to the (excluding) endpoint on the right side
     * @return
     *      {@code Gen} that returns a {@link Double} from within the range {@code [start, stopExclusive)}
     */
    public static Gen<Double> choose(final double start, final double stopExclusive) {
        return normalizedDouble().map(d -> start + d * (stopExclusive - start));
    }

    /**
     * @param probability
     *      represents the probability that the resulting {@code Gen} returns {@code genT1};
     *      vice versa, (1-probability) is the probability {@code genT2} is returned
     * @param genT1
     *      {@code Gen} that is returned with the given probability
     * @param genT2
     *      {@code Gen} that is returned with the probability of (1-probability)
     * @return
     *      {@code Gen} that returns {@code genT1} with probability {@code probability} and {@code genT2} with
     *      probability {@code (1-probability)}
     */
    public static <T> Gen<T> weighted(final double probability, final Gen<T> genT1, final Gen<T> genT2) {
        return normalizedDouble().flatMap(d -> d < probability ? genT1 : genT2);
    }

    /**
     * Recursive generator definitions suffer from the applicative order of execution in Java. Thus,
     * it is required to decouple the definition of a generator from the recursion using some kind of
     * laziness. {@code LazyGen} is a {@code Gen} that does just that.
     *
     * This class cannot be constructed directly. Please use {@link CoreGen#lazy(Supplier)}.
     */
    private static class LazyGen<T> implements Gen<T> {

        private final Supplier<T> supplier;

        private LazyGen(final Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @SuppressWarnings("unchecked")
        public T sample() {
            return ((Gen<T>) supplier.get()).sample();
        }
    }

    /**
     * Recursive generator definitions suffer from the applicative order of execution in Java. Thus,
     * it is required to decouple the definition of a generator from the recursion using some kind of
     * laziness. This method provides this kind of decoupling by using the internal class {@code LazyGen}.
     *
     * @param genU
     *      {@code Gen} that needs to be decoupled from a recursive {@code Gen} definition
     * @return
     *      {@code Gen} that wraps another {@code Gen}
     */
    public static <U> Gen<U> lazy(final Supplier<Gen<U>> genU) {
        return new LazyGen(genU);
    }
}