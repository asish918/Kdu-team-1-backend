package com.kdu.ibebackend.controller;

import com.kdu.ibebackend.dto.GraphQLResponse;
import com.kdu.ibebackend.entities.Room;
//import com.kdu.ibebackend.repository.RoomRepository;
import com.kdu.ibebackend.service.GraphQLService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.Optional;

@RestController
public class TestController {

//    private final RoomRepository roomRepository;

    private final GraphQLService graphQLService;

    @Autowired
    public TestController(GraphQLService graphQLService) {
//        this.roomRepository = roomRepository;
        this.graphQLService = graphQLService;
    }

    @Operation(summary = "Test the working of the server", description = "Returns \"Hey there!! The server works great \uD83D\uDC4D\"")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",  content = @Content(
                    schema = @Schema(implementation = String.class)
            )),
    })
    @CrossOrigin(origins = "*")
    @GetMapping("/test")
    public String testHealthEndpoint() {
        return "Hey there!! The server works great 👍";
    }


    @Operation(summary = "Test the working of GraphQL API calls", description = "Returns GraphQL backend response")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved",  content = @Content(
                    schema = @Schema(implementation = GraphQLResponse.class)
            )),
    })
    @CrossOrigin(origins = "*")
    @GetMapping("/api/graphql")
    public Mono<GraphQLResponse> testGraphQL() {
        return graphQLService.executePostRequest();
    }

//    @QueryMapping
//    public Room roomById(@Argument Long id) {
//        Optional<Room> roomRes = roomRepository.findById(id);

//        return roomRes.orElse(null);
//    }
}
