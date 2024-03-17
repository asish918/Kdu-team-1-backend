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
}
