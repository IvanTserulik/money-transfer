package edu.itserulik.transfer.http;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import edu.itserulik.transfer.controller.AbstractController;
import io.netty.handler.codec.http.HttpMethod;
import lombok.extern.slf4j.Slf4j;
import reactor.netty.DisposableServer;
import reactor.netty.http.server.HttpServer;
import reactor.netty.http.server.HttpServerRoutes;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
public class NettyServer {

    private int port;
    private Set<AbstractController> controllers;

    @Inject
    public NettyServer(@Named("server.port") int port, Set<AbstractController> controllers) {
        this.port = port;
        this.controllers = controllers;
    }

    public DisposableServer getServer() {
        DisposableServer server = HttpServer.create()
                .host("localhost")
                .port(port)
                .route(routes -> processControllers(routes, controllers))
                .bindNow();
        log.info("Netty server prepared");
        return server;
    }

    private void processControllers(HttpServerRoutes routes, Set<AbstractController> controllers) {
        validateControllers(controllers);

        controllers.forEach(controller -> {
                    if (controller.httpMethod().equals(HttpMethod.GET)) {
                        routes.get(controller.path(),
                                (request, response) -> controller.response(request, response));
                        return;
                    }
                    if (controller.httpMethod().equals(HttpMethod.POST)) {
                        routes.post(controller.path(),
                                (request, response) -> controller.response(request, response));

                        return;
                    }
                    throw new UnsupportedOperationException("SupportedHttpMethod was updated but controller was not added to routes");
                }
        );
    }

    private void validateControllers(Set<AbstractController> controllers) {
        var unsupportedControllers = controllers.stream()
                .filter(controller -> !SupportedHttpMethod.valuesSet().contains(controller.httpMethod()))
                .collect(Collectors.toSet());
        if (!unsupportedControllers.isEmpty()) {
            throw new UnsupportedOperationException("Unsupported methods of Controllers found: " + unsupportedControllers);
        }

        var map = new HashMap<>();
        var duplicatesSet = new HashSet<>();
        controllers.forEach(controller -> {
            if (map.containsKey(controller)) {
                duplicatesSet.add(controller);
            }
            map.put(controller, controller);
        });

        if (!duplicatesSet.isEmpty()) {
            throw new UnsupportedOperationException("Duplicated Controllers found: " + duplicatesSet);
        }
    }
}
