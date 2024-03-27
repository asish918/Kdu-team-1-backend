package com.kdu.ibebackend.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class GraphUtils {
    /**
     * Converts a GraphQL query string to POST Request body
     * @param query Normal GraphQL string
     * @return POST Request JSON Body
     */
    public static String convertToGraphQLRequest(String query) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode requestBody = objectMapper.createObjectNode();
        requestBody.put("query", query);
        return requestBody.toString();
    }

    /**
     * Injects all search params in the query to fetch data based on the requested params
     * @param query
     * @param startDate
     * @param endDate
     * @param propertyId
     * @return
     */
    public static String injectSearchParamsQuery(String query, String startDate, String endDate, String propertyId) {
        return query.replace("gte: \"2024-03-01T00:00:00.000Z\"", "gte: \"" + startDate + "\"")
                .replace("lte: \"2024-03-02T00:00:00.000Z\"", "lte: \"" + endDate + "\"")
                .replace("property_id: {equals: 1}", "property_id: {equals: " + propertyId + "}");
    }
}
