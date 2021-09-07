package com.example.api.domain.service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import com.example.api.domain.model.Item;
import com.example.api.domain.model.ItemsByCouponAmountResult;
import com.example.api.domain.port.incoming.SuggestItemsByCouponAmountUseCase;
import com.example.api.domain.port.outgoing.ItemDetailsRetrieverPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@RequiredArgsConstructor
@Service
public class CouponsService implements SuggestItemsByCouponAmountUseCase {

    private final ItemDetailsRetrieverPort itemDetailsRetrieverPort;

    private final MaxValueItemResolver itemResolver;

    @Override
    public Mono<ItemsByCouponAmountResult> suggestBestPurchase(Set<String> itemIds, Float maxAmount) {

        return itemDetailsRetrieverPort.load(itemIds)
                .filter(item -> filterExpensiveItems(item, maxAmount))
                .collectList()
                .flatMap(items -> calculateItems(items, maxAmount))
                .subscribeOn(Schedulers.boundedElastic());
    }

    private boolean filterExpensiveItems(Item item, Float amount) {

        return Objects.nonNull(item.getPrice()) && item.getPrice() <= amount;
    }

    private Mono<ItemsByCouponAmountResult> calculateItems(List<Item> items, Float amount) {

        Map<String, Float> prices = toPricesMap(items);
        List<String> selectedItemsIds = itemResolver.calculate(prices, amount);

        if (CollectionUtils.isEmpty(selectedItemsIds)) {
            return Mono.empty();
        }

        Float selectedItemsTotal = items.stream()
                .filter(item -> selectedItemsIds.contains(item.getId()))
                .map(Item::getPrice)
                .reduce(0f, Float::sum);

        return Mono.just(ItemsByCouponAmountResult.builder()
                .itemIds(selectedItemsIds)
                .amount(selectedItemsTotal)
                .build());
    }

    private Map<String, Float> toPricesMap(List<Item> items) {

        return items.stream()
                .collect(Collectors.toMap(Item::getId, Item::getPrice));
    }
}
