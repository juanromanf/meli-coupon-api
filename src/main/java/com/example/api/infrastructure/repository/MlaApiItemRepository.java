package com.example.api.infrastructure.repository;

import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.example.api.domain.model.Item;
import com.example.api.domain.port.outgoing.ItemDetailsRetrieverPort;
import com.example.api.infrastructure.repository.model.MlaItemEntity;
import com.example.api.infrastructure.repository.model.MlaItemEntityResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@Component
public class MlaApiItemRepository implements ItemDetailsRetrieverPort {

    /**
     * @see <a href="https://developers.mercadolibre.com.co/es_ar/items-y-busquedas#Multiget">Multiget</a>
     */
    public static final int MULTI_GET_MAX_SIZE = 20;

    public static final String DEFAULT_ITEM_ATTRIBUTES = "id,price,title,currency_id,category_id";

    private WebClient apiClient;

    public MlaApiItemRepository(@Qualifier("mlApiWebClient") WebClient apiClient) {

        this.apiClient = apiClient;
    }

    @Override
    public Flux<Item> load(Set<String> ids) {

        return Flux.fromIterable(ids)
                .buffer(MULTI_GET_MAX_SIZE)
                .flatMap(this::multiGet)
                .log()
                .flatMapIterable(this::toDomain);
    }

    public Mono<List<MlaItemEntity>> multiGet(List<String> ids) {

        final String strIds = ids.stream().collect(Collectors.joining(","));

        StringBuilder uri = new StringBuilder("/items")
                .append("?ids=").append(strIds)
                .append("&attributes=").append(DEFAULT_ITEM_ATTRIBUTES);

        return apiClient.get()
                .uri(uri.toString())
                .retrieve()
                .bodyToMono(asListOfMlaItemEntity())
                .log()
                .flatMapIterable(Function.identity())
                .map(r -> r.getBody())
                .collectList();
    }

    private ParameterizedTypeReference<List<MlaItemEntityResponse>> asListOfMlaItemEntity() {
        return new ParameterizedTypeReference<>() {
            // empty
        };
    }

    private List<Item> toDomain(List<MlaItemEntity> itemEntities) {

        return itemEntities.stream()
                .map(this::toDomain)
                .collect(Collectors.toList());
    }

    private Item toDomain(MlaItemEntity itemEntity) {

        return Item.builder()
                .id(itemEntity.getId())
                .title(itemEntity.getTitle())
                .categoryId(itemEntity.getCategoryId())
                .price(itemEntity.getPrice())
                .currencyId(itemEntity.getCurrencyId())
                .build();
    }
}
