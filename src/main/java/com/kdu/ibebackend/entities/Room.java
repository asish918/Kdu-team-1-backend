package com.kdu.ibebackend.entities;

//import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity(name="room")
@NoArgsConstructor @Getter @Setter
public class Room {
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @Column(nullable = false)
    private Integer room_id;

    private String name;

//    @OneToOne(cascade = CascadeType.MERGE)
//    @JoinColumn(name = "room_id", referencedColumnName = "tenant_id")
    private Tenant tenant;
}