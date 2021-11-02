package com.floweytf.utils.data;

import com.floweytf.utils.streams.stdstreams.StandardByteReader;
import com.floweytf.utils.streams.stdstreams.StandardByteWriter;

import java.io.IOException;

public class FloatTag extends PrimitiveTag<Float> {
    public FloatTag(Float init) {
        super(init);
    }

    public FloatTag() {
        super();
    }

    @Override
    public void readFrom(StandardByteReader reader) throws IOException {
        delegate = reader.readFloat();
    }

    @Override
    public void writeTo(StandardByteWriter writer) throws IOException {
        writer.write(delegate);
    }

    @Override
    public byte getType() {
        return TAG_FLOAT;
    }
}
