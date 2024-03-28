package com.kdu.ibebackend.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.sql.Date;

@Getter
@Setter
@Entity
@Table(name = "promo_codes")
public class PromoCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "room_type_id", nullable = false)
    private Integer roomTypeId;

    @Column(name = "promo_code", nullable = false)
    private String promoCode;

    @Column(name = "promo_title", nullable = false)
    private String promoTitle;

    @Column(name = "promo_desc", nullable = false)
    public String promoDesc;

    @Column(name = "start_date", nullable = false)
    private Date startDate;

    @Column(name = "end_date", nullable = false)
    private Date endDate;

    @Column(name = "price_factor", nullable = false)
    private Double priceFactor;
}