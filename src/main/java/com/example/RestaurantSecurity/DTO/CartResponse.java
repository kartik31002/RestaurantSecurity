package com.example.RestaurantSecurity.DTO;

import com.example.RestaurantSecurity.Model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class CartResponse {
    private int id;
    private User user;
    private List<FoodItemResponse> foodItemResponse = new ArrayList<>();
    private double totalAmount;

    private boolean openCart;
}
