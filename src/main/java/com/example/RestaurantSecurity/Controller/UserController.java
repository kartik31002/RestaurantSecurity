package com.example.RestaurantSecurity.Controller;

import com.example.RestaurantSecurity.DTO.LoginRequest;
import com.example.RestaurantSecurity.Model.User;
import com.example.RestaurantSecurity.Service.AnalyticsService;
import com.example.RestaurantSecurity.Service.AuthenticationService;
import com.example.RestaurantSecurity.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthenticationService authService;

    @GetMapping("/userDetails")
    public ResponseEntity<?> getUserDetails(@RequestHeader("Authorization") String authHeader){
        return userService.getUserDetails(authHeader);
    }

    @PostMapping("/addUser")
    public ResponseEntity<?> addUser(@RequestBody User user) {
        return userService.addUser(user);
    }

    @PutMapping("/updateUser")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String authHeader, @RequestBody User user){
        return userService.updateUser(authHeader, user);
    }

    @DeleteMapping("/deleteUser")
    public ResponseEntity<?> deleteUser(@RequestHeader("Authorization") String authHeader){
        return userService.deleteUser(authHeader);
    }
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest request){
        return userService.loginUser(request);
    }
    @GetMapping("/prevOrders")
    public ResponseEntity<?> prevOrders(@RequestHeader("Authorization") String authHeader){
        return userService.prevOrders(authHeader);
    }
}
