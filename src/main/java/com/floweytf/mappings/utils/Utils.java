package com.floweytf.mappings.utils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;

public class Utils {
    public static String readFile(String name) throws IOException {
        return Files.lines(Paths.get(name), StandardCharsets.UTF_8).collect(Collectors.joining());
    }
}
