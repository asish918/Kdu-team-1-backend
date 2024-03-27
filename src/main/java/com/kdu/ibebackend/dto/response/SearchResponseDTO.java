package com.kdu.ibebackend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchResponseDTO {
    private List<RoomResultResponse> results;
    private int totalPages;
    private int currentPage;
}
