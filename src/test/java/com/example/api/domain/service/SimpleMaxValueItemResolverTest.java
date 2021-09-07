package com.example.api.domain.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SimpleMaxValueItemResolverTest {

    private SimpleMaxValueItemResolver resolver = new SimpleMaxValueItemResolver();

    @Test
    void calculate_shouldReturnItems() {

        // given
        Map<String, Float> input = new LinkedHashMap<>();
        input.put("MLA1", 100f);
        input.put("MLA2", 210f);
        input.put("MLA3", 260f);
        input.put("MLA4", 80f);
        input.put("MLA5", 90f);

        Float amount = 500f;

        // when
        List<String> result = resolver.calculate(input, amount);

        // verify
        Assertions.assertEquals(4, result.size());

        List.of("MLA1", "MLA2", "MLA4", "MLA5").forEach(i -> {
            Assertions.assertTrue(result.contains(i));
        });
    }

    @Test
    void calculate_shouldReturnEmpty() {

        Map<String, Float> input = new LinkedHashMap<>();
        input.put("MLA1", 100f);
        input.put("MLA2", 210f);
        input.put("MLA3", 260f);
        input.put("MLA4", 80f);
        input.put("MLA5", 90f);

        Float amount = 50f;

        List<String> result = resolver.calculate(input, amount);

        Assertions.assertTrue(result.isEmpty());
    }

}