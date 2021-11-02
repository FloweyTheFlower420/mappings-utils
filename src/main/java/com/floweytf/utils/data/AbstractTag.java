package com.floweytf.utils.data;

public abstract class AbstractTag {
    public static final int TAG_NULL = 0;

    public static final int TAG_OBJECT = 1;
    public static final int TAG_ARRAY = 2;

    public static final int TAG_BYTE = 3; // 8 bit
    public static final int TAG_SHORT = 4; // 16 bit
    public static final int TAG_INT = 5; // 32 bit
    public static final int TAG_LONG = 6; // 64 bit

    public static final int TAG_FLOAT = 7;
    public static final int TAG_DOUBLE = 8;

    public static final int TAG_STRING = 9;

    public abstract byte getType();

    public ObjectTag asObject() {
        return (ObjectTag) this;
    }
    public ArrayTag asArray() {
        return (ArrayTag) this;
    }
    public ByteTag asByte() {
        return (ByteTag) this;
    }
    public ShortTag asShort() {
        return (ShortTag) this;
    }
    public IntTag asInt() {
        return (IntTag) this;
    }
    public LongTag asLong() {
        return (LongTag) this;
    }
    public FloatTag asFloat() {
        return (FloatTag) this;
    }
    public DoubleTag asDouble() {
        return (DoubleTag) this;
    }
    public StringTag asString() {
        return (StringTag) this;
    }
}
