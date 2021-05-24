package com.floweytf.utils.streams;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.file.Path;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InputStreamUtils {
    private final InputStream stream;
    private final Gson gson = new Gson();
    private InputStreamUtils(InputStream stream) {
        this.stream = stream;
    }

    // get from variety of stuff
    public static InputStreamUtils getStream(String s) {
        return new InputStreamUtils(new ByteArrayInputStream(s.getBytes()));
    }

    public static InputStreamUtils getStream(URL u) throws IOException {
        return new InputStreamUtils(u.openStream());
    }

    public static InputStreamUtils getStream(File f) throws FileNotFoundException {
        return new InputStreamUtils(new FileInputStream(f));
    }

    public static InputStreamUtils getStreamURL(String s) throws IOException {
        return getStream(new URL(s));
    }

    public static InputStreamUtils getStreamFile(String s) throws FileNotFoundException {
        return getStream(new File(s));
    }

    public static InputStreamUtils getStream(Path p) throws FileNotFoundException {
        return getStream(p.toFile());
    }

    public static InputStreamUtils getStreamClassPath(String s) throws ClassNotFoundException {
        return getStream(InputStreamUtils.class.getResourceAsStream(s));
    }

    public static InputStreamUtils getStream(InputStream s) {
        return new InputStreamUtils(s);
    }

    // write to variety of stuff
    public InputStream toStream() {
        return stream;
    }

    public BufferedInputStream toStreamBuffered() {
        return new BufferedInputStream(stream);
    }

    public InputStreamReader toReaderStream() {
        return new InputStreamReader(stream);
    }

    public BufferedReader toReaderBuffered() {
        return new BufferedReader(toReaderStream());
    }

    @Override
    public String toString() {
        return toReaderBuffered().lines().collect(Collectors.joining());
    }

    public<T> T collect(Collector<? super String, ?, T> c) {
        return toReaderBuffered().lines().collect(c);
    }

    public Stream<String> stream() {
        return toReaderBuffered().lines();
    }

    public void forEach(Consumer<String> f ) {
        toReaderBuffered().lines().forEach(f);
    }

    public void singleThreadForEach(Consumer<String> s) throws IOException {
        String line = null;
        while((line = toReaderBuffered().readLine()) != null)
            s.accept(line);
    }

    public void parallelForEach(Consumer<String> f ) {
        toReaderBuffered().lines().parallel().forEach(f);
    }

    public Stream<String> parallel(Consumer<String> f ) {
        return toReaderBuffered().lines().parallel();
    }

    public <T> T asJson(Class<T> classOfT) throws JsonSyntaxException {
        return gson.fromJson(toReaderStream(), classOfT);
    }
}
