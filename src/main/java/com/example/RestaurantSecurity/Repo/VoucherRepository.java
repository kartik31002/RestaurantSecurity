package com.example.RestaurantSecurity.Repo;

import com.example.RestaurantSecurity.Model.Voucher;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoucherRepository extends JpaRepository<Voucher, Integer> {
}
