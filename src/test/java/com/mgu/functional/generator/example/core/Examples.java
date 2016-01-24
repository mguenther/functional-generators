package com.mgu.functional.generator.example.core;

import com.mgu.functional.generator.Gen;
import org.junit.Test;

import static com.mgu.functional.generator.CharGen.alphaCharGen;
import static com.mgu.functional.generator.CharGen.alphaLowerCharGen;
import static com.mgu.functional.generator.CharGen.alphaNumCharGen;
import static com.mgu.functional.generator.CharGen.alphaUpperCharGen;
import static com.mgu.functional.generator.CharGen.numCharGen;
import static com.mgu.functional.generator.CoreGen.choose;
import static com.mgu.functional.generator.CoreGen.oneOf;
import static com.mgu.functional.property.Property.forAll;

public class Examples {

    @Test
    public void usingChooseAndVerifyingTheResultsUsingPropertyBasedChecking() {
        forAll(choose(2, 10), n -> n >= 2 && n < 10).check().assertPassed();
        forAll(choose(5.1, 5.4), n -> n >= 5.1 && n < 5.4).check().assertPassed();
    }

    @Test
    public void combiningOneOfWithChoose() {
        final Gen<Integer> excludingZeroGen = oneOf(choose(-10, -1), choose(1, 10));
        forAll(excludingZeroGen, n -> n != 0).check().assertPassed();
    }

    @Test
    public void usingCharGensToConstructStrings() {
        final Gen<String> stringGen = numCharGen()
                .flatMap(numChar -> alphaUpperCharGen()
                .flatMap(alphaUpperChar -> alphaLowerCharGen()
                .flatMap(alphaLowerChar -> alphaCharGen()
                .flatMap(alphaChar -> alphaNumCharGen()
                .map(alphaNumChar -> "" + numChar + alphaUpperChar + alphaLowerChar + alphaChar)))));
        System.out.println(stringGen.sample());
    }

    @Test
    public void usingOneOfToObtainRandomVowels() {
        final Gen<Character> vowelGen = oneOf('a', 'e', 'i', 'o', 'u');
        System.out.println(vowelGen.sample());
    }
}