package com.floweytf.utils.builders;

import com.floweytf.utils.Utils;

import java.util.*;
import java.util.function.Supplier;

/* cursed map builder
 * please dont break this :(
 */
public class MapBuilder <K, V, T extends Map<K, V>> {
    private Map<K, V> type;

    private MapBuilder(Supplier<T> supplier) {
        type = supplier.get();
    }

    public static <A, B, C extends Map<A, B>> MapBuilder<A, B, C> get(Supplier<C> supplier) {
        return new MapBuilder<>(supplier);
    }

    public static <A, B> MapBuilder<A, B, Map<A, B>> getDefault() {
        return get(HashMap::new);
    }

    public MapBuilder<K, V, T> add(K key, V val) {
        type.put(key, val);
        return this;
    }

    public T build() {
        return (T)type;
    }
}