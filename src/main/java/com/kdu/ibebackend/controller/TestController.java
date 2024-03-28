package com.kdu.ibebackend.controller;

import com.kdu.ibebackend.constants.Constants;
import com.kdu.ibebackend.constants.GraphQLQueries;
import com.kdu.ibebackend.dto.GraphQLResponse;
import com.kdu.ibebackend.service.GraphQLService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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
@CrossOrigin("*")
public class TestController {
    private final GraphQLService graphQLService;

    @Autowired
    public TestController(GraphQLService graphQLService) {
        this.graphQLService = graphQLService;
    }


    @Operation(summary = "Test Working Server",
            parameters = {
                    @Parameter(name = "X-Api-Key", description = "API Key", required = true, in = ParameterIn.HEADER)
            })
    @GetMapping("/test")
    public String testHealthEndpoint() {
        return Constants.SERVER_SUCCESS;
    }


    @GetMapping("/api/graphql")
    public ResponseEntity<GraphQLResponse> testGraphQL() {
        String graphqlQuery = GraphQLQueries.testQuery;
        return graphQLService.executePostRequest(graphqlQuery, GraphQLResponse.class);
    }
}
