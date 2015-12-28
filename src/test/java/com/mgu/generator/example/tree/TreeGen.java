package com.mgu.generator.example.tree;

import com.mgu.generator.Gen;

import static com.mgu.generator.CoreGen.lazy;
import static com.mgu.generator.CoreGen.listOf;
import static com.mgu.generator.CoreGen.oneOf;

/**
 * {@code Gen} implementations for the tree example.
 *
 * @author Markus Günther (markus.guenther@gmail.com)
 */
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