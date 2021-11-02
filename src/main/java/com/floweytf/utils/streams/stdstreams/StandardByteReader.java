package com.floweytf.utils.streams.stdstreams;

import java.io.InputStream;

public class StandardByteReader extends BasicStandardReader<InputStream> {
    public StandardByteReader(InputStream is) {
        super(is, InputStream::read);
    }
}
