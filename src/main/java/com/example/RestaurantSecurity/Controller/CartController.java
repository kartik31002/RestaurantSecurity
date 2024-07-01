package com.example.RestaurantSecurity.Controller;

import com.example.RestaurantSecurity.Model.Voucher;
import com.example.RestaurantSecurity.Service.CartService;
import com.example.RestaurantSecurity.Service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    private CartService cartService;
    @Autowired
    private VoucherService voucherService;
    @GetMapping("/viewCart")
    public ResponseEntity<?> viewCart(@RequestHeader("Authorization") String authHeader){
        return cartService.viewCart(authHeader);
    }
    @PostMapping("/addToCart")
    public ResponseEntity<?> addToCart(@RequestHeader("Authorization") String authHeader, @RequestBody Map<Integer, Integer> foodItemIds){
        return cartService.addToCart(authHeader, foodItemIds);
    }
    @DeleteMapping("/deleteItem")
    public ResponseEntity<?> deleteItem(@RequestHeader("Authorization") String authHeader, @RequestBody Map<Integer, Integer> foodItemIds){
        return cartService.deleteItem(authHeader, foodItemIds);
    }
    @PostMapping("/placeOrder")
    public ResponseEntity<?> placeOrder(@RequestHeader("Authorization") String authHeader, @RequestParam Integer voucherId){
        return cartService.placeOrder(authHeader, voucherId);
    }
    @GetMapping("/viewDiscount")
    public ResponseEntity<?> viewDiscount(@RequestHeader("Authorization") String authHeader, @RequestParam Integer voucherId){
        return cartService.viewDiscount(authHeader, voucherId);
    }
    @GetMapping("/showVouchers")
    public List<Voucher> showVouchers(){
        return voucherService.showVouchers();
    }
}
