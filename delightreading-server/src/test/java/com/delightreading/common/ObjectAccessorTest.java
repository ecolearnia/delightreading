package com.delightreading.common;

import com.delightreading.user.Experience;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ObjectAccessorTest {

    Object aMap;

    @Before
    public void setUp() {

        aMap = new HashMap<String, Object>(){{
            put("text", "Hello");
            put("number", 2);
            put("nullval", null);
            put("dict", new HashMap<String, Object>(){{
                put("text", "Hello2");
                put("number", 12);
                put("Object", Experience.builder().kind("Test-kind").title("Test-title").build());
            }});
        }};
    }

    @Test
    public void test_whenEmptyPath_returnEmpty() {

        Optional<String> option = ObjectAccessor.access(aMap, "", String.class);
        Assertions.assertThat(option).isEmpty();
    }

    @Test
    public void test_whenEntryNotFound_returnEmpty() {

        Optional<String> option = ObjectAccessor.access(aMap, "NONEXISTENT", String.class);
        Assertions.assertThat(option).isEmpty();
    }

    @Test
    public void test_whenNestedEntryNotFound_returnEmpty() {

        var option = ObjectAccessor.access(aMap, "dict1.NONEXISTENT", String.class);
        Assertions.assertThat(option).isEmpty();
    }

    @Test
    public void test_whenSecondNestedEntryNotFound_returnEmpty() {

        var option = ObjectAccessor.access(aMap, "dict.NONEXISTENT", String.class);
        Assertions.assertThat(option).isEmpty();
    }

    @Test
    public void test_whenFoundString_returnString() {

        var option = ObjectAccessor.access(aMap, "text", String.class);
        Assertions.assertThat(option.get()).isEqualTo("Hello");
    }

    @Test
    public void test_whenFoundNull_returnNull() {

        var option = ObjectAccessor.access(aMap, "nullval", Object.class);
        Assertions.assertThat(option).isEmpty();
    }


    @Test
    public void test_whenFoundNumber_returnNumber() {

        var option = ObjectAccessor.access(aMap, "number", Integer.class);
        Assertions.assertThat(option.get()).isEqualTo(2);
    }

    @Test
    public void test_whenFoundMap_returnString() {

        var option = ObjectAccessor.access(aMap, "dict", Map.class);
        Assertions.assertThat(option.get()).isInstanceOf(Map.class);
        Assertions.assertThat(option.get().get("number")).isEqualTo(12);
    }

    @Test
    public void test_whenNestedFound_returnNumber() {

        var option = ObjectAccessor.access(aMap, "dict.text", String.class);
        Assertions.assertThat(option.get()).isEqualTo("Hello2");
    }
}
