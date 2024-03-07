package com.kdu.ibebackend.models;

import java.util.Arrays;
import java.util.List;

public record Room(String id, String name, String tenantId) {

    private static List<Room> rooms = Arrays.asList(
            new Room("room-1", "Deluxe", "tenant-1"),
            new Room("room-2", "King", "tenant-2"),
            new Room("room-3", "Basic", "tenant-1"));

    public static Room getById(String id) {
        return rooms.stream().filter(room -> room.id().equals(id)).findFirst().orElse(null);
    }

}