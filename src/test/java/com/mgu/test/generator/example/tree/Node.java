package com.mgu.test.generator.example.tree;

import java.util.Collections;
import java.util.List;

public class Node<T> implements Tree<T> {

    private final List<Tree<T>> children;

    public Node(final List<Tree<T>> children) {
        this.children = children;
    }

    @Override
    public int size() {
        return children
                .stream()
                .map(child -> child.size())
                .reduce(1, (a, b) -> a + b);
    }

    public List<Tree<T>> getChildren() {
        return Collections.unmodifiableList(children);
    }
}