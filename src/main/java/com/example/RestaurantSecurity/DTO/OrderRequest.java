package com.example.RestaurantSecurity.DTO;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class OrderRequest {
    private Map<Integer, Integer> foodItemIds;
    private String mobileNo;
    private String name;
    private int voucherId;
    private boolean voucher;
}
