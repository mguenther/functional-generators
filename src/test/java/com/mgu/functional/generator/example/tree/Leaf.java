package com.mgu.functional.generator.example.tree;

public class Leaf<T> implements Tree<T> {

    private final T value;

    public Leaf(final T value) {
        this.value = value;
    }

    @Override
    public int size() {
        return 1;
    }

    public T getValue() {
        return this.value;
    }
}