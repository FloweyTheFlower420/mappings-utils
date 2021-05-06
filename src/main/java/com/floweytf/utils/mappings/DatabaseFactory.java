package com.floweytf.utils.mappings;

import com.floweytf.utils.streams.InputStreamUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;

public class DatabaseFactory {

    /**
     * @param fileName The name of the file
     * @return The parsed mappings. The format for which is determined by the file extension.
     */
    public static Mappings readFromFile(String fileName) {
        //if(fileName.startsWith(".csrg"))

        return null;
    }

    public static Mappings readFromSources(MappingType t, InputStreamUtils... inputs) {
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
    public static Mappings readCsrg(InputStreamUtils stream) {
        Map<String, String> classMappings = new ConcurrentHashMap<>();
        Map<String, String> _classMappings = new ConcurrentHashMap<>();
        Map<String, String> fieldMappings = new ConcurrentHashMap<>();
        Map<String, String> methodMappings = new ConcurrentHashMap<>();

        Queue<String> queue = new ConcurrentLinkedQueue<>();

        stream.parallelForEach(line -> {
            if (line.startsWith("#"))
                return;

            String[] names = line.split(" ");
            if (names.length == 2) {
                classMappings.put(names[0], names[1]);
                _classMappings.put(names[1], names[0]);
            }
            else
                queue.add(line);
        });

        queue.stream().parallel().forEach(line -> {
            if (line.startsWith("#"))
                return;
            String[] names = line.split(" ");
            if (names.length == 3)
                fieldMappings.put(_classMappings.get(names[0]) + " " + names[1], names[0] + " " + names[2]);
            else if (names.length == 4) {
                methodMappings.put(
                    _classMappings.get(names[0]) + " " + names[1] + " " + Utils.remapMethodSignature(names[2], _classMappings),
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
    public static Mappings readSrg(InputStreamUtils stream) {
        Map<String, String> classMappings = new ConcurrentHashMap<>();
        Map<String, String> fieldMappings = new ConcurrentHashMap<>();
        Map<String, String> methodMappings = new ConcurrentHashMap<>();

        Queue<String> queue = new ConcurrentLinkedQueue<>();

        stream.parallelForEach(line -> {
            if(line.startsWith("#"))
                return;

            String[] names = line.split(" ");
            if(names.length == 3)
                classMappings.put(names[1], names[2]);
            else
                queue.add(line);
        });

        queue.stream().parallel().forEach(line -> {
            if (line.startsWith("#"))
                return;
            String[] names = line.split(" ");
            if (names.length == 3)
                fieldMappings.put(Utils.splitClassMember(names[1]),Utils.splitClassMember(names[2]));
            else if(names.length == 5) {
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

    public static Mappings readSrgWithMCP(String srg, String csv) { return null; }
}
