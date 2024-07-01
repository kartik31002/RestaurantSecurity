package com.example.RestaurantSecurity.Controller;

import com.example.RestaurantSecurity.Model.FoodItem;
import com.example.RestaurantSecurity.Service.KitchenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/kitchen")
public class KitchenController {
    @Autowired
    private KitchenService kitchenService;
    @PostMapping("/addFoodItem")
    public FoodItem addFoodItem(@RequestBody FoodItem foodItem){
        return kitchenService.addFoodItem(foodItem);
    }
    @GetMapping("/getMenu")
    public List<FoodItem> getMenu(@RequestParam String category, @RequestParam boolean veg) {
        return kitchenService.getMenu(category, veg);
    }
    @PutMapping("/updateFoodItem/{foodItemId}")
    public ResponseEntity<?> updateFoodItem(@PathVariable int foodItemId, @RequestBody FoodItem foodItem){
        return kitchenService.updateFoodItem(foodItemId, foodItem);
    }
}
