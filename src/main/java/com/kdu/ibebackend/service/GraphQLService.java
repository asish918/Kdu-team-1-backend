package com.kdu.ibebackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kdu.ibebackend.models.SecretConfig;
import com.kdu.ibebackend.utils.GraphUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import org.springframework.http.MediaType;

/**
 * Service for executing GraphQL Service calls to the backend
 */
@Service
public class GraphQLService {
    private final SecretConfig secretConfig;
    private final WebClient webClient;

    @Autowired
    public GraphQLService(SecretsManagerService secretsManagerService, @Value("${awsSecretName}") String secretName) throws JsonProcessingException {
        String awsSecret = secretsManagerService.getSecretValue(secretName);
        ObjectMapper mapper = new ObjectMapper();
        secretConfig = mapper.readValue(awsSecret, SecretConfig.class);
        this.webClient = WebClient.create(secretConfig.getGraphqlUrl());
    }

    public <T> ResponseEntity<T> executePostRequest(String graphqlQuery, Class<T> responseClass) {
        String formattedQuery = GraphUtils.convertToGraphQLRequest(graphqlQuery);

        T response = webClient.post()
                .uri("/graphql")
                .contentType(MediaType.APPLICATION_JSON)
                .header("X-Api-Key", secretConfig.getGraphqlApiKey())
                .bodyValue(formattedQuery)
                .retrieve()
                .bodyToMono(responseClass)
                .block();

        return ResponseEntity.ok(response);
    }
}
