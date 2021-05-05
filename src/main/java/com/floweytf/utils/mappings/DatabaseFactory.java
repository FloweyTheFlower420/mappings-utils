package com.floweytf.utils.mappings;

import com.floweytf.utils.streams.InputStreamUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;


public class DatabaseFactory {

    public static IMappings readFromFile(String fileName) {
        //if(fileName.startsWith(".csrg"))

        return null;
    }

    public static IMappings readFromSources(MappingType t, InputStreamUtils... inputs) {
        IMappings source = MappingType.getMethodFromType(t, inputs[0]);
        for (int i = 1; i < inputs.length; i++) {
            source.append(MappingType.getMethodFromType(t, inputs[i]));
        }

        return source;
    }

    public static IMappings readCsrg(InputStreamUtils stream) {
        Map<String, String> classMappings = new ConcurrentHashMap<>();
        Map<String, String> _classMappings = new ConcurrentHashMap<>();
        Map<String, String> fieldMappings = new ConcurrentHashMap<>();
        Map<String, String> methodMappings = new ConcurrentHashMap<>();

        stream.forEach(line -> {
            if (line.startsWith("#"))
                return;

            String[] names = line.split(" ");
            if (names.length == 2) {
                classMappings.put(names[0], names[1]);
                _classMappings.put(names[1], names[0]);
            }
        });

        stream.forEach(line -> {
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

        return new Mappings(
            classMappings,
            fieldMappings,
            methodMappings
        );
    }

    public static IMappings readSrg(InputStreamUtils stream) {
        Map<String, String> classMappings = new ConcurrentHashMap<>();
        Map<String, String> _classMappings = new ConcurrentHashMap<>();
        Map<String, String> fieldMappings = new ConcurrentHashMap<>();
        Map<String, String> methodMappings = new ConcurrentHashMap<>();

        stream.forEach(line -> {
            if(line.startsWith("#"))
                return;

            String[] names = line.split(" ");
            if(names.length == 3) {
                classMappings.put(names[1], names[2]);
                _classMappings.put(names[2], names[1]);
            }
        });

        stream.forEach(line -> {
            if (line.startsWith("#"))
                return;
            String[] names = line.split(" ");
            if (names.length == 3)
                fieldMappings.put(Utils.splitClassMember(names[1]),Utils.splitClassMember(names[2]));
            else if(names.length == 4) {
                methodMappings.put(
                    Utils.splitClassMember(names[1]) + " " + names[2],
                    Utils.splitClassMember(names[3]) + " " + names[4]
                );
            }
        });

        return new Mappings(
            classMappings,
            fieldMappings,
            methodMappings
        );
    }

    public static IMappings readSrgWithMCP(String srg, String csv) { return null; }
}
