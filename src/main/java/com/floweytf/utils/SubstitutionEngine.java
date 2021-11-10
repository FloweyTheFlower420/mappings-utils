package com.floweytf.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SubstitutionEngine {
    private final Pattern pattern;
    // valid characters is [{(
    public SubstitutionEngine(String startCh) {
        StringBuilder sb = new StringBuilder();
        startCh.chars().forEach(i -> {
            sb.append(Utils.getMatchingChar((char)i));
        });

        pattern = Pattern.compile("\\Q$" + startCh + "\\E([a-z]+)\\Q" + sb.toString() + "\\E");
    }

    public SubstitutionEngine() {
        this("{");
    }

    public String substitution(String str, Map<String, String> symbols) {
        StringBuffer buffer = new StringBuffer();
        Matcher matcher = pattern.matcher(str);

        while(matcher.find()) {
            String entry = matcher.group(1);
            entry = symbols.getOrDefault(entry, entry);
            matcher.appendReplacement(buffer, entry);
        }

        return buffer.toString();
    }

    public static void main(String... a) {
        SubstitutionEngine substitutionEngine = new SubstitutionEngine("[[");
        String str = "$[[test]] FOASDALSDJAKL:SDJ $[[foo]]";
        str = substitutionEngine.substitution(str, new HashMap<String, String>(){{
            put("test", "123");
        }});
    }
}
