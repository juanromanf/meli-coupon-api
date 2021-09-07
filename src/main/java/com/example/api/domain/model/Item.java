package com.example.api.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Item {

    private String id;

    private String title;

    private String categoryId;

    private Float price;

    private String currencyId;

    @Override
    public String toString() {
        return "Item{" +
                "id='" + id + '\'' +
                ", price=" + price +
                '}';
    }
}
