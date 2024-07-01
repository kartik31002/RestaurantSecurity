package com.example.RestaurantSecurity.Repo;

import com.example.RestaurantSecurity.Model.Cart;
import com.example.RestaurantSecurity.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Integer> {
    Optional<Cart> findByUserAndOpenCart(User user, boolean openCart);
}
