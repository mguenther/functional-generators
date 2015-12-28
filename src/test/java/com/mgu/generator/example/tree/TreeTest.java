package com.mgu.generator.example.tree;

import com.mgu.generator.CoreGen;
import org.junit.Test;

import static com.mgu.generator.CoreGen.choose;
import static com.mgu.generator.example.tree.TreeGen.treeGen;

public class TreeTest {

    @Test
    public void lala() {
        final Tree<Integer> tree = treeGen(CoreGen.choose(1, 2)).sample();
        System.out.println(tree.size());

        CoreGen.listOfN(CoreGen.choose(1, 100)
                .suchThat(n -> n % 2 == 0), 10)
                .sample()
                .forEach(n -> System.out.println(n));

    }
}