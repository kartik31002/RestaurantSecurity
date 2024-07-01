package com.example.RestaurantSecurity.Service;

import com.example.RestaurantSecurity.DTO.LoginResponse;
import com.example.RestaurantSecurity.DTO.LoginRequest;
import com.example.RestaurantSecurity.Model.User;
import com.example.RestaurantSecurity.Repo.OrderRepository;
import com.example.RestaurantSecurity.Repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationService authenticationService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private OrderRepository orderRepository;

    public ResponseEntity<?> addUser(User user) {

        //check for already existing customer from mobile number
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            String errorMessage = "Email already exists for user: " + existingUser.get().getName();
            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
        }

        //create customer from the provided details
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User savedUser = userRepository.save(user);
        return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
    }

    public ResponseEntity<?> updateUser(String authHeader, User user){

        //check for valid customer
        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwt);
        //check for valid customer
        Optional<User> optUser = userRepository.findByEmail(userEmail);


            // Update the existing customer with new details
            if(user.getEmail()!=null){
                if(userRepository.findByEmail(user.getEmail()).isEmpty() ^ Objects.equals(optUser.get().getId(), userRepository.findByEmail(user.getEmail()).get().getId())){
                        if(user.getName()!=null) optUser.get().setName(user.getName());
                        if(user.getMobileNo()!=null) optUser.get().setMobileNo(user.getMobileNo());
                        optUser.get().setEmail(user.getEmail());
                        if(user.getPassword()!=null) optUser.get().setPassword(passwordEncoder.encode(user.getPassword()));
                        return new ResponseEntity<>(userRepository.save(optUser.get()), HttpStatus.OK);
                }
                else return new ResponseEntity<>("Email already exists",HttpStatus.BAD_REQUEST);
            }
            else{
                if(user.getName()!=null) optUser.get().setName(user.getName());
                if(user.getMobileNo()!=null) optUser.get().setMobileNo(user.getMobileNo());
                if(user.getPassword()!=null) optUser.get().setPassword(passwordEncoder.encode(user.getPassword()));
                return new ResponseEntity<>(userRepository.save(optUser.get()), HttpStatus.OK);
            }
        }



    public ResponseEntity<?> deleteUser(String authHeader){
        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwt);
        //check for valid customer
        Optional<User> user = userRepository.findByEmail(userEmail);


            //delete customer from db
            userRepository.delete(user.get());
            return new ResponseEntity<>("User deleted.",HttpStatus.OK);
        }



    public ResponseEntity<?> getUserDetails(String authHeader){
        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwt);
        //check for valid customer
        Optional<User> user = userRepository.findByEmail(userEmail);

            return new ResponseEntity<>(user, HttpStatus.OK);


    }

    public ResponseEntity<?> loginUser(LoginRequest request){
        LoginResponse response = authenticationService.authenticate(request);
        if(response.getToken()!=null) return new ResponseEntity<>(response, HttpStatus.OK);
        return new ResponseEntity<>("Invalid Username or Password", HttpStatus.UNAUTHORIZED);
    }

    public ResponseEntity<?> prevOrders(String authHeader){
        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwt);
        //check for valid customer
        Optional<User> user = userRepository.findByEmail(userEmail);
            //return all the orders form the customer
            return new ResponseEntity<>(orderRepository.prevOrders(user.get()), HttpStatus.OK);
    }
}
