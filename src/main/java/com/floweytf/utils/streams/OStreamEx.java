package com.floweytf.utils.streams;

import com.google.gson.Gson;

import java.io.*;
import java.nio.file.Path;

public class OStreamEx {
    private final OutputStream stream;
    private final Gson gson = new Gson();

    private OStreamEx(OutputStream stream) {
        this.stream = stream;
    }

    // get from variety of stuff
    public static OStreamEx getStreamString() {
        return new OStreamEx(new ByteArrayOutputStream());
    }

    public static OStreamEx getStream(File f) throws FileNotFoundException {
        return new OStreamEx(new FileOutputStream(f));
    }

    public static OStreamEx getStreamFile(String s) throws FileNotFoundException {
        return getStream(new File(s));
    }

    public static OStreamEx getStream(Path p) throws FileNotFoundException {
        return getStream(p.toFile());
    }

    public static OStreamEx getStream(OutputStream s) {
        return new OStreamEx(s);
    }

    // write to variety of stuff
    public OutputStream toStream() {
        return stream;
    }

    public BufferedOutputStream toStreamBuffered() {
        return new BufferedOutputStream(stream);
    }

    public OutputStreamWriter toWriterBuffered() {
        return new OutputStreamWriter(stream);
    }

    @Override
    public String toString() {
        if (stream instanceof ByteArrayOutputStream) {
            return ((ByteArrayOutputStream) stream).toString();
        }
        throw new IllegalStateException("Stream is not created to write to string!");
    }

    public void write(Object s) {
        try {
            stream.write(s.toString().getBytes());
        }
        catch (IOException e) {
            throw new IllegalStateException("Something went wrong!", e);
        }
    }

    public void writeln(Object s) {
        write(s.toString() + '\n');
    }
}
