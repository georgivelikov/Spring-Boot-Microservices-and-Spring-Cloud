package com.appsdeveloperblog.photoapp.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.server.ServerWebExchange;

import com.google.common.net.HttpHeaders;

import io.jsonwebtoken.Jwts;
import reactor.core.publisher.Mono;

public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    @Autowired
    Environment env;

    public static class Config {

    }

    @Override
    public GatewayFilter apply(AuthorizationHeaderFilter.Config config) {
	return (exchange, chain) -> {
	    ServerHttpRequest req = exchange.getRequest();
	    if (!req.getHeaders()
		    .containsKey(HttpHeaders.AUTHORIZATION)) {
		return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
	    }

	    String authHeader = req.getHeaders()
		    .get(HttpHeaders.AUTHORIZATION)
		    .get(0);

	    String jwt = authHeader.replace("Bearer", "");

	    if (!isJwtValid(jwt)) {
		return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
	    }

	    return chain.filter(exchange);
	};
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err, HttpStatus httpStatus) {
	ServerHttpResponse res = exchange.getResponse();

	res.setStatusCode(httpStatus);

	return res.setComplete();
    }

    private boolean isJwtValid(String jwt) {
	boolean returnValue = true;

	String subject = Jwts.parser()
		.setSigningKey(env.getProperty("token.secret"))
		.parseClaimsJws(jwt)
		.getBody()
		.getSubject();

	if (subject == null || subject.isEmpty()) {
	    returnValue = false;
	}

	return returnValue;
    }
}
