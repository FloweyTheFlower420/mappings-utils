package com.floweytf.utils.streams;

import com.floweytf.utils.streams.stdstreams.IStandardByteReader;
import com.floweytf.utils.streams.stdstreams.StandardByteReader;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import java.io.*;
import java.net.URL;
import java.nio.file.Path;
import java.util.concurrent.ForkJoinPool;
import java.util.function.Consumer;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IStreamEx {
    private final InputStream stream;
    private final Gson gson = new Gson();

    private IStreamEx(InputStream stream) {
        this.stream = stream;
    }

    // get from variety of stuff
    public static IStreamEx getStream(String s) {
        return new IStreamEx(new ByteArrayInputStream(s.getBytes()));
    }

    public static IStreamEx getStream(URL u) throws IOException {
        return new IStreamEx(u.openStream());
    }

    public static IStreamEx getStream(File f) throws FileNotFoundException {
        return new IStreamEx(new FileInputStream(f));
    }

    public static IStreamEx getStreamURL(String s) throws IOException {
        return getStream(new URL(s));
    }

    public static IStreamEx getStreamFile(String s) throws FileNotFoundException {
        return getStream(new File(s));
    }

    public static IStreamEx getStream(Path p) throws FileNotFoundException {
        return getStream(p.toFile());
    }

    public static IStreamEx getStreamClassPath(String s) throws ClassNotFoundException {
        return getStream(IStreamEx.class.getResourceAsStream(s));
    }

    public static IStreamEx getStream(InputStream s) {
        return new IStreamEx(s);
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

    public <T> T collect(Collector<? super String, ?, T> c) {
        return toReaderBuffered().lines().collect(c);
    }

    public Stream<String> stream() {
        return toReaderBuffered().lines();
    }

    public void forEach(Consumer<String> f) {
        toReaderBuffered().lines().forEach(f);
    }

    public void singleThreadForEach(Consumer<String> s) throws IOException {
        String line = null;
        while ((line = toReaderBuffered().readLine()) != null) {
            s.accept(line);
        }
    }

    public void parallelForEach(Consumer<String> f) {
        parallelForEach(f, 8);
    }

    public void parallelForEach(Consumer<String> f, int threads) {
        try {
            ForkJoinPool myPool = new ForkJoinPool(8);
            myPool.submit(() ->
                toReaderBuffered().lines().parallel().forEach(f)
            ).get();
        }
        catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public Stream<String> parallel(Consumer<String> f) {
        return toReaderBuffered().lines().parallel();
    }

    public <T> T asJson(Class<T> classOfT) throws JsonSyntaxException {
        return gson.fromJson(toReaderStream(), classOfT);
    }

    public IStandardByteReader getStandardStream() {
        return new StandardByteReader(stream);
    }
}