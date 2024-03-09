package com.kdu.ibebackend.service;

import com.kdu.ibebackend.dto.GraphQLResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import org.springframework.http.MediaType;

@Service
public class GraphQLService {
    private String graphqlUrl;

    private String apiKey;
    private final WebClient webClient;

    @Autowired
    public GraphQLService(@Value( "${graphql.url}" ) String graphqlUrl, @Value( "${graphql.api.key}" ) String apiKey) {
        this.graphqlUrl = graphqlUrl;
        this.apiKey = apiKey;
        this.webClient = WebClient.create(graphqlUrl);
    }

    public Mono<GraphQLResponse> executePostRequest() {
        String graphqlQuery = "{ \"query\": \"query FindPropertyByName { countRooms }\" }";

        return webClient.post()
                .uri("/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Api-Key", apiKey)
                .bodyValue(graphqlQuery)
                .retrieve()
                .bodyToMono(GraphQLResponse.class);
    }
}
