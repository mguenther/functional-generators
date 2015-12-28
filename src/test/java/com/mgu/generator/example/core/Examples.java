package com.mgu.generator.example.core;

import com.mgu.generator.Gen;
import org.junit.Test;

import static com.mgu.generator.CharGen.alphaCharGen;
import static com.mgu.generator.CharGen.alphaLowerCharGen;
import static com.mgu.generator.CharGen.alphaNumCharGen;
import static com.mgu.generator.CharGen.alphaUpperCharGen;
import static com.mgu.generator.CharGen.numCharGen;
import static com.mgu.generator.CoreGen.choose;
import static com.mgu.generator.CoreGen.oneOf;
import static com.mgu.generator.Prop.forAll;
import static org.junit.Assert.assertTrue;

public class Examples {

    @Test
    public void usingChooseAndVerifyingTheResultsUsingPropertyBasedChecking() {
        assertTrue(forAll(choose(2, 10), n -> n >= 2 && n < 10));
        assertTrue(forAll(choose(5.1, 5.4), n -> n >= 5.1 && n < 5.4));
    }

    @Test
    public void combiningOneOfWithChoose() {
        final Gen<Integer> excludingZeroGen = oneOf(choose(-10, -1), choose(1, 10));
        assertTrue(forAll(excludingZeroGen, n -> n != 0));
    }

    @Test
    public void usingCharGensToConstructStrings() {
        final Gen<String> stringGen = () ->
            new String(new char [] {
                    numCharGen().sample(),
                    alphaUpperCharGen().sample(),
                    alphaLowerCharGen().sample(),
                    alphaCharGen().sample(),
                    alphaNumCharGen().sample()
            });
        System.out.println(stringGen.sample());
    }

    @Test
    public void usingOneOfToObtainRandomVowels() {
        final Gen<Character> vowelGen = oneOf('a', 'e', 'i', 'o', 'u');
        System.out.println(vowelGen.sample());
    }
}