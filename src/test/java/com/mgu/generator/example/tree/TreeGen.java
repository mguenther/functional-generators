package com.mgu.generator.example.tree;

import com.mgu.generator.CoreGen;
import com.mgu.generator.Gen;

/**
 * {@code Gen} implementations for the tree example.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class TreeGen {

    public static <T> Gen<Tree<T>> treeGen(final Gen<T> genT) {
        return CoreGen.lazy(() -> CoreGen.oneOf(leafGen(genT), nodeGen(genT)));
    }

    private static <T> Gen<Tree<T>> leafGen(final Gen<T> genT)  {
        return genT.map(Leaf::new);
    }

    private static <T> Gen<Tree<T>> nodeGen(final Gen<T> genT) {
        return CoreGen.listOf(treeGen(genT), 3).map(Node::new);
    }
}