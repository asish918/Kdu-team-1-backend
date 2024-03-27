package com.kdu.ibebackend.controller;

import com.kdu.ibebackend.dto.request.SearchParamDTO;
import com.kdu.ibebackend.service.RoomResultService;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping(path = "/api/v1/roomresult")
public class RoomResultController {
    private RoomResultService roomResultService;

    @Autowired
    public RoomResultController(RoomResultService roomResultService) {
        this.roomResultService = roomResultService;
    }

    @PostMapping("/search")
    public ResponseEntity<Object> searchResults(@RequestBody @Valid SearchParamDTO searchParamDTO,
                                                           @RequestParam(defaultValue = "0") @Min(0) @NotNull @Valid int page,
                                                           @RequestParam(defaultValue = "10") @Min(1) @NotNull @Valid int size) {

        return roomResultService.paginatedData(searchParamDTO, page, size);
    }
}
