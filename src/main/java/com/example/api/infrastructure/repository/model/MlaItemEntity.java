package com.example.api.infrastructure.repository.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MlaItemEntity {

    private String id;

    private String title;

    private String categoryId;

    private Float price;

    private String currencyId;

    @Override
    public String toString() {

        return "MlaItemEntity{" +
                "id='" + id + '\'' +
                ", price=" + price +
                '}';
    }
}
