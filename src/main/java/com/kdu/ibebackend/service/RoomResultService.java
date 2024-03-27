package com.kdu.ibebackend.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.kdu.ibebackend.constants.GraphQLQueries;
import com.kdu.ibebackend.dto.graphql.ListRoomRateRoomTypeMappings;
import com.kdu.ibebackend.dto.request.SearchParamDTO;
import com.kdu.ibebackend.dto.response.*;
import com.kdu.ibebackend.dto.response.ListRoomAvailabilities;
import com.kdu.ibebackend.models.RoomInfo;
import com.kdu.ibebackend.models.RoomRate;
import com.kdu.ibebackend.repository.DynamoRepository;
import com.kdu.ibebackend.utils.DateUtils;
import com.kdu.ibebackend.utils.GraphUtils;
import com.kdu.ibebackend.utils.ParamUtils;
import com.kdu.ibebackend.utils.RoomUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
public class RoomResultService {
    private GraphQLService graphQLService;
    private HashMap<Integer, RoomType> roomTypeDetails = new HashMap<>();
    private DynamoRepository dynamoRepository;

    @Autowired
    public RoomResultService(GraphQLService graphQLService, DynamoRepository dynamoRepository) {
        this.graphQLService = graphQLService;
        this.dynamoRepository = dynamoRepository;
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

        PromotionData promotionData = graphQLService.executePostRequest(GraphQLQueries.promotionQuery, PromotionData.class).getBody();
        List<RoomInfo> roomInfoList = RoomUtils.findRatings(dynamoRepository);

        HashMap<Integer, RoomResultResponse> finalResponseMap = new HashMap<>();
        for (Integer roomTypeId : validRoomTypeIds) {
            RoomType roomType = roomTypeDetails.get(roomTypeId);

            RoomResultResponse roomResultResponse = new RoomResultResponse();
            roomResultResponse.setArea_in_square_feet(roomType.getArea_in_square_feet());
            roomResultResponse.setDouble_bed(roomType.getDouble_bed());
            roomResultResponse.setSingle_bed(roomType.getSingle_bed());
            roomResultResponse.setMax_capacity(roomType.getMax_capacity());
            roomResultResponse.setRoom_type_name(roomType.getRoom_type_name());
            roomResultResponse.setAverage_rate(roomTypeAverageRate.getOrDefault(roomTypeId, 0.0));
            roomResultResponse.setRoom_type_id(roomType.getRoom_type_id());
            roomResultResponse.setPromotionType(RoomUtils.findPromo(promotionData.getRes().getPromotionTypeList(), roomResultResponse, searchParamDTO));
            roomResultResponse.setReviews(roomInfoList.get(roomTypeId - 1).getRoomReviews());
            roomResultResponse.setRating(roomInfoList.get(roomTypeId - 1).getRoomRating());
            roomResultResponse.setHighResImages(roomInfoList.get(roomTypeId - 1).getHighResImages());
            roomResultResponse.setLowResImages(roomInfoList.get(roomTypeId - 1).getLowResImages());

            finalResponseMap.put(roomTypeId, roomResultResponse);
        }

        return finalResponseMap;
    }

    public List<Integer> roomTypeCount(SearchParamDTO searchParamDTO) {
        String query = GraphQLQueries.roomRes;
        String injectedQuery = GraphUtils.injectSearchParamsQuery(query, searchParamDTO.getStartDate(), searchParamDTO.getEndDate(), String.valueOf(searchParamDTO.getPropertyId()));
        log.info(injectedQuery);
        ListRoomAvailabilities res = graphQLService.executePostRequest(injectedQuery, ListRoomAvailabilities.class).getBody();

        HashMap<Integer, HashMap<String, Integer>> roomTypeCountMap = new HashMap<>();

        for (RoomData room : res.getRes().getRoomData()) {
            int roomTypeId = room.getRoom().getRoom_type_id();
            String date = room.getDate();

            RoomType roomType = new RoomType();
            roomType.setRoom_type_name(room.getRoom().getRoomType().getRoom_type_name());
            roomType.setDouble_bed(room.getRoom().getRoomType().getDouble_bed());
            roomType.setMax_capacity(room.getRoom().getRoomType().getMax_capacity());
            roomType.setSingle_bed(room.getRoom().getRoomType().getSingle_bed());
            roomType.setProperty_id(room.getRoom().getRoomType().getProperty_id());
            roomType.setArea_in_square_feet(room.getRoom().getRoomType().getArea_in_square_feet());
            roomType.setRoom_type_name(room.getRoom().getRoomType().getRoom_type_name());
            roomType.setRoom_type_id(room.getRoom().getRoom_type_id());

            roomTypeDetails.put(room.getRoom().getRoom_type_id(), roomType);

            if (RoomUtils.checkBeds(roomType, searchParamDTO) && RoomUtils.checkGuestMaxCapacity(searchParamDTO.getTotalGuests(), searchParamDTO.getRooms(), roomType.getMax_capacity())) {
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
        log.info(injectedQuery);
        ListRoomRateRoomTypeMappings res = graphQLService.executePostRequest(injectedQuery, ListRoomRateRoomTypeMappings.class).getBody();

        HashMap<Integer, HashMap<String, Double>> roomTypeRateMap = new HashMap<>();

        assert res != null;
        for (RoomRateWrapper roomTypeWithRate : res.getRes().getRoomRates()) {
            RoomRate roomRate = roomTypeWithRate.getRoomRate();
            int roomTypeId = roomTypeWithRate.getRoomType().getRoom_type_id();

            String date = roomRate.getDate();
            double basicNightlyRate = roomRate.getBasic_nightly_rate();

            roomTypeRateMap.putIfAbsent(roomTypeId, new HashMap<>());
            HashMap<String, Double> dateRateMap = roomTypeRateMap.get(roomTypeId);
            dateRateMap.put(date, basicNightlyRate);
        }

        return roomTypeRateMap;
    }
}
