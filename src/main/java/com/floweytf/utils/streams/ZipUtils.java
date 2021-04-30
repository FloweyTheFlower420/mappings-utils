package com.floweytf.utils.streams;

import java.io.IOException;
import java.util.zip.ZipFile;

public class ZipUtils {
    public static InputStreamUtils getStream(ZipFile f, String element) throws IOException {
        return InputStreamUtils.getStream(f.getInputStream(f.getEntry(element)));
    }
}
