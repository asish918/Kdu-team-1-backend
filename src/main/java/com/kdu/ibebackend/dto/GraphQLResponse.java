package com.kdu.ibebackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

/**
 * Sample DTO for response after sending dummy GraphQL request
 */
@Data
public class GraphQLResponse {
    @JsonProperty("data")
    public Res res;

    @Data
    public static class Res {
        @JsonProperty("countRooms")
        public int countRooms;
    }
}
