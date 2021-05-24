package com.floweytf.utils.streams;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class OutputStreamUtils {
    private final OutputStream stream;
    private Gson gson = new Gson();
    private OutputStreamUtils(OutputStream stream) {
        this.stream = stream;
    }

    // get from variety of stuff
    public static OutputStreamUtils getStreamString() {
        return new OutputStreamUtils(new ByteArrayOutputStream());
    }

    public static OutputStreamUtils getStreamFile(File f) throws FileNotFoundException {
        return new OutputStreamUtils(new FileOutputStream(f));
    }

    public static OutputStreamUtils getStreamFile(String s) throws FileNotFoundException {
        return getStreamFile(new File(s));
    }

    public static OutputStreamUtils getStream(Path p) throws FileNotFoundException {
        return getStreamFile(p.toFile());
    }

    public static OutputStreamUtils getStream(OutputStream s) {
        return new OutputStreamUtils(s);
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
        if(stream instanceof ByteArrayOutputStream)
            return new String(((ByteArrayOutputStream)stream).toByteArray());
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
