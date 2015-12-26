package com.mgu.test.generator.example.tree;

import org.junit.Test;

import static com.mgu.test.generator.CoreGen.choose;
import static com.mgu.test.generator.CoreGen.listOfN;
import static com.mgu.test.generator.example.tree.TreeGen.treeGen;

public class TreeTest {

    @Test
    public void lala() {
        final Tree<Integer> tree = treeGen(choose(1, 2)).sample();
        System.out.println(tree.size());

        listOfN(choose(1, 100)
                .suchThat(n -> n % 2 == 0), 10)
                .sample()
                .forEach(n -> System.out.println(n));

    }
}