package com.example.RestaurantSecurity.Repo;

import com.example.RestaurantSecurity.Model.User;
import com.example.RestaurantSecurity.Model.CustomerOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OrderRepository extends JpaRepository<CustomerOrder, Integer> {
    @Query("SELECT AVG(c.discountedAmt) FROM CustomerOrder c")
    Double avgSpend();

    @Query("Select co FROM CustomerOrder co WHERE co.user = :#{#user}")
    List<CustomerOrder> prevOrders(@Param("user") User user);
}
