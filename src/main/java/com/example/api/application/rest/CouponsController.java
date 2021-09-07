package com.example.api.application.rest;

import java.util.NoSuchElementException;

import com.example.api.application.request.BuyerItemsCouponRequest;
import com.example.api.application.response.ApiResponse;
import com.example.api.domain.model.ItemsByCouponAmountResult;
import com.example.api.domain.port.incoming.SuggestItemsByCouponAmountUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RestController
public class CouponsController {

    private final SuggestItemsByCouponAmountUseCase suggestItemsByCouponAmountUseCase;

    @PostMapping("/coupon")
    public Mono<ResponseEntity<?>> findBestMatch(@RequestBody BuyerItemsCouponRequest request) {

        return suggestItemsByCouponAmountUseCase.suggestBestPurchase(request.getItemIds(), request.getAmount())
                .switchIfEmpty(Mono.error(new NoSuchElementException("No enough money for items :(")))
                .map(this::successResponse)
                .map(body -> ResponseEntity.ok(body));
    }

    private ApiResponse<ItemsByCouponAmountResult> successResponse(ItemsByCouponAmountResult result) {

        return ApiResponse.<ItemsByCouponAmountResult>builder()
                .status(HttpStatus.OK.value())
                .payload(result)
                .build();
    }

}
