package com.example.api.application.request;

import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BuyerItemsCouponRequest {

    private Set<String> itemIds;

    private Float amount;
}
