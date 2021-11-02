package com.floweytf.utils;

import com.floweytf.utils.builders.MapBuilder;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

public class Utils {
    private static final Map<Character, Character> FLIP_BRACKET = MapBuilder.<Character, Character>getDefault()
        .add('[', ']')
        .add('{', '}')
        .add('(', ')')
        .build();

    private static abstract class Helper<T> {
        public final Class<T> clazz;
        @SuppressWarnings("unchecked")
        Helper() {
            this.clazz = (Class<T>) ((ParameterizedType)
                getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        }
    }

    public static void rethrow(ThrowingMethod method) {
        try {
            method.run();
        }
        catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    public static <T> Class<T> getClassOfGeneric() {
        // Java.
        return new Helper<T>() {}.clazz;
    }

    public static char getMatchingChar(char ch) {
        return FLIP_BRACKET.getOrDefault(ch, ch);
    }
}
