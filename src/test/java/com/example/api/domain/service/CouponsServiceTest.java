package com.example.api.domain.service;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import com.example.api.domain.model.Item;
import com.example.api.domain.model.ItemsByCouponAmountResult;
import com.example.api.domain.port.outgoing.ItemDetailsRetrieverPort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
class CouponsServiceTest {

    @Mock
    private ItemDetailsRetrieverPort itemDetailsRetriever;

    @Mock
    private MaxValueItemResolver itemResolver;

    @InjectMocks
    private CouponsService service;

    @Test
    void suggestBestPurchase() {

        // given
        Set<String> input = Set.of("MLA1", "MLA2", "MLA3", "MLA4", "MLA5");
        Float amount = 400f;

        List<Item> items = new LinkedList<>();
        for (int i = 1; i <= 5; i++) {
            Item item = Item.builder()
                    .id("MLA" + i)
                    .price(i * 100f)
                    .build();
            items.add(item);
        }

        // when
        Mockito.when(itemDetailsRetriever.load(input))
                .thenReturn(Flux.fromIterable(items));

        Mockito.when(itemResolver.calculate(Mockito.anyMap(), Mockito.eq(amount)))
                .thenReturn(List.of("MLA1", "MLA2"));

        Mono<ItemsByCouponAmountResult> result = service.suggestBestPurchase(input, amount);

        // verify
        StepVerifier.create(result)
                .expectNextCount(1)
                .expectComplete()
                .verify();

        Mockito.verify(itemDetailsRetriever, Mockito.times(1))
                .load(input);

        Mockito.verify(itemResolver, Mockito.times(1))
                .calculate(Mockito.anyMap(), Mockito.eq(amount));

        ItemsByCouponAmountResult itemsByCouponAmountResult = result.block();

        Assertions.assertTrue(itemsByCouponAmountResult.getItemIds().containsAll(List.of("MLA1", "MLA2")));
    }
}