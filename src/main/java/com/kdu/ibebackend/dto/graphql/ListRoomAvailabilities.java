package com.kdu.ibebackend.dto.graphql;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kdu.ibebackend.dto.response.RoomData;
import lombok.Data;

import java.util.List;

/**
 * Upper level DTO for ListRoomAvailabilities GraphQL Query
 */
@Data
public class ListRoomAvailabilities {
    @JsonProperty("data")
    public Res res;

    @Data
    public static class Res {
        @JsonProperty("listRoomAvailabilities")
        public List<RoomData> roomData;
    }
}
