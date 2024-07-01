package com.example.RestaurantSecurity.Repo;

import com.example.RestaurantSecurity.Model.FoodItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface FoodItemRepository extends JpaRepository<FoodItem, Integer> {
    List<FoodItem> findByCategoryAndVegAndAvailability(String category, boolean veg, boolean availability);
    @Query("SELECT fi FROM FoodItem fi WHERE fi.veg = :#{#veg} AND fi.frequency = (SELECT MAX(f.frequency) FROM FoodItem f WHERE f.veg= :#{#veg})")
    List<FoodItem> findMaxFrequencyAndVeg(@Param("veg") boolean veg);

    List<FoodItem> findByFrequency(Integer frequency);
}
