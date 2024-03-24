package com.kdu.ibebackend.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

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
