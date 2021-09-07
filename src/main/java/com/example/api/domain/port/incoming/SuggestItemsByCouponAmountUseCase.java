package com.example.api.domain.port.incoming;

import java.util.Set;

import com.example.api.domain.model.ItemsByCouponAmountResult;
import reactor.core.publisher.Mono;

public interface SuggestItemsByCouponAmountUseCase {

    Mono<ItemsByCouponAmountResult> suggestBestPurchase(Set<String> itemIds, Float amount);
}
