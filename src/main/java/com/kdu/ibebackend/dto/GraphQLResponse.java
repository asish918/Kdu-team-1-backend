package com.kdu.ibebackend.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
public class GraphQLResponse {
    @JsonProperty("data")
    public Res res;

    @Data
    public static class Res {
        @JsonProperty("countRooms")
        @Schema(name = "countRooms", example = "1080")
        public int countRooms;
    }
}
