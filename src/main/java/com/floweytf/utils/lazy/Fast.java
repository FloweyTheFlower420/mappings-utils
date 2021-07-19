package com.floweytf.utils.lazy;

import java.util.function.Supplier;

public final class Fast<T> implements Lazy<T> {
    private Supplier<T> supplier;
    private T instance;

    Fast(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public final T get() {
        if (supplier != null) {
            instance = supplier.get();
            supplier = null;
        }
        return instance;
    }
}
