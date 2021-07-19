package com.floweytf.utils.mappings;

import com.floweytf.utils.streams.IStreamEx;
import com.floweytf.utils.streams.OStreamEx;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class MappingsFactory {

    /**
     * @param fileName The name of the file
     * @return The parsed mappings. The format for which is determined by the file extension.
     */
    public static Mappings readFromFile(String fileName) throws FileNotFoundException {
        IStreamEx stream = IStreamEx.getStreamFile(fileName);
        if (fileName.endsWith(".csrg")) {
            return readCsrg(stream);
        }
        if (fileName.endsWith(".tsrg")) {
            return readTsrg(stream);
        }
        if (fileName.endsWith(".srg")) {
            return readSrg(stream);
        }
        throw new IllegalStateException("Illegal file ending");
    }

    public static Mappings readFromSources(MappingType t, IStreamEx... inputs) {
        Mappings source = MappingType.getMethodFromType(t, inputs[0]);
        for (int i = 1; i < inputs.length; i++) {
            source.append(MappingType.getMethodFromType(t, inputs[i]));
        }

        return source;
    }

    /**
     * @param stream The stream to read from
     * @return The parsed mappings.
     */
    public static Mappings readCsrg(IStreamEx stream) {
        Map<String, String> classMappings = new ConcurrentHashMap<>();
        Map<String, String> _classMappings = new ConcurrentHashMap<>();
        Map<String, String> fieldMappings = new ConcurrentHashMap<>();
        Map<String, String> methodMappings = new ConcurrentHashMap<>();

        Queue<String> queue = new ConcurrentLinkedQueue<>();

        stream.parallelForEach(line -> {
            if (line.startsWith("#")) {
                return;
            }

            String[] names = line.split(" ");
            if (names.length == 2) {
                classMappings.put(names[0], names[1]);
                _classMappings.put(names[1], names[0]);
            }
            else {
                queue.add(line);
            }
        });

        queue.stream().parallel().forEach(line -> {
            if (line.startsWith("#")) {
                return;
            }
            String[] names = line.split(" ");
            if (names.length == 3) {
                fieldMappings.put(_classMappings.getOrDefault(names[0], names[0]) + " " + names[1], names[0] + " " + names[2]);
            }
            else if (names.length == 4) {
                methodMappings.put(
                    _classMappings.getOrDefault(names[0], names[0]) + " " + names[1] + " " + Utils.remapMethodSignature(names[2], _classMappings),
                    names[0] + " " + names[3] + " " + names[2]
                );
            }
        });

        return new MappingsImpl(
            classMappings,
            fieldMappings,
            methodMappings
        );
    }

    /**
     * @param stream The stream to read from
     * @return The parsed mappings.
     */
    public static Mappings readSrg(IStreamEx stream) {
        Map<String, String> classMappings = new ConcurrentHashMap<>();
        Map<String, String> fieldMappings = new ConcurrentHashMap<>();
        Map<String, String> methodMappings = new ConcurrentHashMap<>();

        Queue<String> queue = new ConcurrentLinkedQueue<>();

        stream.parallelForEach(line -> {
            if (line.startsWith("#")) {
                return;
            }

            String[] names = line.split(" ");
            if (names.length == 3 && names[0].equals("CL:")) {
                classMappings.put(names[1], names[2]);
            }
            else {
                queue.add(line);
            }
        });

        queue.stream().parallel().forEach(line -> {
            if (line.startsWith("#")) {
                return;
            }
            String[] names = line.split(" ");
            if (names.length == 3) {
                fieldMappings.put(Utils.splitClassMember(names[1]), Utils.splitClassMember(names[2]));
            }
            else if (names.length == 5) {
                methodMappings.put(
                    Utils.splitClassMember(names[1]) + " " + names[2],
                    Utils.splitClassMember(names[3]) + " " + names[4]
                );
            }
        });

        return new MappingsImpl(
            classMappings,
            fieldMappings,
            methodMappings
        );
    }

    public static Mappings readTsrg(IStreamEx stream) {
        Map<String, String> classMappings = new ConcurrentHashMap<>();

        Queue<String> queue = new ConcurrentLinkedQueue<>();

        stream.parallelForEach(line -> {
            if (line.startsWith("#")) {
                return;
            }
            if (!line.startsWith("\t")) {
                String[] names = line.split(" ");
                classMappings.put(names[0], names[1]);
            }

            queue.add(line);
        });

        Map<String, String> methodMappings = new ConcurrentHashMap<>();
        Map<String, String> fieldMappings = new ConcurrentHashMap<>();


        String obfClassName = null;
        for (String line : queue) {
            if (!line.startsWith("\t")) {
                obfClassName = line.split(" ")[0];
            }
            else {
                String[] name = line.replaceFirst("\t", "").split(" ");
                if (name.length == 2) {
                    fieldMappings.put(
                        obfClassName + " " + name[0],
                        classMappings.get(obfClassName) + " " + name[1]
                    );
                }
                else {
                    methodMappings.put(
                        obfClassName + " " + name[0] + " " + name[1],
                        classMappings.get(obfClassName) + " " + name[2] + " " + Utils.remapMethodSignature(name[1], classMappings)
                    );
                }
            }
        }

        return new MappingsImpl(
            classMappings,
            fieldMappings,
            methodMappings
        );
    }

    public static Mappings applyMCP(Mappings mappings, IStreamEx method, IStreamEx field) {
        Map<String, String> mcp = new ConcurrentHashMap<>();
        method.parallelForEach(s -> {
            String[] elements = s.split(",");
            mcp.put(elements[0], elements[1]);
        });

        field.parallelForEach(s -> {
            String[] elements = s.split(",");
            mcp.put(elements[0], elements[1]);
        });

        mappings.getMethodMappings().entrySet().stream().parallel().forEach(pair -> {
            String[] parts = pair.getValue().split(" ");
            pair.setValue(parts[0] + " " + mcp.getOrDefault(parts[1], parts[1]) + " " + parts[2]);
        });

        mappings.getFieldMappings().entrySet().stream().parallel().forEach(pair -> {
            String[] parts = pair.getValue().split(" ");
            pair.setValue(parts[0] + " " + mcp.getOrDefault(parts[1], parts[1]));
        });

        return mappings;
    }

    public static void writeSrg(Mappings mappings, OStreamEx stream) {
        mappings.getClassMappings().forEach((key, value) -> stream.writeln("CL: " + key + " " + value));
        mappings.getFieldMappings().forEach((key, value) -> stream.writeln("FD: " + key.replace(" ", "/") + " " + value.replace(" ", "/")));
        mappings.getMethodMappings().forEach((key, value) -> stream.writeln("MD: " + key.replaceFirst(" ", "/") + " " + value.replaceFirst(" ", "/")));
    }
}
