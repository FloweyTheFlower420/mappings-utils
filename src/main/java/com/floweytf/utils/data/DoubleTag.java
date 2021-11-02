package com.floweytf.utils.data;

import com.floweytf.utils.streams.stdstreams.StandardByteReader;
import com.floweytf.utils.streams.stdstreams.StandardByteWriter;

import java.io.IOException;

public class DoubleTag extends PrimitiveTag<Double> {
    public DoubleTag(Double init) {
        super(init);
    }

    public DoubleTag() {
        super();
    }

    @Override
    public void readFrom(StandardByteReader reader) throws IOException {
        delegate = reader.readDouble();
    }

    @Override
    public void writeTo(StandardByteWriter writer) throws IOException {
        writer.write(delegate);
    }

    @Override
    public byte getType() {
        return TAG_DOUBLE;
    }
}
