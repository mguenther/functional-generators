# Functional Generators in Java 8

[![Build Status](https://travis-ci.org/mguenther/functional-generators.svg?branch=master)](https://travis-ci.org/mguenther/functional-generators.svg)

Functional-style Java 8 enables us to build powerful abstractions that lead to clean and readable code. This is a small proof-of-concept that implements functional generators in Java, a concept that we usually find in property-based testing libraries. Property-based tests verify statements about the output of your code based on some input, where the same statement is verified for many different possible (admissible and inadmissible) inputs. Such tests rely heavily upon randomly generated objects and values. Even if you do not fully commit to property-based testing, having abstractions for generating random objects and values from your domain can simplify testing a lot:
                                                                                                                                                                                                                                                            
* In a typical, Unit-test-based setup, large portions of your tests consist of methods that construct test fixtures. Even if your test fixtures are parameterizable, it is cumbersome to combine fixtures. A generator-based approach can simplify your code base in that regard.
* Oftentimes you find yourself in need of a test fixture for some class, but the actual test logic only cares about a single parameter that goes into the constructor of that class. You still have to come up with values for the rest of the parameters, even if they provide no value for the test. Not only does this increase the code inside your test case, but it clouds the distinction between parameters that are relevant for that particular test and those that are not. A generator-based approach can help here as well.

## Design

`Gen<T>` is the functional interface that represents some thing that knows how to generate instances of class `T`. It implements default methods for a small set of combinators, namely `map`, `flatMap` and `suchThat`. The latter shows one can provide combinators that play well together with other functional interfaces.

This proof-of-concept also comes with a variety of generators. `CoreGen` provides a set of core generators from which more complex generators can be built. Since these core generators are fundamental when combining generators, their names are not - unlike all other generators - suffixed with a `gen`. Classes `BooleanGen`, `CharGen`, `IntegerGen` and `StringGen` demonstrate how we can leverage `Gen<T>` and `CoreGen` to build some generators for classes from the JDK.

## Examples

This proof-of-concept also comes with some examples. Examples are located in the test sources under package `com.mgu.generator.example`.

### Generating Instances of Classes

Package `com.mgu.generator.example.user` shows how we can use generators to produce instances of a simple user abstraction.

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
    
Noteworthy is the combination of generators using `flatMap` and a final `map`. Subsequent `flatMap`s are called within a `flatMap`, so that previously bound variables inside the lambda are not lost. Also note that the last combinator *must be* a `map` (we do not want to return another generator but a sample!) and that we have full access to all generated values up to this point.

### Generating Recursive Data Structures

Package `com.mgu.generator.example.tree` shows how we can leverage this proof-of-concept to generate recursive data structures.

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
    
Noteworthy is the use of `CoreGen.lazy` to decouple the `oneOf` generator from the recursive call. This is actually required to prevent mutually recursive generator definitions that exhaust the call stack.

## License

This work is released under the terms of the MIT license.