package com.floweytf.utils.data;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayTag extends CollectionTag implements Iterable<AbstractTag> {
    private final List<AbstractTag> delegate = new ArrayList<>();

    public int size() {
        return delegate.size();
    }

    public void add(AbstractTag tag) {
        delegate.add(tag);
    }

    public void set(int index, AbstractTag tag) {
        delegate.set(index, tag);
    }

    public AbstractTag get(int index) {
        return delegate.get(index);
    }

    public void remove(int index) {
        delegate.remove(index);
    }

    public void remove(AbstractTag tag) {
        delegate.remove(tag);
    }

    public ObjectTag getObject(int index) {
        return get(index).asObject();
    }

    public ArrayTag getArray(int index) {
        return get(index).asArray();
    }

    public ByteTag getByte(int index) {
        return get(index).asByte();
    }

    public ShortTag getShort(int index) {
        return get(index).asShort();
    }

    public IntTag getInt(int index) {
        return get(index).asInt();
    }

    public LongTag getLong(int index) {
        return get(index).asLong();
    }

    public FloatTag getFloat(int index) {
        return get(index).asFloat();
    }

    public DoubleTag getDouble(int index) {
        return get(index).asDouble();
    }

    public StringTag getString(int index) {
        return get(index).asString();
    }

    /**
     * Returns an iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @NotNull
    @Override
    public Iterator<AbstractTag> iterator() {
        return delegate.iterator();
    }

    @Override
    public byte getType() {
        return TAG_ARRAY;
    }
}
