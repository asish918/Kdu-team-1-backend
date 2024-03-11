package com.kdu.ibebackend.utils;

import com.kdu.ibebackend.entities.Room;
import com.kdu.ibebackend.entities.Tenant;
//import com.kdu.ibebackend.repository.RoomRepository;
//import com.kdu.ibebackend.repository.TenantRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class InitRunner implements CommandLineRunner {
    private static final Logger LOG =
            LoggerFactory.getLogger(InitRunner.class);

//    @Autowired
//    private final RoomRepository roomRepository;
//    private final TenantRepository tenantRepository;
//
//    public InitRunner(RoomRepository roomRepository,
//                      TenantRepository tenantRepository) {
//        this.roomRepository = roomRepository;
//        this.tenantRepository = tenantRepository;
//    }

    @Override
    public void run(String...args) throws Exception {
//        Tenant tenant1 = new Tenant();
//        tenant1.setFirstName("Asish");
//        tenant1.setLastName("Mahapatra");
//
//        Tenant tenant2 = new Tenant();
//        tenant2.setFirstName("Neer");
//        tenant2.setLastName("Patel");
//
//        Room room1 = new Room();
//        room1.setName("Luxury");
//        room1.setTenant(tenant1);
//
//        Room room2 = new Room();
//        room2.setName("Deluxe");
//        room2.setTenant(tenant2);
//
//        Room room3 = new Room();
//        room3.setName("Basic");
//        room3.setTenant(tenant1);
//
//        tenantRepository.save(tenant1);
//        tenantRepository.save(tenant2);
//        roomRepository.save(room1);
//        roomRepository.save(room2);
//        roomRepository.save(room3);
        LOG.info("Database is seeded....");
    }
}