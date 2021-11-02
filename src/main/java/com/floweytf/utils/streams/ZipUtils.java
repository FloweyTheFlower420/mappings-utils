package com.floweytf.utils.streams;

import com.floweytf.utils.Utils;

import java.io.IOException;
import java.util.function.BiConsumer;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZipUtils {
    public static IStreamEx getStream(ZipFile f, String element) throws IOException {
        return IStreamEx.getStream(f.getInputStream(f.getEntry(element)));
    }

    public static void iterateZipStream(ZipInputStream zis, BiConsumer<ZipEntry, IStreamEx> cb) {
        Utils.rethrow(() -> {
            ZipEntry e;
            while ((e = zis.getNextEntry()) != null) {
                cb.accept(e, IStreamEx.getStream(zis));
            }
        });
    }
}
