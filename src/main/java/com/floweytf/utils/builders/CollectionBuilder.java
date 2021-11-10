package com.floweytf.utils.builders;

import java.util.Collection;
import java.util.HashSet;
import java.util.function.Supplier;

/* cursed collection builder
 * please dont break this :(
 */
public class CollectionBuilder <V, T extends Collection<V>> {
    private final Collection<V> type;

    private CollectionBuilder(Supplier<T> supplier) {
        type = supplier.get();
    }

    public static <A, B extends Collection<A>> CollectionBuilder<A, B> get(Supplier<B> supplier) {
        return new CollectionBuilder<>(supplier);
    }

    public static <A> CollectionBuilder<A, Collection<A>> getDefault() {
        return new CollectionBuilder<>(HashSet::new);
    }

    public void add(V val) {
        type.add(val);
    }

    public T build() {
        return (T) type;
    }
}