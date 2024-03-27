package com.kdu.ibebackend.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.kdu.ibebackend.constants.Errors;
import com.kdu.ibebackend.constants.GraphQLQueries;
import com.kdu.ibebackend.dto.MinRates;
import com.kdu.ibebackend.utils.ResponseParser;
import com.kdu.ibebackend.models.RoomRate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service for dealing with all Property related queries
 */
@Service
public class PropertyService {
    @Autowired
    private GraphQLService graphQLService;

    private static final String QUERY = GraphQLQueries.basicNightlyRates;

    public ResponseEntity<String> executeGraphQLQuery() throws IOException {
        HttpHeaders headers = new HttpHeaders();

        ResponseEntity<String> responseEntity = graphQLService.executePostRequest(QUERY, String.class);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(responseEntity.getBody());
        JsonNode dataNode = rootNode.path("data");
        JsonNode listPropertiesNode = dataNode.path("listProperties");

        List<JsonNode> roomRates = new ArrayList<>();
        for (JsonNode propertyNode : listPropertiesNode) {
            JsonNode roomTypeNode = propertyNode.path("room_type");
            for (JsonNode roomType : roomTypeNode) {
                JsonNode roomRatesNode = roomType.path("room_rates");
                for (JsonNode roomRate : roomRatesNode) {
                    JsonNode rateNode = roomRate.path("room_rate");
                    roomRates.add(rateNode);
                }
            }
        }

        ArrayNode formattedRoomRates = objectMapper.createArrayNode();
        for (JsonNode roomRate : roomRates) {
            ObjectNode formattedRate = objectMapper.createObjectNode();
            formattedRate.put("date", roomRate.path("date").asText());
            formattedRate.put("basic_nightly_rate", roomRate.path("basic_nightly_rate").asInt());
            formattedRate.put("room_rate_id", roomRate.path("room_rate_id").asInt());
            formattedRoomRates.add(formattedRate);
        }

        String formattedRoomRatesJson = objectMapper.writeValueAsString(formattedRoomRates);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new ResponseEntity<>(formattedRoomRatesJson, headers, HttpStatus.OK);
    }

    public Map<LocalDate, Double> getMinimumNightRate() throws IOException {
        ResponseEntity<String> response = executeGraphQLQuery();
        List<RoomRate> roomRates = ResponseParser.parseRoomRates(response.getBody());
        Map<LocalDate, Double> minNightRates = new HashMap<>();
        for (RoomRate rate : roomRates) {
            String dateString = rate.getDate().substring(0, 10);
            LocalDate date = LocalDate.parse(dateString, DateTimeFormatter.ISO_LOCAL_DATE);
            double nightRate = rate.getBasicNightlyRate();
            minNightRates.putIfAbsent(date, nightRate);
            minNightRates.put(date, Math.min(minNightRates.get(date), nightRate));
        }

        return minNightRates;
    }

    public ResponseEntity<Object> getMinimumNightRatesList() {
        try {
            Map<LocalDate, Double> minNightRates = getMinimumNightRate();
            List<MinRates> res = MinRates.convertMapToList(minNightRates);
            MinRates.sortListByDate(res);
            return new ResponseEntity<>(res, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(Errors.MINIMUM_NIGHT_RATES, HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}