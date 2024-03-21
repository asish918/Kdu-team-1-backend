package com.kdu.ibebackend.service;

import com.kdu.ibebackend.constants.GraphQLQueries;
import com.kdu.ibebackend.dto.response.*;
import com.kdu.ibebackend.utils.DateUtils;
import com.kdu.ibebackend.utils.RoomRateUtils;
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

    @Autowired
    public RoomResultService(GraphQLService graphQLService) {
        this.graphQLService = graphQLService;
    }

    public HashMap<Integer, FinalResponse> finalResponseMap() {
        List<Integer> validRoomTypeIds = roomTypeCount();
        HashMap<Integer, HashMap<String, Double>> roomRateType = roomRateType();
        HashMap<Integer, Double> roomTypeAverageRate = RoomRateUtils.calculateAverageRate(roomRateType, "2024-03-01T00:00:00.000Z", "2024-03-02T00:00:00.000Z");

        HashMap<Integer, FinalResponse> finalResponseMap = new HashMap<>();
        for (Integer roomTypeId : validRoomTypeIds) {
            RoomType roomType = roomTypeDetails.get(roomTypeId);

            FinalResponse finalResponse = new FinalResponse();

            finalResponse.setArea_in_square_feet(roomType.getArea_in_square_feet());

            finalResponse.setDouble_bed(roomType.getDouble_bed());

            finalResponse.setSingle_bed(roomType.getSingle_bed());

            finalResponse.setMax_capacity(roomType.getMax_capacity());

            finalResponse.setRoom_type_name(roomType.getRoom_type_name());

            finalResponse.setAverage_rate(roomTypeAverageRate.getOrDefault(roomTypeId, 0.0));

            finalResponseMap.put(roomTypeId, finalResponse);
        }

        return finalResponseMap;
    }

    public List<Integer> roomTypeCount() {
        String query = GraphQLQueries.roomRes;
        RoomResultDTOResponse res = graphQLService.executePostRequest(query, RoomResultDTOResponse.class).getBody();

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

            roomTypeDetails.put(room.getRoom().getRoom_type_id(), roomType);
            log.info(roomType.toString());


            roomTypeCountMap.putIfAbsent(roomTypeId, new HashMap<>());
            HashMap<String, Integer> dateCountMap = roomTypeCountMap.get(roomTypeId);
            dateCountMap.put(date, dateCountMap.getOrDefault(date, 0) + 1);
        }

        int difference = DateUtils.calculateDaysBetween("2024-03-01T00:00:00.000Z", "2024-03-02T00:00:00.000Z");

        List<Integer> filteredRoomTypeIds = new ArrayList<>();

        for (Integer roomTypeId : roomTypeCountMap.keySet()) {
            HashMap<String, Integer> dateCountMap = roomTypeCountMap.get(roomTypeId);
            int total = dateCountMap.values().stream().mapToInt(Integer::intValue).sum();
            if (total >= difference) {
                filteredRoomTypeIds.add(roomTypeId);
            }
        }

        return filteredRoomTypeIds;
    }

    public HashMap<Integer, HashMap<String, Double>> roomRateType() {
        String query = GraphQLQueries.roomRateRoomTypeMappings;
        RoomRateDTOResponse res = graphQLService.executePostRequest(query, RoomRateDTOResponse.class).getBody();

        HashMap<Integer, HashMap<String, Double>> roomTypeRateMap = new HashMap<>();

        for (RoomRateWrapper roomTypeWithRate : res.getRes().getRoomRates()) {
            RoomRate roomRate = roomTypeWithRate.getRoomRate();
            int roomTypeId = roomTypeWithRate.getRoomTypeRate().getRoom_type_id();

            String date = roomRate.getDate();
            double basicNightlyRate = roomRate.getBasic_nightly_rate();

            roomTypeRateMap.putIfAbsent(roomTypeId, new HashMap<>());
            HashMap<String, Double> dateRateMap = roomTypeRateMap.get(roomTypeId);
            dateRateMap.put(date, basicNightlyRate);
        }

        return roomTypeRateMap;
    }
}
