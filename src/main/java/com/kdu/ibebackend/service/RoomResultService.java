package com.kdu.ibebackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kdu.ibebackend.constants.Errors;
import com.kdu.ibebackend.constants.GraphQLQueries;
import com.kdu.ibebackend.dto.graphql.ListRoomAvailabilities;
import com.kdu.ibebackend.dto.graphql.ListRoomRateRoomTypeMappings;
import com.kdu.ibebackend.dto.request.SearchParamDTO;
import com.kdu.ibebackend.dto.response.*;
import com.kdu.ibebackend.models.PromotionType;
import com.kdu.ibebackend.models.dynamodb.RoomInfo;
import com.kdu.ibebackend.models.RoomRate;
import com.kdu.ibebackend.utils.DateUtils;
import com.kdu.ibebackend.utils.GraphUtils;
import com.kdu.ibebackend.utils.ParamUtils;
import com.kdu.ibebackend.utils.RoomUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Service to fetch data for the room details page
 */
@Service
@Slf4j
public class RoomResultService {
    private GraphQLService graphQLService;
    private DynamoDBService dynamoDBService;
    private PromotionService promotionService;

    private HashMap<Integer, RoomType> roomTypeDetails = new HashMap<>();

    @Autowired
    public RoomResultService(GraphQLService graphQLService, DynamoDBService dynamoDBService, PromotionService promotionService) {
        this.graphQLService = graphQLService;
        this.dynamoDBService = dynamoDBService;
        this.promotionService = promotionService;
    }

    public ResponseEntity<Object> paginatedData(SearchParamDTO searchParamDTO, int page, int size) {
        try {
            HashMap<Integer, RoomResultResponse> resMap = finalResponseMap(searchParamDTO);
            List<RoomResultResponse> resList = filteredData(searchParamDTO, RoomUtils.hashMapToList(resMap));

            if (resList.isEmpty()) {
                return new ResponseEntity<>(new SearchResponseDTO(new ArrayList<>(), 0, 0), HttpStatus.OK);
            }

            int totalItems = resMap.size();
            int totalPages = (int) Math.ceil((double) totalItems / size);

            int startIndex = page * size;
            int endIndex = Math.min(startIndex + size, totalItems);

            if (resList.size() == 1) {
                return new ResponseEntity<>(new SearchResponseDTO(resList, 1, page), HttpStatus.OK);
            }

            List<RoomResultResponse> pageResults = resList.subList(startIndex, endIndex);
            return new ResponseEntity<>(new SearchResponseDTO(pageResults, totalPages, page), HttpStatus.OK);
        } catch (JsonProcessingException e) {
            return new ResponseEntity<>(Errors.ROOM_RESULTS, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public List<RoomResultResponse> filteredData(SearchParamDTO searchParamDTO, List<RoomResultResponse> roomRes) {
        List<RoomResultResponse> filteredRoomRes = roomRes;

        if (ParamUtils.containsFilters(searchParamDTO)) {
            filteredRoomRes = RoomUtils.filterRoomTypeIds(searchParamDTO.getRoomTypes(), roomRes);
            filteredRoomRes = RoomUtils.filterRoomTypeBeds(searchParamDTO.getBedTypes(), filteredRoomRes);
        }

        if (ParamUtils.containsSort(searchParamDTO)) {
            if (searchParamDTO.getPriceSort() != null)
                filteredRoomRes = RoomUtils.sortPriceType(searchParamDTO, filteredRoomRes);
            if (searchParamDTO.getAreaSort() != null)
                filteredRoomRes = RoomUtils.sortAreaType(searchParamDTO, filteredRoomRes);
            if (searchParamDTO.getRatingSort() != null)
                filteredRoomRes = RoomUtils.sortRatingType(searchParamDTO, filteredRoomRes);
        }

        return filteredRoomRes;
    }

    public HashMap<Integer, RoomResultResponse> finalResponseMap(SearchParamDTO searchParamDTO) throws JsonProcessingException {
        List<Integer> validRoomTypeIds = roomTypeCount(searchParamDTO);
        HashMap<Integer, HashMap<String, Double>> roomRateType = roomRateType(searchParamDTO);
        HashMap<Integer, Double> roomTypeAverageRate = RoomUtils.calculateAverageRate(roomRateType, searchParamDTO.getStartDate(), searchParamDTO.getEndDate());

        List<PromotionType> promotions = promotionService.fetchPromotions();
        List<RoomInfo> roomInfoList = RoomUtils.findRoomInfo(dynamoDBService);

        HashMap<Integer, RoomResultResponse> finalResponseMap = new HashMap<>();
        for (Integer roomTypeId : validRoomTypeIds) {
            RoomType roomType = roomTypeDetails.get(roomTypeId);

            RoomResultResponse roomResultResponse = new RoomResultResponse();
            roomResultResponse.setAreaInSquareFeet(roomType.getAreaInSquareFeet());
            roomResultResponse.setDoubleBed(roomType.getDoubleBed());
            roomResultResponse.setSingleBed(roomType.getSingleBed());
            roomResultResponse.setMaxCapacity(roomType.getMaxCapacity());
            roomResultResponse.setRoomTypeName(roomType.getRoomTypeName());
            roomResultResponse.setAverageRate(roomTypeAverageRate.getOrDefault(roomTypeId, 0.0));
            roomResultResponse.setRoomTypeId(roomType.getRoomTypeId());
            roomResultResponse.setBestPromotion(RoomUtils.findMinPromo(promotions, roomResultResponse, searchParamDTO));
            roomResultResponse.setReviews(new ArrayList<>());
            roomResultResponse.setRating(roomInfoList.get(roomTypeId - 1).getRoomRating());
            roomResultResponse.setHighResImages(roomInfoList.get(roomTypeId - 1).getHighResImages());
            roomResultResponse.setLowResImages(roomInfoList.get(roomTypeId - 1).getLowResImages());
            roomResultResponse.setValidPromotions(RoomUtils.findValidPromos(promotions, searchParamDTO));
            roomResultResponse.setAmenities(roomInfoList.get(roomTypeId - 1).getAmenities());

            finalResponseMap.put(roomTypeId, roomResultResponse);
        }

        return finalResponseMap;
    }

    public List<Integer> roomTypeCount(SearchParamDTO searchParamDTO) {
        String query = GraphQLQueries.roomRes;
        String injectedQuery = GraphUtils.injectSearchParamsQuery(query, searchParamDTO.getStartDate(), searchParamDTO.getEndDate(), String.valueOf(searchParamDTO.getPropertyId()));
        ListRoomAvailabilities res = graphQLService.executePostRequest(injectedQuery, ListRoomAvailabilities.class).getBody();

        HashMap<Integer, HashMap<String, Integer>> roomTypeCountMap = new HashMap<>();

        for (RoomData room : res.getRes().getRoomData()) {
            int roomTypeId = room.getRoom().getRoomTypeId();
            String date = room.getDate();

            RoomType roomType = new RoomType();
            roomType.setRoomTypeName(room.getRoom().getRoomType().getRoomTypeName());
            roomType.setDoubleBed(room.getRoom().getRoomType().getDoubleBed());
            roomType.setMaxCapacity(room.getRoom().getRoomType().getMaxCapacity());
            roomType.setSingleBed(room.getRoom().getRoomType().getSingleBed());
            roomType.setPropertyId(room.getRoom().getRoomType().getPropertyId());
            roomType.setAreaInSquareFeet(room.getRoom().getRoomType().getAreaInSquareFeet());
            roomType.setRoomTypeName(room.getRoom().getRoomType().getRoomTypeName());
            roomType.setRoomTypeId(roomTypeId);

            roomTypeDetails.put(room.getRoom().getRoomTypeId(), roomType);

            if (RoomUtils.checkBeds(roomType, searchParamDTO) && RoomUtils.checkGuestMaxCapacity(searchParamDTO.getTotalGuests(), searchParamDTO.getRooms(), roomType.getMaxCapacity())) {
                roomTypeCountMap.putIfAbsent(roomTypeId, new HashMap<>());
                HashMap<String, Integer> dateCountMap = roomTypeCountMap.get(roomTypeId);
                dateCountMap.put(date, dateCountMap.getOrDefault(date, 0) + 1);
            }
        }

        int difference = DateUtils.calculateDaysBetween(searchParamDTO.getStartDate(), searchParamDTO.getEndDate());

        List<Integer> filteredRoomTypeIds = new ArrayList<>();

        for (Integer roomTypeId : roomTypeCountMap.keySet()) {
            HashMap<String, Integer> dateCountMap = roomTypeCountMap.get(roomTypeId);
            int total = dateCountMap.values().stream().mapToInt(Integer::intValue).sum();
            if (total >= difference && total >= searchParamDTO.getRooms()) {
                filteredRoomTypeIds.add(roomTypeId);
            }
        }

        return filteredRoomTypeIds;
    }

    public HashMap<Integer, HashMap<String, Double>> roomRateType(SearchParamDTO searchParamDTO) {
        String query = GraphQLQueries.roomRateRoomTypeMappings;
        String injectedQuery = GraphUtils.injectSearchParamsQuery(query, searchParamDTO.getStartDate(), searchParamDTO.getEndDate(), String.valueOf(searchParamDTO.getPropertyId()));
        ListRoomRateRoomTypeMappings res = graphQLService.executePostRequest(injectedQuery, ListRoomRateRoomTypeMappings.class).getBody();

        HashMap<Integer, HashMap<String, Double>> roomTypeRateMap = new HashMap<>();

        assert res != null;
        for (RoomRateWrapper roomTypeWithRate : res.getRes().getRoomRates()) {
            RoomRate roomRate = roomTypeWithRate.getRoomRate();
            int roomTypeId = roomTypeWithRate.getRoomType().getRoomTypeId();

            String date = roomRate.getDate();
            double basicNightlyRate = roomRate.getBasicNightlyRate();

            roomTypeRateMap.putIfAbsent(roomTypeId, new HashMap<>());
            HashMap<String, Double> dateRateMap = roomTypeRateMap.get(roomTypeId);
            dateRateMap.put(date, basicNightlyRate);
        }

        return roomTypeRateMap;
    }
}
