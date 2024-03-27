package com.kdu.ibebackend.dto.graphql;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.kdu.ibebackend.dto.response.RoomRateWrapper;
import lombok.Data;

import java.util.List;

@Data
public class ListRoomRateRoomTypeMappings {
    @JsonProperty("data")
    public Res res;

    @Data
    public static class Res {
        @JsonProperty("listRoomRateRoomTypeMappings")
        public List<RoomRateWrapper> roomRates;
    }
}
