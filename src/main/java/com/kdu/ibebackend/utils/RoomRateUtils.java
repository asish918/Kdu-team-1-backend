package com.kdu.ibebackend.utils;

import java.util.HashMap;

public class RoomRateUtils {
    public static HashMap<Integer, Double> calculateAverageRate(HashMap<Integer, HashMap<String, Double>> roomTypeRateMap, String startDate, String endDate) {
        HashMap<Integer, Double> averageRateMap = new HashMap<>();

        for (HashMap.Entry<Integer, HashMap<String, Double>> entry : roomTypeRateMap.entrySet()) {
            int roomTypeId = entry.getKey();
            HashMap<String, Double> dateRateMap = entry.getValue();
            double totalRate = 0;
            int count = 0;

            for (HashMap.Entry<String, Double> rateEntry : dateRateMap.entrySet()) {
                String date = rateEntry.getKey();
                double rate = rateEntry.getValue();
                
                // Check if the date is within the specified range
                if (date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0) {
                    totalRate += rate;
                    count++;
                }
            }

            if (count > 0) {
                averageRateMap.put(roomTypeId, totalRate / count);
            }
        }

        return averageRateMap;
    }
}
