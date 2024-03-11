package com.kdu.ibebackend.entities;

//import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//@Entity(name="tenant")
@NoArgsConstructor @Getter @Setter
public class Tenant {
//    @Id
//    @GeneratedValue(strategy = GenerationType.SEQUENCE)
//    @Column(nullable = false)
    private Integer tenant_id;

    private String firstName;
    private String lastName;
}