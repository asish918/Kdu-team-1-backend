package com.kdu.ibebackend.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kdu.ibebackend.dto.request.SearchParamDTO;
import com.kdu.ibebackend.dto.response.RoomResultResponse;
import com.kdu.ibebackend.dto.response.SearchResponseDTO;
import com.kdu.ibebackend.service.GraphQLService;
import com.kdu.ibebackend.service.RoomResultService;
import com.kdu.ibebackend.utils.RoomUtils;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@Slf4j
@RequestMapping(path = "/api/roomresult")
public class RoomResultController {
    private GraphQLService graphQLService;
    private RoomResultService roomResultService;

    @Autowired
    public RoomResultController(GraphQLService graphQLService, RoomResultService roomResultService) {
        this.graphQLService = graphQLService;
        this.roomResultService = roomResultService;
    }

    // MethodArgumentNotValidException

    @PostMapping("/search")
    public SearchResponseDTO searchResults(@RequestBody SearchParamDTO searchParamDTO,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) throws JsonProcessingException {
        HashMap<Integer, RoomResultResponse> resMap = roomResultService.finalResponseMap(searchParamDTO);
        List<RoomResultResponse> resList = roomResultService.filteredData(searchParamDTO, RoomUtils.hashMapToList(resMap));
        log.info(resList.toString());

        if (resList.isEmpty()) {
            return new SearchResponseDTO(new ArrayList<>(), 0, 0);
        }

        int totalItems = resMap.size();
        int totalPages = (int) Math.ceil((double) totalItems / size);

        int startIndex = page * size;
        int endIndex = Math.min(startIndex + size, totalItems);

        if(resList.size() == 1) {
            return new SearchResponseDTO(resList, 1, page);
        }

        List<RoomResultResponse> pageResults = resList.subList(startIndex, endIndex);
        return new SearchResponseDTO(pageResults, totalPages, page);
    }
}
