package com.example.api.domain.port.outgoing;

import java.util.Set;

import com.example.api.domain.model.Item;
import reactor.core.publisher.Flux;

public interface ItemDetailsRetrieverPort {

    Flux<Item> load(Set<String> ids);
}
