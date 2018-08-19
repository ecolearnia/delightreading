package com.delightreading.common;

import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.util.HashMap;

import static org.assertj.core.api.Assertions.assertThat;

public class VariousTest {


    @Test
    public void test_spel() {

        var aMap = new HashMap<String, Object>(){{
            put("text", "Hello");
            put("number", 2);
            put("dict", new HashMap<String, Object>(){{
                put("text", "Hello2");
                put("number", 12);
            }});
        }};


        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext(aMap);
        context.setVariable("aMap", aMap);
        var string = parser.parseExpression("#aMap['text']").getValue(context, String.class);
        var stringInMap = parser.parseExpression("#aMap['dict']['text']").getValue(context, String.class);
        var integerInMap = parser.parseExpression("#aMap['dict']['number']").getValue(context, Integer.class);
        var nonexistent = parser.parseExpression("#aMap['NONEXISENT']").getValue(context, String.class);

        assertThat(string).isEqualTo("Hello");
        assertThat(stringInMap).isEqualTo("Hello2");
        assertThat(integerInMap).isEqualTo(12);
        assertThat(nonexistent).isNull();
    }
}
