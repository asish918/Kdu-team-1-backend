package com.kdu.ibebackend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kdu.ibebackend.dto.request.SearchParamDTO;
import com.kdu.ibebackend.dto.response.RoomResultResponse;
import com.kdu.ibebackend.dto.response.RoomType;
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

    public static boolean checkBeds(RoomType roomType, SearchParamDTO searchParamDTO) {
        if(searchParamDTO.getBeds() == 0) return true;
        else return (roomType.getSingle_bed() + roomType.getDouble_bed()) == searchParamDTO.getBeds();
    }

    public static List<RoomResultResponse> hashMapToList(HashMap<Integer, RoomResultResponse> finalResponseMap) {
        List<RoomResultResponse> resultList = new ArrayList<>();
        for (Map.Entry<Integer, RoomResultResponse> entry : finalResponseMap.entrySet()) {
            resultList.add(entry.getValue());
        }
        return resultList;
    }

    public static List<RoomResultResponse> filterRoomTypeIds(List<Integer> roomTypeIds, List<RoomResultResponse> roomTypes) {
        if (roomTypeIds.isEmpty()) return roomTypes;

        List<RoomResultResponse> filteredRoomTypes = new ArrayList<>();
        for (RoomResultResponse roomType : roomTypes) {
            if (roomTypeIds.contains(roomType.getRoom_type_id())) {
                filteredRoomTypes.add(roomType);
            }
        }
        return filteredRoomTypes;
    }

    public static List<RoomResultResponse> filterRoomTypeBeds(List<String> bedTypes, List<RoomResultResponse> roomTypes) {
        if (bedTypes.isEmpty()) return roomTypes;

        List<RoomResultResponse> filteredRoomTypes = new ArrayList<>();
        for (RoomResultResponse roomType : roomTypes) {
            if (bedTypes.containsAll(List.of("Single", "Double")) && roomType.getSingle_bed() > 0 && roomType.getDouble_bed() > 0) {
                filteredRoomTypes.add(roomType);
            } else if (bedTypes.contains("Single") && bedTypes.size() == 1 && roomType.getSingle_bed() > 0 && roomType.getDouble_bed() == 0) {
                filteredRoomTypes.add(roomType);
            } else if (bedTypes.contains("Double") && bedTypes.size() == 1 && roomType.getDouble_bed() > 0 && roomType.getSingle_bed() == 0) {
                filteredRoomTypes.add(roomType);
            }
        }

        return filteredRoomTypes;
    }

    public static List<RoomResultResponse> sortPriceType(SearchParamDTO searchParamDTO, List<RoomResultResponse> roomTypes) {
        if (searchParamDTO.getPriceSort()) {
            return roomTypes.stream()
                    .sorted(Comparator.comparing(RoomResultResponse::getAverage_rate))
                    .collect(Collectors.toList());
        } else {
            return roomTypes.stream()
                    .sorted(Comparator.comparing(RoomResultResponse::getAverage_rate).reversed())
                    .collect(Collectors.toList());
        }
    }

    public static List<RoomResultResponse> sortAreaType(SearchParamDTO searchParamDTO, List<RoomResultResponse> roomTypes) {
        if (searchParamDTO.getAreaSort()) {
            return roomTypes.stream()
                    .sorted(Comparator.comparing(RoomResultResponse::getArea_in_square_feet))
                    .collect(Collectors.toList());
        } else {
            return roomTypes.stream()
                    .sorted(Comparator.comparing(RoomResultResponse::getArea_in_square_feet).reversed())
                    .collect(Collectors.toList());
        }
    }

    public static List<RoomResultResponse> sortRatingType(SearchParamDTO searchParamDTO, List<RoomResultResponse> roomTypes) {
        if (searchParamDTO.getRatingSort()) {
            return roomTypes.stream()
                    .sorted(Comparator.comparing(RoomResultResponse::getRating))
                    .collect(Collectors.toList());
        } else {
            return roomTypes.stream()
                    .sorted(Comparator.comparing(RoomResultResponse::getRating).reversed())
                    .collect(Collectors.toList());
        }
    }

    public static PromotionType findPromo(List<PromotionType> promotionTypeList, RoomResultResponse roomResultResponse, SearchParamDTO searchParamDTO) {
        double minProduct = Double.MAX_VALUE;
        PromotionType minPromo = null;

        for (PromotionType promo : promotionTypeList) {
            double product = promo.getPrice_factor() * roomResultResponse.getAverage_rate();
            if (product < minProduct && promo.getMinimum_days_of_stay() <= DateUtils.calculateDaysBetween(searchParamDTO.getStartDate(), searchParamDTO.getEndDate())) {
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
