package com.floweytf.utils.data;

import com.floweytf.utils.streams.stdstreams.StandardByteReader;
import com.floweytf.utils.streams.stdstreams.StandardByteWriter;

import java.io.IOException;

public class ShortTag extends PrimitiveTag<Short> {
    public ShortTag(Short init) {
        super(init);
    }

    public ShortTag() {
        super();
    }

    @Override
    public void readFrom(StandardByteReader reader) throws IOException {
        delegate = reader.readShort();
    }

    @Override
    public void writeTo(StandardByteWriter writer) throws IOException {
        writer.write(delegate);
    }

    @Override
    public byte getType() {
        return TAG_SHORT;
    }
}
