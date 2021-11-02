package com.floweytf.utils.data;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ObjectTag extends CollectionTag implements Iterable<Map.Entry<String, AbstractTag>>{
    private final Map<String, AbstractTag> delegate = new HashMap<>();

    public int size() {
        return delegate.size();
    }
    
    public void put(String key, AbstractTag tag) {
        delegate.put(key, tag);
    }

    public AbstractTag get(String key) {
        return delegate.get(key);
    }

    public void remove(String key) {
        delegate.remove(key);
    }

    public ObjectTag getObject(String key) {
        return get(key).asObject();
    }

    public ArrayTag getArray(String key) {
        return get(key).asArray();
    }

    public ByteTag getByte(String key) {
        return get(key).asByte();
    }

    public ShortTag getShort(String key) {
        return get(key).asShort();
    }

    public IntTag getInt(String key) {
        return get(key).asInt();
    }

    public LongTag getLong(String key) {
        return get(key).asLong();
    }

    public FloatTag getFloat(String key) {
        return get(key).asFloat();
    }

    public DoubleTag getDouble(String key) {
        return get(key).asDouble();
    }

    public StringTag getString(String key) {
        return get(key).asString();
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @NotNull
    @Override
    public Iterator<Map.Entry<String, AbstractTag>> iterator() {
        return delegate.entrySet().iterator();
    }

    @Override
    public byte getType() {
        return TAG_OBJECT;
    }
}
