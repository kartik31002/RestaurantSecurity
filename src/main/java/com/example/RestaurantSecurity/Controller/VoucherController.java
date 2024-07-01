package com.example.RestaurantSecurity.Controller;

import com.example.RestaurantSecurity.Model.Voucher;
import com.example.RestaurantSecurity.Service.VoucherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/voucher")
public class VoucherController {
    @Autowired
    private VoucherService voucherService;
    @PostMapping("/addVoucher")
    public Voucher addVoucher(@RequestBody Voucher voucher){
        return voucherService.addVoucher(voucher);
    }
    @DeleteMapping("/deleteVoucher/{voucherId}")
    public ResponseEntity<?> deleteVoucher(@PathVariable Integer voucherId){
        return voucherService.deleteVoucher(voucherId);
    }
    @GetMapping("/showVouchers")
    public List<Voucher> showVouchers(){
        return voucherService.showVouchers();
    }
    @PutMapping("/updateVoucher/{voucherId}")
    public ResponseEntity<?> updateVoucher(@PathVariable int voucherId, @RequestBody Voucher voucher){
        return voucherService.updateVoucher(voucherId, voucher);
    }
}
