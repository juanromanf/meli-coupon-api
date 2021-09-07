package com.example.api.infrastructure.repository.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MlaItemEntityResponse {

    private Integer code;

    private MlaItemEntity body;
}
