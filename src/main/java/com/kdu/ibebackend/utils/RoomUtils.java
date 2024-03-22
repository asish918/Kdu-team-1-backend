package com.kdu.ibebackend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kdu.ibebackend.dto.request.SearchParamDTO;
import com.kdu.ibebackend.dto.response.FinalResponse;
import com.kdu.ibebackend.models.PromotionType;
import com.kdu.ibebackend.models.RoomInfo;
import com.kdu.ibebackend.repository.DynamoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class RoomUtils {
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

    public static boolean checkGuestMaxCapacity(Integer totalGuests, Integer rooms, Integer maxCap) {
        Double ceilValue = Math.ceil((double) totalGuests / rooms);
        return maxCap >= ceilValue;
    }

    public static List<FinalResponse> hashMapToList(HashMap<Integer, FinalResponse> finalResponseMap) {
        List<FinalResponse> resultList = new ArrayList<>();
        for (Map.Entry<Integer, FinalResponse> entry : finalResponseMap.entrySet()) {
            resultList.add(entry.getValue());
        }
        return resultList;
    }

    public static List<FinalResponse> filterRoomTypeIds(List<Integer> roomTypeIds, List<FinalResponse> roomTypes) {
        if (roomTypeIds.isEmpty()) return roomTypes;

        List<FinalResponse> filteredRoomTypes = new ArrayList<>();
        for (FinalResponse roomType : roomTypes) {
            if (roomTypeIds.contains(roomType.getRoom_type_id())) {
                filteredRoomTypes.add(roomType);
            }
        }
        return filteredRoomTypes;
    }

    public static List<FinalResponse> filterRoomTypeBeds(List<String> bedTypes, List<FinalResponse> roomTypes) {
        if (bedTypes.isEmpty()) return roomTypes;

        List<FinalResponse> filteredRoomTypes = new ArrayList<>();
        for (FinalResponse roomType : roomTypes) {
            if (bedTypes.containsAll(List.of("Single Bed", "Double Bed")) && roomType.getSingle_bed() > 0 && roomType.getDouble_bed() > 0) {
                filteredRoomTypes.add(roomType);
            } else if (bedTypes.contains("Single Bed") && roomType.getSingle_bed() > 0 && roomType.getDouble_bed() == 0) {
                filteredRoomTypes.add(roomType);
            } else if (bedTypes.contains("Double Bed") && roomType.getDouble_bed() > 0 && roomType.getSingle_bed() == 0) {
                filteredRoomTypes.add(roomType);
            }
        }

        return filteredRoomTypes;
    }

    public static List<FinalResponse> sortPriceType(SearchParamDTO searchParamDTO, List<FinalResponse> roomTypes) {
        if (searchParamDTO.getPriceSort()) {
            return roomTypes.stream()
                    .sorted(Comparator.comparing(FinalResponse::getAverage_rate))
                    .collect(Collectors.toList());
        } else {
            return roomTypes.stream()
                    .sorted(Comparator.comparing(FinalResponse::getAverage_rate).reversed())
                    .collect(Collectors.toList());
        }
    }

    public static List<FinalResponse> sortAreaType(SearchParamDTO searchParamDTO, List<FinalResponse> roomTypes) {
        if (searchParamDTO.getAreaSort()) {
            return roomTypes.stream()
                    .sorted(Comparator.comparing(FinalResponse::getArea_in_square_feet))
                    .collect(Collectors.toList());
        } else {
            return roomTypes.stream()
                    .sorted(Comparator.comparing(FinalResponse::getArea_in_square_feet).reversed())
                    .collect(Collectors.toList());
        }
    }

    public static List<FinalResponse> sortRatingType(SearchParamDTO searchParamDTO, List<FinalResponse> roomTypes) {
        if (searchParamDTO.getRatingSort()) {
            return roomTypes.stream()
                    .sorted(Comparator.comparing(FinalResponse::getRating))
                    .collect(Collectors.toList());
        } else {
            return roomTypes.stream()
                    .sorted(Comparator.comparing(FinalResponse::getRating).reversed())
                    .collect(Collectors.toList());
        }
    }

    public static PromotionType findPromo(List<PromotionType> promotionTypeList, FinalResponse finalResponse) {
        double minProduct = Double.MAX_VALUE;
        PromotionType minPromo = null;

        for (PromotionType promo : promotionTypeList) {
            double product = promo.getPrice_factor() * finalResponse.getAverage_rate();
            if (product < minProduct) {
                minProduct = product;
                minPromo = promo;
            }
        }

        return minPromo;
    }

    @Cacheable("ratings")
    public static List<RoomInfo> findRatings(DynamoRepository dynamoRepository) throws JsonProcessingException {
        List<RoomInfo> roomInfos = new ArrayList<>();

        for (int i = 1; i <= 6; i++) {
            RoomInfo roomInfo = dynamoRepository.getRoomRatingReview(i);
            roomInfos.add(roomInfo);
        }

        return roomInfos;
    }


    @CacheEvict("ratings")
    @Scheduled(fixedRate = 86400000)
    public void evictDataCache() {
    }
}
