package com.thanhha.edtechcosystem.apigateway.security;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Service;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Service
public class RouteValidate {
    public  final List<String> securePaths= Arrays.asList("/eureka", "/register", "/login", "/refresh-token", "/demo");
    public  Predicate<ServerHttpRequest> isSecure=
            serverHttpRequest -> securePaths.stream()
                    .noneMatch(path->serverHttpRequest.getURI().getPath().contains(path))
            ;

}
