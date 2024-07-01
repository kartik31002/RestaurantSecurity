package com.example.RestaurantSecurity.Repo;

import com.example.RestaurantSecurity.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByEmail(String email);
    Optional<User> findByMobileNo(String mobileNo);
    @Query("SELECT MAX(f.frequency) FROM User f")
    Integer findMaxFrequency();
    List<User> findByFrequency(Integer frequency);
    @Query("SELECT c.id FROM User c JOIN CustomerOrder co ON c = co.user GROUP BY c.id ORDER BY SUM(co.discountedAmt) DESC LIMIT 1")
    Integer getBestCustomer();
    @Query("SELECT SUM(co.discountedAmt) FROM CustomerOrder co WHERE co.user = :#{#user}")
    Double getTotalAmount(@Param("user") User user);
    @Query("SELECT AVG(co.discountedAmt) FROM CustomerOrder co WHERE co.user = :#{#user}")
    Double avgSpendBy(@Param("user") User user);
}
