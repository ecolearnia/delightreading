package com.delightreading.common

import com.delightreading.user.model.Experience
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.util.HashMap

class ObjectAccessorTest {

    lateinit var aMap: Map<*, *>

    @BeforeEach
    fun setUp() {

        aMap = object : HashMap<String, Any?>() {
            init {
                put("text", "Hello")
                put("number", 2)
                put("nullval", null)
                put("dict", object : HashMap<String, Any>() {
                    init {
                        put("text", "Hello2")
                        put("number", 12)
                        put("Object", Experience(kind = "Test-kind", title = "Test-title"))
                    }
                })
            }
        }
    }

    @Test
    fun test_whenEmptyPath_returnEmpty() {

        val option = ObjectAccessor.access(aMap, "", String::class.java)
        Assertions.assertThat(option).isEmpty
    }

    @Test
    fun test_whenEntryNotFound_returnEmpty() {

        val option = ObjectAccessor.access(aMap, "NONEXISTENT", String::class.java)
        Assertions.assertThat(option).isEmpty
    }

    @Test
    fun test_whenNestedEntryNotFound_returnEmpty() {

        val option = ObjectAccessor.access(aMap, "dict1.NONEXISTENT", String::class.java)
        Assertions.assertThat(option).isEmpty
    }

    @Test
    fun test_whenSecondNestedEntryNotFound_returnEmpty() {

        val option = ObjectAccessor.access(aMap, "dict.NONEXISTENT", String::class.java)
        Assertions.assertThat(option).isEmpty
    }

    @Test
    fun test_whenFoundString_returnString() {

        val option = ObjectAccessor.access(aMap, "text", String::class.java)
        Assertions.assertThat(option.get()).isEqualTo("Hello")
    }

    @Test
    fun test_whenFoundNull_returnNull() {

        val option = ObjectAccessor.access(aMap, "nullval", Any::class.java)
        Assertions.assertThat(option).isEmpty()
    }


    @Test
    fun test_whenFoundNumber_returnNumber() {

        val option = ObjectAccessor.access(aMap, "number", Integer::class.java)
        Assertions.assertThat(option.get()).isEqualTo(2)
    }

    @Test
    fun test_whenFoundMap_returnString() {

        val option = ObjectAccessor.access(aMap, "dict", Map::class.java)
        Assertions.assertThat(option.get()).isInstanceOf(Map::class.java)
        Assertions.assertThat(option.get()["number"]).isEqualTo(12)
    }

    @Test
    fun test_whenNestedFound_returnNumber() {

        val option = ObjectAccessor.access(aMap, "dict.text", String::class.java)
        Assertions.assertThat(option.get()).isEqualTo("Hello2")
    }


    @Test
    fun set_whenTargetEmptyAndOneLevelPath_add() {

        val newMap = HashMap<String, Any>()
        ObjectAccessor[newMap, "text"] = "HelloWorld"
        Assertions.assertThat(newMap["text"]).isEqualTo("HelloWorld")
    }

    @Test
    fun set_whenTargetEmptyAndTwoLevelPath_add() {

        val newMap = HashMap<String, Any>()
        ObjectAccessor[newMap, "dict.text"] = "HelloWorld"
        Assertions.assertThat((newMap["dict"] as Map<*, *>)["text"]).isEqualTo("HelloWorld")
    }

    @Test
    fun set_whenTargetNonEmptyAndExistingPath_update() {

        ObjectAccessor[aMap, "dict.text"] = "HelloWorld"
        Assertions.assertThat((aMap["dict"] as Map<*, *>)["text"]).isEqualTo("HelloWorld")
        val existing = ObjectAccessor.access(aMap, "dict.number", Integer::class.java)
        Assertions.assertThat(existing.get()).isEqualTo(12)
    }
}