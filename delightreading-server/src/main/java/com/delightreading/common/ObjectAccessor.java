package com.delightreading.common;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

public class ObjectAccessor {

    public static final String SEPARATOR = "\\.";

    public static <T> Optional<T> access(Object target, String path, Class<T> cls) {
        var pathItems = path.split(SEPARATOR);
        return access(target, pathItems, cls);
    }

    public static <T> Optional<T> access(Object target, String[] pathItems, Class<T> cls) {
        if (pathItems == null || pathItems.length == 0) {
            return Optional.empty();
        }
        Map map = (Map) target; // At the moment we are assuming the object is always a map
        if (!map.containsKey(pathItems[0])) {
            return Optional.empty();
        }
        var entry = map.get(pathItems[0]);

        if (pathItems.length == 1) {
            return (entry == null) ? Optional.empty() : Optional.of(cls.cast(entry));
        }
        if (entry instanceof Map) {
            var pathTail = Arrays.copyOfRange(pathItems, 1, pathItems.length);
            return access(entry, pathTail, cls);
        }
        // If it is not an Map, it will return empty.
        return Optional.empty();

    }


}
