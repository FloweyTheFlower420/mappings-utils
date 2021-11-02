package com.floweytf.utils.data;


import com.floweytf.utils.streams.stdstreams.StandardByteReader;
import com.floweytf.utils.streams.stdstreams.StandardByteWriter;

import java.io.IOException;

public abstract class PrimitiveTag <T> extends AbstractTag {
    protected T delegate;

    public PrimitiveTag(T init) {
        delegate = init;
    }

    public PrimitiveTag() {

    }

    public void set(T val) {
        delegate = val;
    }

    public T get() {
        return delegate;
    }
    public abstract void readFrom(StandardByteReader reader) throws IOException;
    public abstract void writeTo(StandardByteWriter writer) throws IOException;
}
