package com.example.api.application.error;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.NoSuchElementException;

import com.example.api.application.response.ApiResponse;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.NestedExceptionUtils;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Configuration
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiErrorHandler implements ErrorWebExceptionHandler {

    private ObjectMapper mapper;

    public ApiErrorHandler(ObjectMapper mapper) {

        this.mapper = mapper;
    }

    @Override
    public Mono<Void> handle(ServerWebExchange serverWebExchange, Throwable throwable) {

        DataBufferFactory bufferFactory = serverWebExchange.getResponse().bufferFactory();

        Throwable e = NestedExceptionUtils.getMostSpecificCause(throwable);
        ApiResponse response;

        if (e instanceof ResponseStatusException) {

            ApiError error = translateToApiError((ResponseStatusException) e);
            response = toApiResponse(((ResponseStatusException) e).getStatus(), error);

        } else if (e instanceof NoSuchElementException) {

            ApiError error = translateToApiError((NoSuchElementException) e);
            response = toApiResponse(HttpStatus.NOT_FOUND, error);
        } else {

            log.error("unexpected error !", e);

            ApiError error = translateToApiError(e);
            response = toApiResponse(HttpStatus.INTERNAL_SERVER_ERROR, error);
        }

        DataBuffer dataBuffer = wrapResponse(bufferFactory, response);

        serverWebExchange.getResponse().setStatusCode(HttpStatus.resolve(response.getStatus()));
        serverWebExchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);

        return serverWebExchange.getResponse().writeWith(Mono.just(dataBuffer));
    }

    private DataBuffer wrapResponse(DataBufferFactory bufferFactory, ApiResponse response) {

        DataBuffer dataBuffer;
        try {
            dataBuffer = bufferFactory.wrap(mapper.writeValueAsBytes(response));

        } catch (JsonProcessingException jpe) {
            log.warn("json processing error!", jpe);

            dataBuffer = bufferFactory.wrap("".getBytes(Charset.defaultCharset()));
        }

        return dataBuffer;
    }

    private ApiResponse toApiResponse(HttpStatus status, ApiError error) {

        return ApiResponse.builder()
                .status(status.value())
                .errors(Arrays.asList(error))
                .build();
    }

    private ApiError translateToApiError(ResponseStatusException e) {

        return ApiError.builder()
                .errorMessage(e.getStatus().getReasonPhrase())
                .build();
    }

    private ApiError translateToApiError(NoSuchElementException e) {

        return ApiError.builder()
                .errorMessage(e.getMessage())
                .errorCause("NoSuchElement")
                .build();
    }

    private ApiError translateToApiError(Throwable e) {

        return ApiError.builder()
                .errorMessage("Unexpected error :S")
                .errorCause(e.getMessage())
                .build();
    }
}
