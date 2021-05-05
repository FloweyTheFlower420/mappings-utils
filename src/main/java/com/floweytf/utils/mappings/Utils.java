package com.floweytf.utils.mappings;

import com.google.common.collect.ImmutableSet;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

class Utils {
    private static final Set<String> JVM_PRIMITIVES = ImmutableSet.of("Z", "B", "C", "S", "I", "J", "F", "D");
    private static final Pattern FIND_CLASS = Pattern.compile("L\1;");

    static Map<String, String> reverseMap(Map<String, String> map) {
        return map.entrySet().stream().collect(Collectors.toConcurrentMap(Map.Entry::getValue, Map.Entry::getKey));
    }

    static Map<String, String> combine(Map<String, String> map1, Map<String, String> map2) {
        return map1.entrySet().stream().collect(Collectors.toConcurrentMap(Map.Entry::getKey, kvp -> {
            if(map2.containsKey(kvp.getValue()))
                return map2.get(kvp.getValue());
            return kvp.getValue();
        }));
    }

    static List<String> readArguments(String str) {
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

    static String remapMethodSignature(String VM, Map<String, String> map) {
        Matcher matcher = FIND_CLASS.matcher(VM);

        StringBuffer out = new StringBuffer();
        while (matcher.find()) {
            String r = map.get(matcher.group(1));
            matcher.appendReplacement(out, r);
        }
        matcher.appendTail(out);
        return out.toString();
    }

    static String splitClassMember(String src) {
        StringBuilder builder = new StringBuilder(src);
        int index = builder.lastIndexOf("/");
        builder.replace(index,1 + index, " ");
        return builder.toString();
    }
}
