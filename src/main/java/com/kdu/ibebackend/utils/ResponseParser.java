package com.kdu.ibebackend.utils;

import com.fasterxml.jackson.databind.JsonNode;
 import com.fasterxml.jackson.databind.ObjectMapper;
 import com.fasterxml.jackson.databind.node.ArrayNode;
import com.kdu.ibebackend.models.RoomRate;

import java.util.ArrayList;
 import java.util.List;

/**
 * Response Parser utility
 */
public class ResponseParser {

    /**
     * Function to parse room rates
     * @param jsonResponse Stringified JSON
     * @return List of
     */
     public static List<RoomRate> parseRoomRates(String jsonResponse) {
         List<RoomRate> roomRates = new ArrayList<>();
         try {
             ObjectMapper objectMapper = new ObjectMapper();
             JsonNode rootNode = objectMapper.readTree(jsonResponse);
 
             ArrayNode listRoomRatesNode = (ArrayNode) rootNode;
 
             for (JsonNode roomRateNode : listRoomRatesNode) {
                 String date = roomRateNode.path("date").asText();
                 double basicNightlyRate = roomRateNode.path("basic_nightly_rate").asDouble();
                 int roomRateId = roomRateNode.path("room_rate_id").asInt();
 
                 RoomRate roomRate = new RoomRate(date, basicNightlyRate, roomRateId);
                 roomRates.add(roomRate);
             }
         } catch (Exception e) {
             e.printStackTrace();
         }
 
         return roomRates;
     }
 }