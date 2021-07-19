package com.floweytf.utils.lazy;

import java.util.function.Supplier;

public interface Lazy<T> extends Supplier<T> {
    static <T> Lazy<T> of(Supplier<T> supplier) {
        return new Fast<>(supplier);
    }

    static <T> Lazy<T> concurrentOf(Supplier<T> supplier) {
        return new Concurrent<>(supplier);
    }
}