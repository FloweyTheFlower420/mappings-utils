package com.floweytf.utils.lazy;

import java.util.function.Supplier;

public final class Concurrent<T> implements Lazy<T> {
    private volatile Object lock = new Object();
    private volatile Supplier<T> supplier;
    private volatile T instance;

    Concurrent(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    @Override
    public final T get() {
        Object localLock = this.lock;
        if (supplier != null) {
            synchronized (localLock) {
                if (supplier != null) {
                    instance = supplier.get();
                    supplier = null;
                    this.lock = null;
                }
            }
        }
        return instance;
    }
}
