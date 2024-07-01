package com.example.RestaurantSecurity.DTO;

import com.example.RestaurantSecurity.Model.FoodItem;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FoodItemResponse {
    private FoodItem foodItem;
    private int quantity;
}
