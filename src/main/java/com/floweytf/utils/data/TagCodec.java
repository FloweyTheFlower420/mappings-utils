package com.floweytf.utils.data;

import com.floweytf.utils.data.internal.*;
import com.floweytf.utils.streams.stdstreams.StandardByteReader;
import com.floweytf.utils.streams.stdstreams.StandardByteWriter;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Stack;

import static com.floweytf.utils.data.AbstractTag.*;

public class TagCodec {

    private static AbstractTag getFromInt(byte type) {
        switch (type) {
            case TAG_OBJECT:
                return new ObjectTag();
            case TAG_ARRAY:
                return new ArrayTag();
            case TAG_BYTE:
                return new ByteTag();
            case TAG_SHORT:
                return new ShortTag();
            case TAG_INT:
                return new IntTag();
            case TAG_LONG:
                return new LongTag();
            case TAG_STRING:
                return new StringTag();
            case TAG_FLOAT:
                return new FloatTag();
            case TAG_DOUBLE:
                return new DoubleTag();
        }

        throw new IllegalStateException("Invalid type, you data is corrupted!");
    }


    public static ObjectTag deserialize(InputStream i) throws IOException {
        // the reader that we will use to read things in a standard fashion
        StandardByteReader reader = new StandardByteReader(i);
        // stack frames to emulate recursive algo
        Stack<DeserializeStackFrame> stack = new Stack<>();

        DeserializeStackFrame frame = new DeserializeStackFrame(
            new ObjectTag(),
            0,
            reader.readInt()
        );

        while (true) {
            while (frame.index == frame.size && stack.size() != 0) {
                // we have reached the end of the iteration
                DeserializeStackFrame newFrame = stack.pop();
                frame = newFrame;
            }
            if (stack.size() == 0 && frame.index == frame.size)
                break;
            // the state for an object
            frame.index++;

            if (frame.tag instanceof ArrayTag) {
                // we have/are reading another object
                // read the type
                byte type = reader.readByte();
                // obtain the tag
                AbstractTag t = getFromInt(type);
                if (t instanceof ObjectTag) {
                    // push to stack
                    frame.tag.asArray().add(t);
                    stack.push(frame);
                    frame = new DeserializeStackFrame((CollectionTag) t, 0, reader.readInt());
                }
                else if (t instanceof ArrayTag) {
                    frame.tag.asArray().add(t);
                    stack.push(frame);
                    frame = new DeserializeStackFrame((CollectionTag) t, 0, reader.readInt());
                }
                else {
                    ((PrimitiveTag<?>) t).readFrom(reader);
                    frame.tag.asArray().add(t);
                }
            }
            else {
                // we have/are reading another object
                // read the name and type
                String name = reader.readString();
                byte type = reader.readByte();
                // obtain the tag
                AbstractTag t = getFromInt(type);
                if (t instanceof ObjectTag) {
                    frame.tag.asObject().put(name, t);
                    stack.push(frame);
                    frame = new DeserializeStackFrame((CollectionTag) t, 0, reader.readInt());
                }
                else if (t instanceof ArrayTag) {
                    frame.tag.asObject().put(name, t);
                    stack.push(frame);
                    frame = new DeserializeStackFrame((CollectionTag) t, 0, reader.readInt());
                }
                else {
                    ((PrimitiveTag<?>) t).readFrom(reader);
                    frame.tag.asObject().put(name, t);
                }
            }
        }

        return frame.tag.asObject();
    }

    public static void serialize(OutputStream o, ObjectTag root) throws IOException {
        StandardByteWriter writer = new StandardByteWriter(o);
        Stack<SerializeStackFrame> stack = new Stack<>();
        SerializeStackFrame frame = new SerializeStackFrame(
            root,
            root.iterator()
        );

        writer.write(root.size());

        while(true) {
            while (!frame.iterator.hasNext() && stack.size() != 0) {
                frame = stack.pop();
            }
            if (stack.size() == 0 && !frame.iterator.hasNext())
                break;

            if(frame.tag instanceof ArrayTag) {
                AbstractTag t = (AbstractTag) frame.iterator.next();
                writer.write(t.getType());
                if(t instanceof ObjectTag) {
                    stack.push(frame);
                    frame = new SerializeStackFrame((CollectionTag) t, ((ObjectTag)t).iterator());
                    writer.write(frame.tag.size());
                }
                else if(t instanceof ArrayTag) {
                    stack.push(frame);
                    frame = new SerializeStackFrame((CollectionTag) t, ((ArrayTag)t).iterator());
                    writer.write(frame.tag.size());
                }
                else {
                    ((PrimitiveTag<?>)t).writeTo(writer);
                }
            }
            else {
                @SuppressWarnings("unchecked")
                Map.Entry<String, AbstractTag> t = (Map.Entry<String, AbstractTag>) frame.iterator.next();
                writer.write(t.getKey());
                writer.write(t.getValue().getType());
                if(t.getValue() instanceof ObjectTag) {
                    stack.push(frame);
                    frame = new SerializeStackFrame((CollectionTag) t.getValue(), ((ObjectTag) t.getValue()).iterator());
                    writer.write(frame.tag.size());
                }
                else if(t.getValue() instanceof ArrayTag) {
                    stack.push(frame);
                    frame = new SerializeStackFrame((CollectionTag) t.getValue(), ((ArrayTag) t.getValue()).iterator());
                    writer.write(frame.tag.size());
                }
                else {
                    ((PrimitiveTag<?>)t.getValue()).writeTo(writer);
                }
            }
        }
    }
}
