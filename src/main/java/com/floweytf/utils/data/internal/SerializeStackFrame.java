package com.floweytf.utils.data.internal;

import com.floweytf.utils.data.CollectionTag;

import java.util.Iterator;

public class SerializeStackFrame {
    public CollectionTag tag;
    public Iterator<?> iterator;

    public SerializeStackFrame(CollectionTag tag, Iterator<?> iterator) {
        this.tag = tag;
        this.iterator = iterator;
    }
}
