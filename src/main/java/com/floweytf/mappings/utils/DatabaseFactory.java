package com.floweytf.mappings.utils;

import com.google.common.collect.ImmutableSet;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DatabaseFactory {
    private static Pattern FIND_CLASS = Pattern.compile("L\1;");
    private static final Set<String> JVM_PRIMITIVES = ImmutableSet.of("Z", "B", "C", "S", "I", "J", "F", "D");

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

    public static IMappings readCsrg(BufferedReader sourceClass, BufferedReader sourceMembers) throws IOException {
        Map<String, String> classMappings = new ConcurrentHashMap<>();
        Map<String, String> _classMappings = new ConcurrentHashMap<>();
        Map<String, String> fieldMappings = new ConcurrentHashMap<>();
        Map<String, String> methodMappings = new ConcurrentHashMap<>();

        sourceClass.lines().parallel().forEach(line -> {
            if(line.startsWith("#"))
                return;
            String[] names = line.split(" ");
            classMappings.put(names[0], names[1]);
            _classMappings.put(names[1], names[0]);
        });

        sourceMembers.lines().forEach(line -> {
            if (line.startsWith("#"))
                return;
            String[] names = line.split(" ");
            if (names.length == 3)
                fieldMappings.put(_classMappings.get(names[0]) + " " + names[1], names[0] + " " + names[2]);
            else {
                methodMappings.put(
                    _classMappings.get(names[0]) + " " + names[1] + " " + remapMethodSignature(names[2], _classMappings),
                    names[0] + " " + names[3] + " " + names[2]
                    );
            }
        });

        return new IMappings() {
            @Override
            public IMappings reverse() {
                return null;
            }

            @Override
            public IMappings merge(IMappings other) {
                return null;
            }

            @Override
            public Map<String, String> getClassMappings() {
                return classMappings;
            }

            @Override
            public Map<String, String> getMethodMappings() {
                return methodMappings;
            }

            @Override
            public Map<String, String> getFieldMappings() {
                return fieldMappings;
            }

            @Override
            public String mapClass(String className) {
                return classMappings.get(className);
            }

            @Override
            public FieldMapping mapField(String className, String fieldName) {
                String out = fieldMappings.get(className + " " + fieldName);
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

                String out = fieldMappings.get(builder.toString());
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
        };
    }
}
