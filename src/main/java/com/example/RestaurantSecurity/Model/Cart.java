package com.example.RestaurantSecurity.Model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@Entity
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name="customer_id")
    private User user;

    @ElementCollection
    private Map<FoodItem, Integer> foodItems = new HashMap<>();
    private double totalAmount;

    private boolean openCart;

}
