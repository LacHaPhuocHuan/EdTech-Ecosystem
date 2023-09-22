package com.thanhha.edtechcosystem.apigateway.security;

import com.thanhha.edtechcosystem.apigateway.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.ObjectInputFilter;
import java.nio.file.AccessDeniedException;

@Component
@RequiredArgsConstructor
@Log4j2
public class JwtGatewayFilter extends AbstractGatewayFilterFactory<JwtGatewayFilter.Config> {
    private final WebClient.Builder webClientBuilder;
    private final JwtUtils jwtUtils;
    private final RouteValidate routeValidate;
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
            ServerHttpRequest request=exchange.getRequest();
            log.info("URL : {}",request.getPath());
            if (routeValidate.isSecure.test(request)){
                HttpHeaders authHeader=request.getHeaders();
                if(!authHeader.containsKey(HttpHeaders.AUTHORIZATION))
                    exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                else {
                    String token = authHeader.get(HttpHeaders.AUTHORIZATION).get(0).strip();
                    if (!token.startsWith("Bearer"))
                        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    else {
                        String jwt = token.substring(7);
                        return webClientBuilder.build()
                                .post()
                                .uri("http://user-service/users/validateToken?token=" + jwt)
                                .retrieve().bodyToMono(UserDto.class)
                                .map(userDto -> {
                                    exchange.getRequest()
                                            .mutate()
                                            .header("X-auth-user-id", String.valueOf(userDto.getId()));
                                    return exchange;
                                }).flatMap(chain::filter);
                    }
                }
                return exchange.getResponse().setComplete();

            }
            return chain.filter(exchange);
        });
    }

    public static class Config {
        // empty class as I don't need any particular configuration
    }


}
