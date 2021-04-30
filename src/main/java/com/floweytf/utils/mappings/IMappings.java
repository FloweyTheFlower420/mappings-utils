package com.floweytf.utils.mappings;

import java.util.Map;

public interface IMappings {
    /**
     * @return A copy of the mapping, in reverse form
     */
    IMappings reverse();

    /**
     * Merges this and other, where
     * if this has mapping a -> b
     * and other has mapping b -> c
     * then, the merged mapping is a -> c
     * if other does not have a mapping for b, the output is a -> b
     * if other has b -> c, but this does not have any mapping to b, do not add that mapping
     * @return A copy of a joined mapping between this and other
     */
    IMappings merge(IMappings other);

    /**
     * @return The underlying map for class mappings
     */
    Map<String, String> getClassMappings();

    /**
     * @return The underlying map for method mappings
     */
    Map<String, String> getMethodMappings();

    /**
     * @return The underlying map for field mappings
     */
    Map<String, String> getFieldMappings();

    /**
     * @param className The name of the class
     * @return the mapped name of the class
     */
    String mapClass(String className);

    /**
     * @param className The name of the class
     * @param fieldName The name of the field
     * @return the mapped named of the field
     */
    FieldMapping mapField(String className, String fieldName);

    /**
     * @param className The name of the class
     * @param methodName The name of the method
     * @param args The arguments of the method
     * @return the mapped named of the method
     */
    MethodMapping mapMethod(String className, String methodName, String returnType, String... args);
}
