package com.example.api.domain.service;

import java.util.List;
import java.util.Map;

public interface MaxValueItemResolver {

    List<String> calculate(Map<String, Float> itemsMap, Float maxAmount);

}
