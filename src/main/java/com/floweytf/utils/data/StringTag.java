package com.floweytf.utils.data;

import com.floweytf.utils.streams.stdstreams.StandardByteReader;
import com.floweytf.utils.streams.stdstreams.StandardByteWriter;

import java.io.IOException;

public class StringTag extends PrimitiveTag<String> {
    public StringTag(String init) {
        super(init);
    }

    public StringTag() {
        super();
    }

    @Override
    public void readFrom(StandardByteReader reader) throws IOException {
        delegate = reader.readString();
    }

    @Override
    public void writeTo(StandardByteWriter writer) throws IOException {
        writer.write(delegate);
    }

    @Override
    public byte getType() {
        return TAG_STRING;
    }
}
