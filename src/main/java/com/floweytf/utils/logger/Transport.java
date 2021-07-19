package com.floweytf.utils.logger;

import java.io.OutputStream;
import java.io.PrintWriter;

public class Transport {
    protected OutputStream stream;
    protected int level;
    protected PostOutTransform p;

    protected Transport() {
        stream = null;
        level = -1;
        p = null;
    }

    public Transport(OutputStream stream, int level) {
        this.stream = stream;
        this.level = level;
        p = null;
    }

    public Transport(OutputStream stream, int level, PostOutTransform p) {
        this.stream = stream;
        this.level = level;
        this.p = p;
    }

    public void setTransform(PostOutTransform p) {
        this.p = p;
    }

    public PostOutTransform getTransform() {
        return p;
    }

    public void write(String buffer, int level) {
        if (level > this.level) {
            return;
        }
        if (p != null) {
            buffer = p.operation(buffer);
        }
        PrintWriter p = new PrintWriter(stream);
        p.print(buffer);
        p.flush();
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        if (level > BetterLogger.DEBUG || level < -1) {
            throw new IllegalArgumentException("Invalid level");
        }
        this.level = level;
    }
}
