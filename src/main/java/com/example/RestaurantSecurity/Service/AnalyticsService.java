package com.example.RestaurantSecurity.Service;

import com.example.RestaurantSecurity.Repo.UserRepository;
import com.example.RestaurantSecurity.Repo.FoodItemRepository;
import com.example.RestaurantSecurity.Repo.OrderRepository;
import com.example.RestaurantSecurity.DTO.TotalCustomerSpending;
import com.example.RestaurantSecurity.Model.User;
import com.example.RestaurantSecurity.Model.FoodItem;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnalyticsService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private FoodItemRepository foodItemRepository;

    public ResponseEntity<?> getMostPopularCustomer() {
        return new ResponseEntity<>(userRepository.findByFrequency(userRepository.findMaxFrequency()),HttpStatus.ACCEPTED);
    }
    public ResponseEntity<?> avgSpend(){
        return new ResponseEntity<>(orderRepository.avgSpend(),HttpStatus.ACCEPTED);
    }
    public ResponseEntity<?> getBestCustomer() {
        //Query the db for the customer with the highest sum of amount among all orders
        User customer =  userRepository.findById(userRepository.getBestCustomer()).get();
        double total_spent = userRepository.getTotalAmount(customer);

        //Make an instance of total customer spending class which contains a customer and his total spending among all visits.
        TotalCustomerSpending totalCustomerSpending = new TotalCustomerSpending();
        totalCustomerSpending.setUser(customer);
        totalCustomerSpending.setTotalSpent(total_spent);
        return new ResponseEntity<>(totalCustomerSpending,HttpStatus.OK);

    }

    public ResponseEntity<?> avgSpendBy(Integer customerId){

        Optional<User> optionalCustomer = userRepository.findById(customerId);

        //Check for valid customer
        if (optionalCustomer.isPresent()) {

            //query the db for avg spend by this customer
            return new ResponseEntity<>(userRepository.avgSpendBy(optionalCustomer.get()), HttpStatus.ACCEPTED);
        } else {
            return new ResponseEntity<>("Customer does not exist.", HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> prevOrders(Integer customerId){
            Optional<User> optionalCustomer = userRepository.findById(customerId);

            //Check for valid customer
            if (optionalCustomer.isPresent()) {

                //return all the orders form the customer
                return new ResponseEntity<>(orderRepository.prevOrders(optionalCustomer.get()), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Customer does not exist.", HttpStatus.BAD_REQUEST);
            }

    }

    public ResponseEntity<?> getMostPopularDish(boolean veg) {

            //return the food item with the highest frequency in the db
            List<FoodItem> FoodItem = foodItemRepository.findMaxFrequencyAndVeg(veg);
            return new ResponseEntity<>(FoodItem, HttpStatus.OK);
    }

}
