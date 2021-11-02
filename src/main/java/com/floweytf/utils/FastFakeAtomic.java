package com.floweytf.utils;

public class FastFakeAtomic<T> {
    private T t;

    public void set(T v) {
        t = v;
    }

    public T get() {
        return t;
    }
}
