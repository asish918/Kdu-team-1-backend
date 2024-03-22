package com.kdu.ibebackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kdu.ibebackend.dto.request.SearchParamDTO;
import com.kdu.ibebackend.dto.response.FinalResponse;
import com.kdu.ibebackend.service.GraphQLService;
import com.kdu.ibebackend.service.RoomResultService;
import com.kdu.ibebackend.utils.RoomUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

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

    //    MethodArgumentNotValidException

    @PostMapping("/search")
    public List<FinalResponse> searchResults(@RequestBody SearchParamDTO searchParamDTO, @RequestParam(defaultValue = "0") int page,
    @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {
        HashMap<Integer, FinalResponse> resMap = roomResultService.finalResponseMap(searchParamDTO);
        List<FinalResponse> resList = roomResultService.filteredData(searchParamDTO, RoomUtils.hashMapToList(resMap));

        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, resMap.size());

        return resList.subList(startIndex, endIndex);
    }
}
