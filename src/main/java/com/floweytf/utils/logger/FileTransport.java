package com.floweytf.utils.logger;

import com.floweytf.utils.Utils;

import java.io.File;
import java.io.FileOutputStream;

public class FileTransport extends Transport {
    public FileTransport(String fileName, int level) {
        Utils.rethrow(() -> {
            File f = new File(fileName);
            if (!f.getParentFile().exists()) {
                f.getParentFile().mkdirs();
            }
            f.createNewFile();
            this.stream = new FileOutputStream(f);
        });

        this.level = level;
        this.p = (String s) -> {
            return s.replaceAll("\u001B\\[[;\\d]*m", "");
        };
    }

    public FileTransport(String fileName, int level, PostOutTransform p) {
        this(fileName, level);
        this.p = p;
    }
}
