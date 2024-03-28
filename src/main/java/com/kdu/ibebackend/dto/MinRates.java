package com.kdu.ibebackend.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.time.LocalDate;

/**
 * Response DTO for fetching Minimum Nightly Rates
 */
@Data
public class MinRates {
    private LocalDate date;
    private Double price;

    public MinRates(LocalDate date, Double price) {
        this.date = date;
        this.price = price;
    }

    public static List<MinRates> convertMapToList(Map<LocalDate, Double> map) {
        List<MinRates> resultList = new ArrayList<>();
        for (Map.Entry<LocalDate, Double> entry : map.entrySet()) {
            resultList.add(new MinRates(entry.getKey(), entry.getValue()));
        }

        return resultList;
    }

    public static void sortListByDate(List<MinRates> list) {
        list.sort(new Comparator<MinRates>() {
            @Override
            public int compare(MinRates o1, MinRates o2) {
                return o1.getDate().compareTo(o2.getDate());
            }
        });
    }
}
