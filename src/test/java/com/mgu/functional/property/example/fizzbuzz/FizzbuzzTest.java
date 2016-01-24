package com.mgu.functional.property.example.fizzbuzz;

import com.mgu.functional.generator.Gen;
import org.junit.Test;

import static com.mgu.functional.generator.CoreGen.choose;
import static com.mgu.functional.property.Property.forAll;

public class FizzbuzzTest {

    private final Fizzbuzz fizzbuzz = new Fizzbuzz(); // object-under-test

    private Gen<Integer> multiplesOfThreeButNotFiveGen = choose(-1000, 1000).map(n -> 3 * n).suchThat(n -> n % 5 != 0);

    private Gen<Integer> multiplesOfFiveButNotThreeGen = choose(-1000, 1000).map(n -> 5 * n).suchThat(n -> n % 3 != 0);

    private Gen<Integer> multiplesOfFifteenGen = choose(-1000, 1000).map(n -> 15 * n);

    private Gen<Integer> excludeMultiplesOfThreeOrFiveGen = choose(-1000, 1000).suchThat(n -> n % 5 != 0 && n % 3 != 0);

    @Test
    public void mapShouldYieldFizzForNumberThatIsNotDivisibleBy5AndMultipliedBy3() {
        forAll(multiplesOfThreeButNotFiveGen, n -> fizzbuzz.map(n).equals("Fizz")).check().assertPassed();
    }

    @Test
    public void mapShouldYieldBuzzForNumberThatIsNotDivisibleBy3AndMultipliedBy5() {
        forAll(multiplesOfFiveButNotThreeGen, n -> fizzbuzz.map(n).equals("Buzz")).check().assertPassed();
    }

    @Test
    public void mapShouldYieldFizzBuzzForNumbersThatAreDivisibleByFifteen() {
        forAll(multiplesOfFifteenGen, n -> fizzbuzz.map(n).equals("FizzBuzz")).check().assertPassed();
    }

    @Test
    public void mapShouldYieldTheIdentityOfAnyNumberThatIsNotDivisibleByThreeOrFive() {
        forAll(excludeMultiplesOfThreeOrFiveGen, n -> fizzbuzz.map(n).endsWith(String.valueOf(n))).check().assertPassed();
    }
}