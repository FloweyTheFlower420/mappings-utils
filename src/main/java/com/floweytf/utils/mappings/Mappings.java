package com.floweytf.utils.mappings;

import java.util.Map;

/**
 * The format for classMapping:
 * "com/example/Class" -> "com/otherexample/Other"
 * The format for fieldMapping:
 * "com/example/Class fieldName" -> "com/example/Other otherFieldName"
 * The format for methodMapping:
 * "com/example/Class method (Lcom/example/Example;)Z" -> "com/example/Other otherMethod (Lcom/example/Example;)Z"
 */
class Mappings implements IMappings {
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
            Utils.reverseMap(c),
            Utils.reverseMap(f),
            Utils.reverseMap(m)
        );
    }

    @Override
    public IMappings merge(IMappings other) {
        return new Mappings(
            Utils.combine(c, other.getClassMappings()),
            Utils.combine(f, other.getFieldMappings()),
            Utils.combine(m, other.getMethodMappings())
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
        for (String s : args) {
            builder.append(s);
        }
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
        mapping.args = Utils.readArguments(parts[0]);
        return mapping;
    }

    @Override
    public IMappings append(IMappings other) {
        return null;
    }
}
