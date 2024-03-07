package com.kdu.ibebackend.controller;

import com.kdu.ibebackend.models.Room;
import com.kdu.ibebackend.models.Tenant;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    public String testHealthEndpoint() {
        return "Hey there!! The server works great 👍";
    }

    @QueryMapping
    public Room roomById(@Argument String id) {
        return Room.getById(id);
    }

    @SchemaMapping
    public Tenant tenant(Room room) {
        return Tenant.getById(room.tenantId());
    }
}
