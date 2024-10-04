package com.investment.okta.client.Okta.Demo.For.investment.client.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/client")
public class ClientController {

    private final WebClient webClient;

    @Value("${okta.oauth2.issuer}/v1/token")
    private String tokenUri;

    @Value("${okta.oauth2.client-id}")
    private String clientId;

    @Value("${okta.oauth2.client-secret}")
    private String clientSecret;

    @Autowired
    public ClientController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8082").build();
    }

    @GetMapping
    public String newUser() {
        return "client page";
    }

    @GetMapping(value = "/resources", produces = MediaType.TEXT_PLAIN_VALUE)
    public Mono<String> message() {
        return retrieveAccessToken()
                .flatMap(token -> webClient.get()
                        .uri("/v1/resources/hello")
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + token)
                        .retrieve()
                        .bodyToMono(String.class)
                        .map(body -> "Success :: " + body)
                );
    }

    private Mono<String> retrieveAccessToken() {
        return webClient.post()
                .uri(tokenUri)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(getTokenRequestBody())
                .retrieve()
                .bodyToMono(Map.class)
                .map(response -> (String) response.get("access_token"));
    }

    private MultiValueMap<String, String> getTokenRequestBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("grant_type", "client_credentials");
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("scope", "internal");
        return body;
    }
}
