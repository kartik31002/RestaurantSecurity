package com.example.RestaurantSecurity.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@Entity
public class CustomerOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private User user;

    @ElementCollection
    private Map<FoodItem, Integer> foodItems = new HashMap<>();
    private double totalAmount;
    private double discountedAmt;
    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private Voucher voucher;
    private boolean voucherApply;
    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;
    private int cartId;

}
