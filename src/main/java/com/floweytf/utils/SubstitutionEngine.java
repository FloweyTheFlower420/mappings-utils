package com.floweytf.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubstitutionEngine {
    private final Pattern pattern;
    private static final Map<Character, Character> CLOSER = new HashMap<Character, Character>() {{
        put('[', ']');
        put('{', '}');
        put('(', ')');
        put('<', '>');
    }};

    // valid characters is [{(<
    public SubstitutionEngine(String startCh) {
        String endCh = startCh.chars().filter(v -> CLOSER.containsKey((char)v))
            .map(v -> CLOSER.get((char)v)).collect(StringBuilder::new,
            StringBuilder::appendCodePoint, StringBuilder::append)
            .toString();
        pattern = Pattern.compile("\\Q$" + startCh + "\\E([a-z]+)\\Q" + endCh + "\\E");
    }

    public SubstitutionEngine() {
        this("{");
    }

    public String substitution(String str, Map<String, String> symbols) {
        StringBuffer buffer = new StringBuffer();
        Matcher matcher = pattern.matcher(str);
        for(int i = 1; i <= matcher.groupCount(); i++) {
            String entry = matcher.group(i);
            entry = symbols.getOrDefault(entry, entry);
            matcher.appendReplacement(buffer, entry);
        }

        return buffer.toString();
    }

    public static void main(String... a) {
        SubstitutionEngine substitutionEngine = new SubstitutionEngine();
        String str = "${test} FOASDALSDJAKL:SDJ ${foo}";
        str = substitutionEngine.substitution(str, new HashMap<String, String>(){{
            put("test", "123");
        }});
    }
}
