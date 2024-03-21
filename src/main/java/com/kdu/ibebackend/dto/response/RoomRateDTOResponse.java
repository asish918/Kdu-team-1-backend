package com.kdu.ibebackend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class RoomRateDTOResponse {
    @JsonProperty("data")
    public Res res;

    @Data
    public static class Res {
        @JsonProperty("listRoomRateRoomTypeMappings")
        public List<RoomRateWrapper> roomRates;
    }
}
