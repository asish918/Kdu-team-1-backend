package com.kdu.ibebackend.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kdu.ibebackend.constants.PromoType;
import com.kdu.ibebackend.dto.request.SearchParamDTO;
import com.kdu.ibebackend.dto.response.RoomResultResponse;
import com.kdu.ibebackend.dto.response.RoomType;
import com.kdu.ibebackend.models.PromotionType;
import com.kdu.ibebackend.models.dynamodb.RoomInfo;
import com.kdu.ibebackend.repository.DynamoRepository;
import com.kdu.ibebackend.service.DynamoDBService;
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
        else return (roomType.getSingleBed() + roomType.getDoubleBed()) == searchParamDTO.getBeds();
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
            if (roomTypeIds.contains(roomType.getRoomTypeId())) {
                filteredRoomTypes.add(roomType);
            }
        }
        return filteredRoomTypes;
    }

    public static List<RoomResultResponse> filterRoomTypeBeds(List<String> bedTypes, List<RoomResultResponse> roomTypes) {
        if (bedTypes.isEmpty()) return roomTypes;

        List<RoomResultResponse> filteredRoomTypes = new ArrayList<>();
        for (RoomResultResponse roomType : roomTypes) {
            if (bedTypes.containsAll(List.of("Single", "Double")) && roomType.getSingleBed() > 0 && roomType.getDoubleBed() > 0) {
                filteredRoomTypes.add(roomType);
            } else if (bedTypes.contains("Single") && bedTypes.size() == 1 && roomType.getSingleBed() > 0 && roomType.getDoubleBed() == 0) {
                filteredRoomTypes.add(roomType);
            } else if (bedTypes.contains("Double") && bedTypes.size() == 1 && roomType.getDoubleBed() > 0 && roomType.getSingleBed() == 0) {
                filteredRoomTypes.add(roomType);
            }
        }

        return filteredRoomTypes;
    }

    public static List<RoomResultResponse> sortPriceType(SearchParamDTO searchParamDTO, List<RoomResultResponse> roomTypes) {
        if (searchParamDTO.getPriceSort()) {
            return roomTypes.stream()
                    .sorted(Comparator.comparing(RoomResultResponse::getAverageRate))
                    .collect(Collectors.toList());
        } else {
            return roomTypes.stream()
                    .sorted(Comparator.comparing(RoomResultResponse::getAverageRate).reversed())
                    .collect(Collectors.toList());
        }
    }

    public static List<RoomResultResponse> sortAreaType(SearchParamDTO searchParamDTO, List<RoomResultResponse> roomTypes) {
        if (searchParamDTO.getAreaSort()) {
            return roomTypes.stream()
                    .sorted(Comparator.comparing(RoomResultResponse::getAreaInSquareFeet))
                    .collect(Collectors.toList());
        } else {
            return roomTypes.stream()
                    .sorted(Comparator.comparing(RoomResultResponse::getAreaInSquareFeet).reversed())
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

    public static PromotionType findMinPromo(List<PromotionType> promotionTypeList, RoomResultResponse roomResultResponse, SearchParamDTO searchParamDTO) {
        double minProduct = Double.MAX_VALUE;
        PromotionType minPromo = null;

        for (PromotionType promo : promotionTypeList) {
            double product = promo.getPriceFactor() * roomResultResponse.getAverageRate();
            if (product < minProduct && promo.getMinimumDaysOfStay() <= DateUtils.calculateDaysBetween(searchParamDTO.getStartDate(), searchParamDTO.getEndDate())) {
                minProduct = product;
                minPromo = promo;
            }
        }

        return minPromo;
    }

    public static List<PromotionType> findValidPromos(List<PromotionType> promotionTypeList, SearchParamDTO searchParamDTO) {
        List<PromotionType> validPromos = new ArrayList<PromotionType>();

        for (PromotionType promo : promotionTypeList) {
            if(PromoUtils.checkKDUMember(promo, searchParamDTO)) {
                log.info("KDU Member");
                validPromos.add(promo);
            }
            else if(PromoUtils.checkMilitaryPersonal(promo, searchParamDTO)) {
                log.info("Military");
                validPromos.add(promo);
            }
            else if(PromoUtils.checkSeniorCitizen(promo, searchParamDTO)) {
                log.info("Senior");
                validPromos.add(promo);
            }
            else if(PromoUtils.checkLongWeekend(promo, searchParamDTO)) {
                log.info("Long Weekend");
                validPromos.add(promo);
            }
            else if(PromoUtils.checkWeekend(promo, searchParamDTO)) {
                log.info("Weekend");
                validPromos.add(promo);
            }
            else if (PromoType.UPFRONT.getPromotionId() == Integer.parseInt(promo.getPromotionId())) {
                validPromos.add(promo);
            }
        }

        log.info(validPromos.toString());
        return validPromos;
    }

    /**
     * Caching room info to prevent repeated unnecessary calls to DynamoDB
     * @param dynamoDBService
     * @return
     * @throws JsonProcessingException
     */
    @Cacheable("roominfo")
    public static List<RoomInfo> findRoomInfo(DynamoDBService dynamoDBService) throws JsonProcessingException {
        List<RoomInfo> roomInfos = new ArrayList<>();

        for (int i = 1; i <= 6; i++) {
            RoomInfo roomInfo = dynamoDBService.fetchRoomInfo(i);
            roomInfos.add(roomInfo);
        }

        return roomInfos;
    }


    @CacheEvict("roominfo")
    @Scheduled(fixedRate = 86400000)
    public void evictDataCache() {
    }
}
