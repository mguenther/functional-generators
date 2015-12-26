package com.mgu.test.generator.example.tree;

import com.mgu.test.generator.CoreGen;
import com.mgu.test.generator.Gen;

/**
 * {@code Gen} implementations for the tree example.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
public class TreeGen {

    public static <T> Gen<Tree<T>> treeGen(final Gen<T> genT) {
        return CoreGen.lazy(() -> CoreGen.oneOf(leafGen(genT), nodeGen(genT)));
    }

    public static <T> Gen<Tree<T>> leafGen(final Gen<T> genT)  {
        return genT.map(Leaf::new);
    }

    public static <T> Gen<Tree<T>> nodeGen(final Gen<T> genT) {
        return CoreGen.listOf(treeGen(genT), 3).map(Node::new);
    }
}