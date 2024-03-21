package com.kdu.ibebackend.controller;

import com.kdu.ibebackend.constants.GraphQLQueries;
import com.kdu.ibebackend.dto.response.FinalResponse;
import com.kdu.ibebackend.dto.response.RoomRateDTOResponse;
import com.kdu.ibebackend.dto.response.RoomResultDTOResponse;
import com.kdu.ibebackend.service.GraphQLService;
import com.kdu.ibebackend.service.RoomResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping(path = "/api/roomresult")
public class RoomResultController {
    private GraphQLService graphQLService;
    private RoomResultService roomResultService;

    @Autowired
    public RoomResultController(GraphQLService graphQLService, RoomResultService roomResultService) {
        this.graphQLService = graphQLService;
        this.roomResultService = roomResultService;
    }

    @GetMapping("/trial")
    public ResponseEntity<RoomResultDTOResponse> testRoomRes() {
        String query = GraphQLQueries.roomRes;
        ResponseEntity<RoomResultDTOResponse> res = graphQLService.executePostRequest(query, RoomResultDTOResponse.class);
        return res;
    }

    @GetMapping("/trial1")
    public ResponseEntity<RoomRateDTOResponse> testRoomRate() {
        String query = GraphQLQueries.roomRateRoomTypeMappings;
        ResponseEntity<RoomRateDTOResponse> res = graphQLService.executePostRequest(query, RoomRateDTOResponse.class);
        return res;
    }

    //    MethodArgumentNotValidException
    @GetMapping("/trial2")
    public HashMap<Integer, FinalResponse> testRoomRate1() {
        return roomResultService.finalResponseMap();
    }
}
