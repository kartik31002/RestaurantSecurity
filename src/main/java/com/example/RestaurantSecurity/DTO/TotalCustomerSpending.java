package com.example.RestaurantSecurity.DTO;

import com.example.RestaurantSecurity.Model.User;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class TotalCustomerSpending {
    private User user;
    private double totalSpent;

}
