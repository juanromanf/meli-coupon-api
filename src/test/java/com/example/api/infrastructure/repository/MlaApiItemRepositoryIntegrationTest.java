package com.example.api.infrastructure.repository;

import java.util.List;
import java.util.Set;

import com.example.api.domain.model.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

class MlaApiItemRepositoryIntegrationTest {

    private MlaApiItemRepository itemRepositoryRetriever;

    @BeforeEach
    void setup() {

        WebClient webClient = WebClient
                .builder()
                .baseUrl("https://api.mercadolibre.com")
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build();

        itemRepositoryRetriever = new MlaApiItemRepository(webClient);
    }

    @Test
    void testMultiGetItems() {

        Set<String> ids = Set.of("MLA811601010", "MLA811601011");

        List<Item> itemList = itemRepositoryRetriever.load(ids)
                .collectList()
                .block();

        itemList.stream()
                .forEach(item -> {
                    Assertions.assertNotNull(item.getId());
                    Assertions.assertNotNull(item.getPrice());
                });
    }
}