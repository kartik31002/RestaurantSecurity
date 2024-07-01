package com.example.RestaurantSecurity.Service;

import com.example.RestaurantSecurity.Repo.VoucherRepository;
import com.example.RestaurantSecurity.Model.Voucher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VoucherService {
    @Autowired
    private VoucherRepository voucherRepository;
    //add a voucher to db
    public Voucher addVoucher(Voucher voucher){
        return voucherRepository.save(voucher);
    }

    //delete voucher from db
    public ResponseEntity<?> deleteVoucher(Integer voucherId){
        Optional<Voucher> optionalVoucher = voucherRepository.findById(voucherId);
        if(optionalVoucher.isPresent()){
            voucherRepository.delete(optionalVoucher.get());
            return new ResponseEntity<>("Voucher Deleted", HttpStatus.OK);
        }
        else return new ResponseEntity<>("Voucher does not Exist.", HttpStatus.BAD_REQUEST);
    }

    //List all the vouchers present
    public List<Voucher> showVouchers(){
        return voucherRepository.findAll();
    }

    //update full or partial details of a voucher
    public ResponseEntity<?> updateVoucher(Integer voucherId, Voucher voucher){
        Optional<Voucher> optVoucher = voucherRepository.findById(voucherId);
        if(optVoucher.isPresent()){
            Voucher existingVoucher = getVoucher(voucher, optVoucher);
            return new ResponseEntity<>(voucherRepository.save(existingVoucher),HttpStatus.OK);
        }
        else{
            return new ResponseEntity<>("Food Item Id doesn't exist.",HttpStatus.BAD_REQUEST);
        }
    }

    //method to set the unmentioned details same as before
    private static Voucher getVoucher(Voucher voucher, Optional<Voucher> optVoucher) {
        Voucher existingVoucher = optVoucher.get();
        if(voucher.getDiscount() != 0) existingVoucher.setDiscount(voucher.getDiscount());
        if(voucher.getDiscountLimit() != 0) existingVoucher.setDiscountLimit(voucher.getDiscountLimit());
        if(voucher.getLeastAmount() != 0) existingVoucher.setLeastAmount(voucher.getLeastAmount());
        return existingVoucher;
    }
}
