package com.example.RestaurantSecurity.DTO;

import com.example.RestaurantSecurity.Model.User;
import com.example.RestaurantSecurity.Model.Voucher;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
@Getter
@Setter
public class OrderResponse {
    private int id;


    private User user;


    private List<FoodItemResponse> foodItemResponses = new ArrayList<>();
    private double totalAmount;
    private double discountedAmt;

    private Voucher voucher;
    private boolean voucherApply;
    private Date orderDate;
    private int cartId;
}
