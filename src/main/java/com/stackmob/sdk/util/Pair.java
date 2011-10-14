package com.stackmob.sdk.util;

public class Pair<T, U> {
    private T one;
    private U two;
    public Pair(T one, U two) {
        this.one = one;
        this.two = two;
    }

    public T getFirst() {
        return one;
    }

    public U getSecond() {
        return two;
    }
}
