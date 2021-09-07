package com.example.api.domain.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SimpleMaxValueItemResolver implements MaxValueItemResolver {

    @Override
    public List<String> calculate(Map<String, Float> itemsMap, Float maxAmount) {

        List<String> ids = new ArrayList<>();
        Float sum = Float.valueOf(0);

        for (Map.Entry<String, Float> e : itemsMap.entrySet()) {

            if ((e.getValue() + sum) <= maxAmount) {
                sum += e.getValue();
                ids.add(e.getKey());
            }
        }

        return ids;
    }
}
