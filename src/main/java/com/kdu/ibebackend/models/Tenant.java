package com.kdu.ibebackend.models;

import java.util.Arrays;
import java.util.List;

public record Tenant(String id, String firstName, String lastName) {

    private static List<Tenant> tenants = Arrays.asList(
            new Tenant("tenant-1", "Asish", "Mahapatra"),
            new Tenant("tenant-2", "Neer", "Patel")
    );

    public static Tenant getById(String id) {
        return tenants.stream().filter(tenant -> tenant.id().equals(id)).findFirst().orElse(null);
    }
}