package edu.itserulik.transfer.http;

import io.netty.handler.codec.http.HttpMethod;
import lombok.Getter;

import java.util.Set;
import java.util.stream.Collectors;

public enum SupportedHttpMethod {

    GET(HttpMethod.GET),
    POST(HttpMethod.POST);

    @Getter
    private HttpMethod httpMethod;

    SupportedHttpMethod(HttpMethod httpMethod) {
        this.httpMethod = httpMethod;
    }

    public static Set<HttpMethod> valuesSet(){
        return Set.of(values()).stream()
                .map(SupportedHttpMethod::getHttpMethod)
                .collect(Collectors.toSet());
    }
}
