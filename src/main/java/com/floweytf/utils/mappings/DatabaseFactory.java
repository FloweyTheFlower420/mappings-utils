package com.floweytf.utils.mappings;

import com.floweytf.utils.streams.InputStreamUtils;
import com.google.common.collect.ImmutableSet;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Mappings implements IMappings{
    private static final Set<String> JVM_PRIMITIVES = ImmutableSet.of("Z", "B", "C", "S", "I", "J", "F", "D");

    private static Map<String, String> reverseMap(Map<String, String> map) {
        return map.entrySet().stream().collect(Collectors.toConcurrentMap(Map.Entry::getValue, Map.Entry::getKey));
    }
    private static Map<String, String> combine(Map<String, String> map1, Map<String, String> map2) {
        return map1.entrySet().stream().collect(Collectors.toConcurrentMap(Map.Entry::getKey, kvp -> {
            if(map2.containsKey(kvp.getValue()))
                return map2.get(kvp.getValue());
            return kvp.getValue();
        }));
    }
    private static List<String> readArguments(String str) {
        List<String> out = new ArrayList<>();
        for(int i = 0; i < str.length(); i++) {
            char a = str.charAt(i);
            if(JVM_PRIMITIVES.contains(((Character) a).toString()))
                out.add(((Character)a).toString());
            else {
                out.add(str.substring(i, str.indexOf(';', i)));
                i = str.indexOf(';', i);
            }
        }

        return out;
    }


    private final Map<String, String> c;
    private final Map<String, String> f;
    private final Map<String, String> m;

    public Mappings(Map<String, String> c, Map<String, String> f, Map<String, String> m) {
        this.c = c;
        this.f = f;
        this.m = m;
    }

    @Override
    public IMappings reverse() {
        return new Mappings(
            reverseMap(c),
            reverseMap(f),
            reverseMap(m)
        );
    }

    @Override
    public IMappings merge(IMappings other) {
        return new Mappings(
            combine(c, other.getClassMappings()),
            combine(f, other.getFieldMappings()),
            combine(m, other.getMethodMappings())
        );
    }

    @Override
    public Map<String, String> getClassMappings() {
        return c;
    }

    @Override
    public Map<String, String> getMethodMappings() {
        return m;
    }

    @Override
    public Map<String, String> getFieldMappings() {
        return f;
    }

    @Override
    public String mapClass(String className) {
        return c.get(className);
    }

    @Override
    public FieldMapping mapField(String className, String fieldName) {
        String out = f.get(className + " " + fieldName);
        String[] o = out.split(" ");
        FieldMapping mapping = new FieldMapping();
        mapping.clazz = o[0];
        mapping.name = o[1];
        return mapping;
    }

    @Override
    public MethodMapping mapMethod(String className, String methodName, String returnType, String... args) {
        StringBuilder builder = new StringBuilder();
        builder.append(className).append(" ").append(methodName).append(" (");
        for(String s : args)
            builder.append(s);
        builder.append(")").append(returnType);

        String out = f.get(builder.toString());
        String[] o = out.split(" ");
        MethodMapping mapping = new MethodMapping();
        mapping.clazz = o[0];
        mapping.name = o[1];
        String sig = o[2];
        sig = sig.replace("(", "");
        String[] parts = sig.split("\\)");
        mapping.ret = parts[1];
        mapping.args = readArguments(parts[0]);
        return mapping;
    }

    @Override
    public IMappings append(IMappings other) {
        return null;
    }
}


public class DatabaseFactory {
    public enum MappingType {
        CSRG,
        SRG,
        TSRG,
        TSRG2;

        private static final Map<MappingType, Function<InputStreamUtils, IMappings>> TYPE_TO_LAMBADA = new HashMap<>();

        static {
            TYPE_TO_LAMBADA.put(CSRG, DatabaseFactory::readCsrg);
        }

        static IMappings getMethodFromType(MappingType type, InputStreamUtils utils) {
            return TYPE_TO_LAMBADA.get(type).apply(utils);
        }
    }
    private static final Pattern FIND_CLASS = Pattern.compile("L\1;");

    private static String remapMethodSignature(String VM, Map<String, String> map) {
        Matcher matcher = FIND_CLASS.matcher(VM);

        StringBuffer out = new StringBuffer();
        while (matcher.find()) {
            String r = map.get(matcher.group(1));
            matcher.appendReplacement(out, r);
        }
        matcher.appendTail(out);
        return out.toString();
    }

    public static IMappings readFromFile(String fileName) {
        //if(fileName.startsWith(".csrg"))

        return null;
    }

    public static IMappings readFromSources(MappingType t, InputStreamUtils... inputs) {
        IMappings source = MappingType.getMethodFromType(t, inputs[0]);
        for(int i = 1; i < inputs.length; i++) {
            source.append(MappingType.getMethodFromType(t, inputs[i]));
        }

        return source;
    }

    public static IMappings readCsrg(InputStreamUtils stream) {
        try {
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
                        _classMappings.get(names[0]) + " " + names[1] + " " + remapMethodSignature(names[2], _classMappings),
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
        catch(Exception e) {
            return null;
        }
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
            if(names.length == 2) {
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
            else if(names.length == 4){
                methodMappings.put(
                    _classMappings.get(names[0]) + " " + names[1] + " " + remapMethodSignature(names[2], _classMappings),
                    names[0] + " " + names[3] + " " + names[2]
                );
            }
        });

        return null;
    }

    public static IMappings readSrgWithMCP(String srg, String csv) { return null; }
}
