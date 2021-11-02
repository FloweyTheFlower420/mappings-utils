package com.floweytf.utils.data.internal;

import com.floweytf.utils.data.CollectionTag;

public class DeserializeStackFrame {
    public CollectionTag tag;
    public int index;
    public int size;

    public DeserializeStackFrame(CollectionTag tag, int index, int size) {
        this.tag = tag;
        this.index = index;
        this.size = size;
    }
}
