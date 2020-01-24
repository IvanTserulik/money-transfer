package edu.itserulik.transfer.controller;

import io.netty.handler.codec.http.HttpMethod;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class GetController<R, T> extends AbstractController<R, T> {

    @Override
    public final HttpMethod httpMethod() {
        return HttpMethod.GET;
    }

}
