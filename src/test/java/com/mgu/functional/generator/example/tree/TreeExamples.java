package com.mgu.functional.generator.example.tree;

import org.junit.Test;

import static com.mgu.functional.generator.CoreGen.choose;
import static com.mgu.functional.generator.example.tree.TreeGen.treeGen;

public class TreeExamples {

    @Test
    public void generatingRandomTreeAndPrintingItsSize() {
        final Tree<Integer> tree = treeGen(choose(1, 2)).sample();
        System.out.println(tree.size());
    }
}