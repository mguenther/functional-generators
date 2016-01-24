package com.mgu.functional.property.example.monoid;

import com.mgu.functional.Predicate3;
import org.junit.Test;

import java.util.function.Predicate;

import static com.mgu.functional.generator.CoreGen.choose;
import static com.mgu.functional.generator.StringGen.alphaNumStringGen;
import static com.mgu.functional.property.Property.forAll;

public class MonoidTest {

    @Test
    public void stringMonoidRespectsMonoidLaws() {
        forAll(alphaNumStringGen(8), monoidRespectsLeftIdentity(new StringMonoid()))
                .check()
                .assertPassed();
        forAll(alphaNumStringGen(8), monoidRespectsRightIdentity(new StringMonoid()))
                .check()
                .assertPassed();
        forAll(alphaNumStringGen(8), alphaNumStringGen(8), alphaNumStringGen(8), monoidIsAssociative(new StringMonoid()))
                .check()
                .assertPassed();
    }

    @Test
    public void intAdditionMonoidRespectsMonoidLaws() {
        forAll(choose(-127, 127), monoidRespectsLeftIdentity(new IntAdditionMonoid()))
                .check()
                .assertPassed();
        forAll(choose(-127, 127), monoidRespectsRightIdentity(new IntAdditionMonoid()))
                .check()
                .assertPassed();
        forAll(choose(-127, 127), choose(-127, 127), choose(-127, 127), monoidIsAssociative(new IntAdditionMonoid()))
                .check()
                .assertPassed();
    }

    private <T> Predicate<T> monoidRespectsLeftIdentity(final Monoid<T> monoid) {
        return t -> monoid.op(monoid.zero(), t).equals(t);
    }

    private <T> Predicate<T> monoidRespectsRightIdentity(final Monoid<T> monoid) {
        return t -> monoid.op(t, monoid.zero()).equals(t);
    }

    private <T> Predicate3<T, T, T> monoidIsAssociative(final Monoid<T> monoid) {
        return (a, b, c) -> {
            T sampleLeft = monoid.op(monoid.op(a, b), c);
            T sampleRight = monoid.op(a, monoid.op(b, c));
            return sampleLeft.equals(sampleRight);
        };
    }
}