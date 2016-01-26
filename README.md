# Functional Design in Java 8

[![Build Status](https://travis-ci.org/mguenther/functional-generators.svg?branch=master)](https://travis-ci.org/mguenther/functional-generators.svg)

Functional-style Java 8 enables us to build powerful abstractions that lead to clean and readable code. This repository holds a couple of proof-of-concept implementations of various ideas from Functional Programming, in particular: `Gen<T>`, a monadic type for representing things that can generate objects, and `Property<T>`, which builds upon `Gen<T>` to implement a simple abstraction for expressing property-based tests. 

## The `Gen` Monad

The `Gen` monad is a small proof-of-concept that implements functional generators in Java, a concept that we usually find in property-based testing libraries. Property-based tests verify statements about the output of your code based on some input, where the same statement is verified for many different possible (admissible and inadmissible) inputs. Such tests rely heavily upon randomly generated objects and values. Even if you do not fully commit to property-based testing, having abstractions for generating random objects and values from your domain can simplify testing a lot:
                                                                                                                                                                                                                                                            
* In a typical, Unit-test-based setup, large portions of your tests consist of methods that construct test fixtures. Even if your test fixtures are parameterizable, it is cumbersome to combine fixtures. A generator-based approach can simplify your code base in that regard.
* Oftentimes you find yourself in need of a test fixture for some class, but the actual test logic only cares about a single parameter that goes into the constructor of that class. You still have to come up with values for the rest of the parameters, even if they provide no value for the test. Not only does this increase the code inside your test case, but it clouds the distinction between parameters that are relevant for that particular test and those that are not. A generator-based approach can help here as well.

### Design

`Gen<T>` is the functional interface that represents some thing that knows how to generate instances of class `T`. It implements default methods for a small set of combinators, namely `map`, `flatMap` and `suchThat`. The latter shows how one can implement combinators that play well together with other functional interfaces.

This proof-of-concept also comes with a variety of generators. `CoreGen` provides a set of core generators from which more complex generators can be built. Since these core generators are fundamental when combining generators, their names are not - unlike all other generators - suffixed with a `gen`. Classes `BooleanGen`, `CharGen`, `IntegerGen` and `StringGen` demonstrate how we can leverage `Gen<T>` and `CoreGen` to build some generators for classes from the JDK.

### Examples

This proof-of-concept also comes with some examples. Examples are located in the test sources under package `com.mgu.functional.generator.example`.

#### Generating Instances of Classes

Package `com.mgu.functional.generator.example.user` shows how we can use generators to produce instances of a simple user abstraction.

```java
public class UserGen {

    private static Gen<String> topLevelDomainNameGen() {
        return oneOf("com", "de", "at", "ch", "ca", "uk", "gov", "edu");
    }

    private static Gen<String> domainNameGen() {
        return oneOf("habitat47", "google", "spiegel");
    }

    private static Gen<String> validEmailGen(final Gen<String> firstNameGen, final Gen<String> lastNameGen) {
        return firstNameGen
                .flatMap(firstName -> oneOf("-", ".", "_")
                .flatMap(nameDelimiter -> lastNameGen
                .flatMap(lastName -> constant("@")
                .flatMap(at -> domainNameGen()
                .flatMap(domainName -> constant(".")
                .flatMap(domainDelimiter -> topLevelDomainNameGen()
                .map(topLevelDomain -> firstName + nameDelimiter + lastName + at + domainName + domainDelimiter + topLevelDomain)))))));
    }

    public static Gen<User> userGen() {
        return alphaNumStringGen(8)
                .flatMap(firstName -> alphaNumStringGen(8)
                .flatMap(lastName -> validEmailGen(constant(firstName), constant(lastName))
                .flatMap(email -> alphaNumStringGen(14)
                .map(hashedPassword -> new User(firstName + " " + lastName, email, hashedPassword)))));
    }
}
```
    
Noteworthy is the combination of generators using `flatMap` and a final `map`. Subsequent `flatMap`s are called within a `flatMap`, so that previously bound variables inside the lambda are not lost. Also note that the last combinator *must be* a `map` (we do not want to return another generator but a sample!) and that we have full access to all generated values up to this point.

#### Generating Recursive Data Structures

Package `com.mgu.functional.generator.example.tree` shows how we can leverage this proof-of-concept to generate recursive data structures.

```java
public class TreeGen {

    public static <T> Gen<Tree<T>> treeGen(final Gen<T> genT) {
        return lazy(() -> oneOf(leafGen(genT), nodeGen(genT)));
    }

    private static <T> Gen<Tree<T>> leafGen(final Gen<T> genT)  {
        return genT.map(Leaf::new);
    }

    private static <T> Gen<Tree<T>> nodeGen(final Gen<T> genT) {
        return listOf(treeGen(genT), 3).map(Node::new);
    }
}
```
    
Noteworthy is the use of `CoreGen.lazy` to decouple the `oneOf` generator from the recursive call. This is actually required to prevent mutually recursive generator definitions that exhaust the call stack.

## Property-Based Testing Using `Gen`

A property-based test verifies a statement about the output of your code based on some given input. The same statement - or property - is verified for many different possible inputs in order to find an input that **falsifies** the property. Thus, such tests rely heavily upon generators to randomly construct instances from a given domain. If the property cannot be falsified, the test passes and. In fact, if the generator is **total** with regard to the instances it generates, we actually have a **proof** that the property holds under the given circumstances.

### Design

Class `Property<T>` provides the means to create a property-based test with the `forAll` method. This method consumes a `Gen<T>`, which is able to generate instances of type `T`, and a `Predicate<T>` which encodes the actual logic on which instances of type `T` are tested. The test is not executed immediately: `forAll` acts as a type constructor and returns a `Property<T>` which closes over a runner function with type signature `Function<Integer, Result<T>>`. The runner function closes over the given `Gen<T>` and `Predicate<T>` and encodes the actual test data generation and probing logic. It consumes an `Integer` which represents the number of instances that ought to be generated. Upon execution, it collects intermediate results until the property has been tested exhaustively or has been falsified while trying. Thus, our design of `Property<T>` decouples the expression of a property from its actual evaluation.
 
 However, the `Property<T>` abstraction presented here is rather simple, as it does not leverage advanced techniques like **shrinking**. The shrinking technique tries to narrow down the problem space on instances that falsify the property, which is especially handy if the domain of generated values is large.

### Examples

This proof-of-concept also comes with some examples. Examples are located in the test sources under package `com.mgu.functional.property.example`.

#### Property-Based Tests for Fizzbuzz

Given the following implementation of the infamous Fizzbuzz problem

```java
public class Fizzbuzz {

    public String fizzbuzz(int upToInclusive) {
        return IntStream
                .range(0, upToInclusive+1)
                .boxed()
                .map(n -> map(n))
                .reduce("", (a, b) -> a + " " + b);
    }

    public String map(int n) {
        if (n % 15 == 0) {
            return "FizzBuzz";
        } else if (n % 3 == 0) {
            return "Fizz";
        } else if (n % 5 == 0) {
            return "Buzz";
        } else {
            return String.valueOf(n);
        }
    }
}
```

we can easily derive properties that must hold for the `map` function:

1. All multiples of three that are not multiples of five must be mapped to *Fizz*.
2. All multiples of five that are not multiples of three must be mapped to *Buzz*.
3. All multiples of fifteen must be mapped to *FizzBuzz*.
4. All numbers that are neither multiples of three nor multiples of five must be mapped to their identity.

Expressing the first property as a JUnit test is simple:

```java
@Test
public void mapShouldYieldFizzForMultiplesOfThreeButNotMultiplesOfFive() {

    Gen<Integer> multiplesOfThreeButNotFiveGen = 
        choose(start, end)
        .map(n -> 3 * n)
        .suchThat(n -> n % 5 != 0);
        
    forAll(multiplesOfThreeButNotFiveGen, n -> fizzbuzz.map(n).equals("Fizz")).check().assertPassed()
}
```

`assertPassed` will raise an `AssertionError` if the property has been falsified, and thus fail the unit test. The console output for this test is either `+ OK, passed 100 tests.` in the success case or `! Falsified after <number> tests on sample <object>.` in case the property has been falsified.

#### Ensuring Monoid Laws using Property-Based Tests

A monoid is 3-tuple of the form `(A, op, zero)`, where `A` denotes the type it operates on, `op` denotes a combinator operation for two instances of `A` and `zero` is neutral.
  
For a given monoid, the following properties must hold:

1. `op` is associative. Given a, b, and c, `op(a, op(b, c)) = op(op(a, b), c)` must hold.
2. `zero` is the neutral element. Given a, `op(a, zero) = a` must hold.

The structure of the monoid can be easily expressed using a Java interface:

```java
public interface Monoid<A> {
    A op(A a1, A a2);
    A zero();
}
```

A possible implementation of `Monoid<A>` might be a monoid that adds integers:

```java
public class IntAdditionMonoid implements Monoid<Integer> {

    @Override
    public Integer op(final Integer a1, final Integer a2) {
        return a1 + a2;
    }

    @Override
    public Integer zero() {
        return 0;
    }
}
```

Testing these monoid implementation - and in fact all, **all** monoid implementations - can be achieved by expressing the aforementioned properties as part of a Unit test. Have a look at this test class.

```java
public class MonoidTest {

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
```

## License

This work is released under the terms of the MIT license.