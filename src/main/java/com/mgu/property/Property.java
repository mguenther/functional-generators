package com.mgu.property;

import com.mgu.functional.Predicate3;
import com.mgu.functional.Tuple2;
import com.mgu.functional.Tuple3;
import com.mgu.generator.Gen;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.mgu.generator.CoreGen.listOfN;
import static com.mgu.generator.TupleGen.tuple2Gen;
import static com.mgu.generator.TupleGen.tuple3Gen;
import static com.mgu.property.Result.falsified;
import static com.mgu.property.Result.passed;

public class Property<T> {

    private static final int DEFAULT_NUMBER_OF_INSTANCES = 100;

    private final Function<Integer, Result<T>> runner;

    private Property(Function<Integer, Result<T>> runner) {
        this.runner = runner;
    }

    public Result<T> check() {
        return check(DEFAULT_NUMBER_OF_INSTANCES);
    }

    public Result<T> check(final int numberOfInstances) {
        final Result<T> result = runner.apply(numberOfInstances);
        if (result.passed()) {
            System.out.println("+ OK, passed " + numberOfInstances + " tests.");
        } else {
            System.out.println("! Falsified after " + result.numberOfPassedTests() + " tests on sample " + result.failedOn().get()); // safe get
        }
        return result;
    }

    public static <T> Property<T> forAll(final Gen<T> gen, final Predicate<T> predicate) {
        final Function<Integer, Result<T>> f = n -> {
            final Deque<T> probedSamples = new LinkedList<>();
            final boolean successful = listOfN(gen, n)
                    .sample()
                    .stream()
                    .peek(sample -> probedSamples.addLast(sample))
                    .allMatch(predicate::test);

            if (successful) {
                return passed(probedSamples.size(), null);
            } else {
                return falsified(probedSamples.size()-1, probedSamples.getLast());
            }
        };
        return new Property<>(f);
    }

    public static <T, U> Property<Tuple2<T, U>> forAll(final Gen<T> genT, final Gen<U> genU, final BiPredicate<T, U> predicate) {
        return forAll(
                tuple2Gen(genT, genU),
                tuple -> predicate.test(tuple.a, tuple.b));
    }

    public static <T, U, V> Property<Tuple3<T, U, V>> forAll(final Gen<T> genT, final Gen<U> genU, final Gen<V> genV, final Predicate3<T, U, V> predicate) {
        return forAll(
                tuple3Gen(genT, genU, genV),
                tuple -> predicate.test(tuple.a, tuple.b, tuple.c));
    }
}