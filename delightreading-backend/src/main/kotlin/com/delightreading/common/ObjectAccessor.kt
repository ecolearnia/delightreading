package com.delightreading.common

import java.util.*

object ObjectAccessor {

    val SEPARATOR = "\\."

    /**
     * Gets the value of the object traversing the object graph, where the nodes are made of Maps
     * @param target
     * @param path
     * @param cls
     * @param <T>
     * @return
    </T> */
    fun <T> access(target: Any, path: String, cls: Class<T>): Optional<T> {
        val pathItems = path.split(SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        return access(target, pathItems, cls, false)
    }

    fun <T> access(target: Any, pathItems: Array<String>?, cls: Class<T>, createPath: Boolean): Optional<T> {
        if (pathItems == null || pathItems.size == 0) {
            return Optional.empty()
        }
        val map = target as MutableMap<String, Any?> // At the moment we are assuming the object is always a map
        if (!map.containsKey(pathItems[0])) {
            if (createPath) {
                if (pathItems.isNotEmpty())
                    map[pathItems[0]] = HashMap<String, Any?>()
                else
                    map[pathItems[0]] = null
            } else {
                return Optional.empty()
            }
        }
        val entry = map[pathItems[0]]

        if (pathItems.size == 1) {
            return if (entry == null) Optional.empty() else Optional.of(cls.cast(entry))
        }
        if (entry is Map<*, *>) {
            val pathTail = Arrays.copyOfRange(pathItems, 1, pathItems.size)
            return access(entry, pathTail, cls, createPath)
        }
        // If it is not an Map, it will return empty.
        return Optional.empty()

    }

    /**
     * Sets the value to the given path.
     * If the inner element is not found, creates them as HashMap
     * @param target
     * @param path
     * @param value
     */
    operator fun set(target: Any, path: String, value: Any) {
        val pathItems = path.split(SEPARATOR.toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        val leafItem: String

        val optNode: Optional<Any>
        if (pathItems.size == 1) {
            leafItem = pathItems[0]
            optNode = Optional.of(target)
        } else {
            val nonLealItems = Arrays.copyOfRange(pathItems, 0, pathItems.size - 1)
            leafItem = pathItems[pathItems.size - 1]
            optNode = access(target, nonLealItems, Any::class.java, true)
        }
        optNode.ifPresent { node ->
            if (node is MutableMap<*, *>) {
                var mutableNode = node as MutableMap<String, Any?>
                mutableNode[leafItem] = value
            }
        }

    }

}