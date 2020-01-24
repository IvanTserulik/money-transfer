package edu.itserulik.transfer.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.itserulik.transfer.common.exception.BadRequestException;
import edu.itserulik.transfer.http.ResponseWrapper;
import edu.itserulik.transfer.model.dto.ErrorDto;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.QueryStringDecoder;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.netty.http.server.HttpServerRequest;
import reactor.netty.http.server.HttpServerResponse;

import java.lang.reflect.ParameterizedType;
import java.nio.charset.Charset;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON;

@Slf4j
public abstract class AbstractController<R, T> {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public abstract Mono<T> processRequest(Mono<R> request, Map<String, ?> parameters);

    public abstract HttpMethod httpMethod();

    public abstract String path();

    public Mono<Void> response(HttpServerRequest request, HttpServerResponse response) {
        log.info("request : {}", request);

        var decoder = new QueryStringDecoder(request.uri());
        log.info("parameters : {}", decoder.parameters());

        var httpHeaders = new DefaultHttpHeaders();
        httpHeaders.add(CONTENT_TYPE, APPLICATION_JSON);
        response.headers(httpHeaders);

        return request.receive()
                .aggregate()
                .map(httpContent -> httpContent.toString(Charset.defaultCharset()))
                .map(this::convertObject)
                .as(req -> processRequest(req, decoder.parameters()))
                .map(this::convertJson)
                .map(resp -> new ResponseWrapper<String>(resp, HttpResponseStatus.OK))
                .onErrorResume(e ->
                        Mono.just(new ResponseWrapper<String>(convertJson(
                                ErrorDto.builder()
                                        .message(e.getMessage())
                                        .build()),
                                HttpResponseStatus.BAD_REQUEST)))
                .flatMap(s -> doCommit(() -> response.sendString(Mono.just(s.getContent())).then(), response, s.getStatus()));
    }

    private Mono<Void> doCommit(Supplier<? extends Mono<Void>> writeAction,
                                HttpServerResponse response, HttpResponseStatus status) {
        Supplier<? extends Mono<Void>> setProperties = () ->
                Mono.fromRunnable(() -> response.status(status));

        Flux<Void> commit = Flux.empty();
        commit = commit.concatWith(setProperties.get())
                .concatWith(writeAction.get());

        return commit.then();
    }

    private <T> String convertJson(T object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.info("Cannot process request: {}", object);
            throw new BadRequestException("Error during request serialization");
        }
    }

    @SuppressWarnings("unchecked")
    private R convertObject(String json) {
        var type = (ParameterizedType) this.getClass().getGenericSuperclass();
        var genericsArray = type.getActualTypeArguments();
        var clazz = (Class<R>) genericsArray[0];
        if (Void.TYPE.equals(clazz)) {
            return null;
        }
        try {
            return objectMapper.readValue(json, clazz);
        } catch (JsonProcessingException e) {
            log.info("Cannot process response: {} {}", json, e);
            throw new BadRequestException("Error during response deserialization");
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(path(), httpMethod());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof AbstractController))
            return false;
        var other = (AbstractController) obj;
        return Objects.equals(httpMethod(), other.httpMethod())
                && Objects.equals(path(), other.path());
    }

    @Override
    public String toString() {
        return getClass().toString();
    }
}

