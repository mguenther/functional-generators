package com.mgu.generator;

import org.junit.Test;

import java.util.stream.Stream;

import static com.mgu.generator.CoreGen.choose;
import static org.junit.Assert.assertTrue;

public class CoreGenTest {

    @Test
    public void listOfShouldGenerateListOfValuesWithinRange() {
        final int maximumListSize = 5;

        assertTrue(Stream.generate(() -> CoreGen.listOf(CoreGen.choose(1, 10), maximumListSize).sample())
                .limit(100)
                .map(l -> l.size())
                .allMatch(size -> size >= 0 && size <= maximumListSize));
    }

    @Test
    public void listOfShouldGenerateListsThatMayBeEmpty() {
        assertTrue(Stream.generate(() -> CoreGen.listOf(CoreGen.choose(1, 10), 0).sample())
                .limit(100)
                .map(l -> l.size())
                .allMatch(size -> size == 0));
    }

    @Test
    public void nonEmptyListOfShouldGenerateNonEmptyLists() {
        assertTrue(Stream.generate(() -> CoreGen.nonEmptyListOf(CoreGen.choose(1, 10), 0).sample())
                .limit(100)
                .map(l -> l.size())
                .allMatch(size -> size > 0));
    }

    @Test
    public void listOfNShouldGenerateExactlyListsOfSizeN() {
        assertTrue(Stream.generate(() -> CoreGen.listOfN(CoreGen.choose(1, 10), 10).sample())
                .limit(100)
                .map(l -> l.size())
                .allMatch(size -> size == 10));
    }
}