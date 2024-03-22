package com.kdu.ibebackend.utils;

import com.kdu.ibebackend.dto.request.SearchParamDTO;

public class ParamUtils {
    public static boolean isNumeric(String str) {
        return str != null && str.matches("[0-9.]+");
    }

    public static boolean containsFilters(SearchParamDTO searchParamDTO) {
        return !searchParamDTO.getBedTypes().isEmpty() || !searchParamDTO.getRoomTypes().isEmpty();
    }

    public static boolean containsSort(SearchParamDTO searchParamDTO) {
        return searchParamDTO.getPriceSort() != null || searchParamDTO.getRatingSort() != null || searchParamDTO.getAreaSort() != null;
    }
}
