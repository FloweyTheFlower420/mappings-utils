package com.floweytf.utils.streams.stdstreams;

import java.io.OutputStream;

public class StandardByteWriter extends BasicStandardWriter<OutputStream> {
    public StandardByteWriter(OutputStream os) {
        super(os, (OutputStream::write));
    }
}
