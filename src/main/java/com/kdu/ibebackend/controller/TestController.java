package com.kdu.ibebackend.controller;

import com.kdu.ibebackend.constants.GraphQLQueries;
import com.kdu.ibebackend.dto.GraphQLResponse;
import com.kdu.ibebackend.repository.TenantDynamoRepository;
import com.kdu.ibebackend.service.GraphQLService;
import com.kdu.ibebackend.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * A Test Controller that defines test endpoints for consuming in frontend. CORS
 * has been allowed on
 * this entire controller
 */
@RestController
public class TestController {
    private final GraphQLService graphQLService;

    @Autowired
    public TestController(GraphQLService graphQLService) {
        this.graphQLService = graphQLService;
    }


    @GetMapping("/test")
    public String testHealthEndpoint() {
        return "Hey there!! The server works great 👍";
    }


    @GetMapping("/api/graphql")
    public ResponseEntity<GraphQLResponse> testGraphQL() {
        String graphqlQuery = GraphQLQueries.testQuery;
        return graphQLService.executePostRequest(graphqlQuery, GraphQLResponse.class);
    }
}
