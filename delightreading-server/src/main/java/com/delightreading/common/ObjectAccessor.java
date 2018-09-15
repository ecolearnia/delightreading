package com.delightreading.common;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ObjectAccessor {

    public static final String SEPARATOR = "\\.";

    /**
     * Gets the value of the object traversing the object graph, where the nodes are made of Maps
     * @param target
     * @param path
     * @param cls
     * @param <T>
     * @return
     */
    public static <T> Optional<T> access(Object target, String path, Class<T> cls) {
        var pathItems = path.split(SEPARATOR);
        return access(target, pathItems, cls, false);
    }

    public static <T> Optional<T> access(Object target, String[] pathItems, Class<T> cls, boolean createPath ) {
        if (pathItems == null || pathItems.length == 0) {
            return Optional.empty();
        }
        Map map = (Map) target; // At the moment we are assuming the object is always a map
        if (!map.containsKey(pathItems[0])) {
            if (createPath) {
                if (pathItems.length > 0)
                    map.put(pathItems[0], new HashMap<String, Object>());
                else
                    map.put(pathItems[0], null);
            } else {
                return Optional.empty();
            }
        }
        var entry = map.get(pathItems[0]);

        if (pathItems.length == 1) {
            return (entry == null) ? Optional.empty() : Optional.of(cls.cast(entry));
        }
        if (entry instanceof Map) {
            var pathTail = Arrays.copyOfRange(pathItems, 1, pathItems.length);
            return access(entry, pathTail, cls, createPath);
        }
        // If it is not an Map, it will return empty.
        return Optional.empty();

    }

    /**
     * Sets the value to the given path.
     * If the inner element is not found, creates them as HashMap
     * @param target
     * @param path
     * @param value
     */
    public static void set(Object target, String path, Object value) {
        var pathItems = path.split(SEPARATOR);
        String leafItem;
        Optional<Object> optNode;
        if (pathItems.length == 1) {
            leafItem = pathItems[0];
            optNode = Optional.of(target);
        } else {
            var nonLealItems = Arrays.copyOfRange(pathItems, 0, pathItems.length - 1);
            leafItem = pathItems[pathItems.length - 1];
            optNode = access(target, nonLealItems, Object.class, true);
        }
        optNode.ifPresent(node -> {
            if (node instanceof Map) {
                ((Map)node).put(leafItem, value);
            }
        });

    }


}
