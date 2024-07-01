package com.example.RestaurantSecurity.Controller;

import com.example.RestaurantSecurity.Service.AnalyticsService;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/analytics")
public class AnalyticsController {
    @Autowired
    private AnalyticsService analyticsService;

    @GetMapping("/popularCustomer")
    public ResponseEntity<?> getMostPopularCustomer() {
        return analyticsService.getMostPopularCustomer();
    }
    @GetMapping("/avgSpend")
    public ResponseEntity<?> avgSpend(){
        return analyticsService.avgSpend();
    }
    @GetMapping("/bestCustomer")
    public ResponseEntity<?> getBestCustomer() {
        return analyticsService.getBestCustomer();
    }
    @GetMapping("/avgSpendBy/{customerId}")
    public ResponseEntity<?> avgSpendBy(@PathVariable Integer customerId){
        return analyticsService.avgSpendBy(customerId);
    }
    @GetMapping("/prevOrders/{customerId}")
    public ResponseEntity<?> prevOrders(@PathVariable Integer customerId){
        return analyticsService.prevOrders(customerId);
    }
    @GetMapping("/popularDish")
    public ResponseEntity<?> getMostPopularDish(@RequestParam boolean veg) {
        return analyticsService.getMostPopularDish(veg);
    }
}
