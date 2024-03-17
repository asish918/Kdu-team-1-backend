package com.kdu.ibebackend.service;

import com.kdu.ibebackend.utils.GraphUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.http.MediaType;

/**
 * Service for executing GraphQL Service calls to given backend
 */
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

    public <T> ResponseEntity<T> executePostRequest(String graphqlQuery, Class<T> responseClass) {
        String formattedQuery = GraphUtils.convertToGraphQLRequest(graphqlQuery);

        T response = webClient.post()
                .uri("/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Api-Key", apiKey)
                .bodyValue(formattedQuery)
                .retrieve()
                .bodyToMono(responseClass)
                .block();

        return ResponseEntity.ok(response);
    }
}
