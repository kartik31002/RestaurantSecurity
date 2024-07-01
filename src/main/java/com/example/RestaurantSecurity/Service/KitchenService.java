package com.example.RestaurantSecurity.Service;

import com.example.RestaurantSecurity.Repo.FoodItemRepository;
import com.example.RestaurantSecurity.Model.FoodItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class KitchenService {
    @Autowired
    private FoodItemRepository foodItemRepository;

    public FoodItem addFoodItem(FoodItem foodItem){

        //add given food item to the db
        return foodItemRepository.save(foodItem);
    }

    public List<FoodItem> getMenu(String category, boolean veg) {

        //query the database with given category and veg/non-veg option
        return foodItemRepository.findByCategoryAndVegAndAvailability(category, veg, true);
    }

    public ResponseEntity<?> updateFoodItem(Integer foodItemId, FoodItem foodItem){

        //check for valid food item
        Optional<FoodItem> optFoodItem = foodItemRepository.findById(foodItemId);
        if(optFoodItem.isPresent()){

            //change the necessary details of the food item
            FoodItem existingFoodItem = getFoodItem(foodItem, optFoodItem);
            return new ResponseEntity<>(foodItemRepository.save(existingFoodItem),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Food Item Id doesn't exist.",HttpStatus.BAD_REQUEST);
        }
    }

    //method to only change the given information about the food item and set the existing details the same
    private static FoodItem getFoodItem(FoodItem foodItem, Optional<FoodItem> optFoodItem) {
        FoodItem existingFoodItem = optFoodItem.get();
        existingFoodItem.setAvailability(foodItem.isAvailability());
        if(foodItem.getCategory() != null) existingFoodItem.setCategory(foodItem.getCategory());
        if(foodItem.getName() != null) existingFoodItem.setName(foodItem.getName());
        if(foodItem.getPrice() != 0) existingFoodItem.setPrice(foodItem.getPrice());
        existingFoodItem.setVeg(foodItem.isVeg());
        return existingFoodItem;
    }
}
