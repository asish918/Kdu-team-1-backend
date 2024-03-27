package com.kdu.ibebackend.controller;

import com.kdu.ibebackend.constants.GraphQLQueries;
import com.kdu.ibebackend.dto.FetchProperties;
import com.kdu.ibebackend.dto.MinRates;
import com.kdu.ibebackend.service.GraphQLService;
import com.kdu.ibebackend.service.PropertyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Controller for all landing page endpoints
 */
@RestController
@RequestMapping(path = "/api/v1/landingpage")
public class LandingPageController {
    private final PropertyService propertyService;
    private final GraphQLService graphQLService;

    @Autowired
    LandingPageController(PropertyService propertyService, GraphQLService graphQLService) {
        this.propertyService = propertyService;
        this.graphQLService = graphQLService;
    }


    @Operation(summary = "Minimum Rates",
            parameters = {
                    @Parameter(name = "X-Api-Key", description = "API Key", required = true, in = ParameterIn.HEADER)
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = MinRates[].class))),
    })
    @GetMapping("/minrates")
    public ResponseEntity<Object> getMinimumNightRate() {
        return propertyService.getMinimumNightRatesList();
    }

    @Operation(summary = "Fetch Properties",
            parameters = {
                    @Parameter(name = "X-Api-Key", description = "API Key", required = true, in = ParameterIn.HEADER)
            })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful operation",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = FetchProperties.class))),
    })
    @GetMapping("/properties")
    public ResponseEntity<FetchProperties> getProperties() {
        String query = GraphQLQueries.fetchProperties;
        return graphQLService.executePostRequest(query, FetchProperties.class);
    }
}
