package com.floweytf.utils.data;

import com.floweytf.utils.streams.stdstreams.StandardByteReader;
import com.floweytf.utils.streams.stdstreams.StandardByteWriter;

import java.io.IOException;

public class IntTag extends PrimitiveTag<Integer> {
    public IntTag(Integer init) {
        super(init);
    }

    public IntTag() {
        super();
    }

    @Override
    public void readFrom(StandardByteReader reader) throws IOException {
        delegate = reader.readInt();
    }

    @Override
    public void writeTo(StandardByteWriter writer) throws IOException {
        writer.write(delegate);
    }

    @Override
    public byte getType() {
        return TAG_INT;
    }
}
