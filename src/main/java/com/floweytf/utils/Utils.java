package com.floweytf.utils;

public class Utils {
    public void rethrow(ThrowingMethod method) {
        try {
            method.run();
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }
}
