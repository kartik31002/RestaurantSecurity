package com.example.RestaurantSecurity.Service;

import com.example.RestaurantSecurity.Repo.*;
import com.example.RestaurantSecurity.DTO.*;
import com.example.RestaurantSecurity.Model.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private FoodItemRepository foodItemRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationService authenticationService;

    public ResponseEntity<?> viewCart(String authHeader){
        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwt);
        //check for valid customer
        Optional<User> user = userRepository.findByEmail(userEmail);
        //Check for valid Customer

            List<FoodItemResponse> foodItemResponses = new ArrayList<>();
            //check if an open cart is present in the d
            Optional<Cart> cart = cartRepository.findByUserAndOpenCart(user.get(), true);
            if(cart.isPresent()){
                for(Map.Entry<FoodItem, Integer> set : cart.get().getFoodItems().entrySet()){
                    FoodItemResponse foodItemResponse = new FoodItemResponse();
                    foodItemResponse.setFoodItem(set.getKey());
                    foodItemResponse.setQuantity(set.getValue());
                    foodItemResponses.add(foodItemResponse);
                }
                CartResponse cartResponse = new CartResponse();
                cartResponse.setUser(user.get());
                cartResponse.setOpenCart(true);
                cartResponse.setFoodItemResponse(foodItemResponses);
                cartResponse.setTotalAmount(cart.get().getTotalAmount());
                cartResponse.setId(cart.get().getId());
                return new ResponseEntity<>(cartResponse, HttpStatus.OK);
            }

            //If an open cart is not present then create one
            else {
                Cart newCart = new Cart();
                newCart.setUser(user.get());
                newCart.setOpenCart(true);
                Cart savedCart = cartRepository.save(newCart);
                return new ResponseEntity<>(savedCart, HttpStatus.CREATED);
            }
        }

    public ResponseEntity<?> addToCart(String authHeader, Map<Integer, Integer> foodItemIds){

        //Check for valid Customer
        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwt);
        //check for valid customer
        Optional<User> user = userRepository.findByEmail(userEmail);

            //Get food items from the list of ids provided and check for invalid ids and unavailable items
            ResponseEntity<?> foodItemGet = getFoodItemList(foodItemIds);
            if(foodItemGet.getStatusCode()==HttpStatus.BAD_REQUEST) return foodItemGet;
            Map<FoodItem, Integer> foodItems = (Map<FoodItem, Integer>) foodItemGet.getBody();

//            //Calculate the total price of all the added items
//            double totalAmount = foodItems.stream().mapToDouble(FoodItem::getPrice).sum();
            List<FoodItemResponse> foodItemResponses = new ArrayList<>();
            //check if an open cart is present for the customer
            Optional<Cart> cart = cartRepository.findByUserAndOpenCart(user.get(), true);
            if(cart.isPresent()) {

                //get the existing items in the cart and add the new items to the list
                Map<FoodItem, Integer> existingItems = cart.get().getFoodItems();

                foodItems.forEach((key, value)->
                        existingItems.replace(key, existingItems.get(key), existingItems.get(key)+value)
                        );


                //Add the new amount to the previous total
                double totalAmount = 0;
                for(Map.Entry<FoodItem, Integer> set : existingItems.entrySet()){
                    FoodItemResponse foodItemResponse = new FoodItemResponse();
                    foodItemResponse.setFoodItem(set.getKey());
                    foodItemResponse.setQuantity(set.getValue());
                    foodItemResponses.add(foodItemResponse);
                    totalAmount+=set.getKey().getPrice()*set.getValue();
                }
                cart.get().setFoodItems(existingItems);
                cart.get().setTotalAmount(totalAmount);
                Cart savedCart = cartRepository.save(cart.get());

                CartResponse cartResponse = new CartResponse();
                cartResponse.setUser(user.get());
                cartResponse.setOpenCart(true);
                cartResponse.setFoodItemResponse(foodItemResponses);
                cartResponse.setTotalAmount(totalAmount);
                cartResponse.setId(savedCart.getId());
                return new ResponseEntity<>(cartResponse, HttpStatus.OK);
            }
            else {
                double totalAmount = 0;
                for(Map.Entry<FoodItem, Integer> set : foodItems.entrySet()){
                    totalAmount+=set.getKey().getPrice()*set.getValue();
                    FoodItemResponse foodItemResponse = new FoodItemResponse();
                    foodItemResponse.setFoodItem(set.getKey());
                    foodItemResponse.setQuantity(set.getValue());
                    foodItemResponses.add(foodItemResponse);
                }
                //if open cart is not present then create one
                Cart newCart = new Cart();
                newCart.setUser(user.get());
                newCart.setOpenCart(true);
                newCart.setFoodItems(foodItems);
                newCart.setTotalAmount(totalAmount);
                Cart savedCart = cartRepository.save(newCart);

                CartResponse cartResponse = new CartResponse();
                cartResponse.setUser(user.get());
                cartResponse.setOpenCart(true);
                cartResponse.setFoodItemResponse(foodItemResponses);
                cartResponse.setTotalAmount(totalAmount);
                cartResponse.setId(savedCart.getId());

                return new ResponseEntity<>(cartResponse, HttpStatus.OK);
            }
        }


    //method to verify the food item ids from input and output the list of food items
    public ResponseEntity<?> getFoodItemList(Map<Integer, Integer> foodItemIds){
        Map<FoodItem, Integer> foodItems = new HashMap<>();
        for(Map.Entry<Integer, Integer> set : foodItemIds.entrySet()){
            if(!foodItemRepository.existsById(set.getKey())) return new ResponseEntity<>("Dish does not exist.", HttpStatus.BAD_REQUEST);
            FoodItem foodItem = foodItemRepository.findById(set.getKey()).get();
            if(!foodItem.isAvailability()) return new ResponseEntity<>("Dish not available.", HttpStatus.BAD_REQUEST);
            if(set.getValue() < 1) return new ResponseEntity<>("Invalid Quantity.", HttpStatus.BAD_REQUEST);
            foodItems.put(foodItem, set.getValue());
        }
        return new ResponseEntity<>(foodItems,HttpStatus.OK);
    }
    public ResponseEntity<?> deleteItem(String authHeader, Map<Integer, Integer> foodItemIds){

        //Check for valid Customer
        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwt);
        //check for valid customer
        Optional<User> user = userRepository.findByEmail(userEmail);

            List<FoodItemResponse> foodItemResponses = new ArrayList<>();
            //check for invalid food items
            ResponseEntity<?> foodItemGet = getFoodItemList(foodItemIds);
            if(foodItemGet.getStatusCode()==HttpStatus.BAD_REQUEST) return foodItemGet;
            Map<FoodItem, Integer> foodItems = (Map<FoodItem, Integer>) foodItemGet.getBody();
            Optional<Cart> cart = cartRepository.findByUserAndOpenCart(user.get(), true);

            //check if an open cart is present for the customer
            if(cart.isPresent()) {
                Map<FoodItem, Integer> existingItems = cart.get().getFoodItems();

                //remove the listed food items from the cart and deduct their price from the total amount
                for(Map.Entry<FoodItem, Integer> set : foodItems.entrySet()){
                    existingItems.replace(set.getKey(), existingItems.get(set.getKey()), existingItems.get(set.getKey()) - set.getValue());
                    if(set.getValue() < 0) return new ResponseEntity<>("Invalid Request", HttpStatus.BAD_REQUEST);
                }
                double totalAmount = 0;
                for(Map.Entry<FoodItem, Integer> set : existingItems.entrySet()){
                    FoodItemResponse foodItemResponse = new FoodItemResponse();
                    foodItemResponse.setFoodItem(set.getKey());
                    foodItemResponse.setQuantity(set.getValue());
                    foodItemResponses.add(foodItemResponse);
                    totalAmount+=set.getKey().getPrice()*set.getValue();
                }
                    //if item is not present in the cart then throw bad request

                //save the cart to db
                cart.get().setTotalAmount(totalAmount);
                cart.get().setFoodItems(existingItems);
                Cart savedCart = cartRepository.save(cart.get());

                CartResponse cartResponse = new CartResponse();
                cartResponse.setUser(user.get());
                cartResponse.setOpenCart(true);
                cartResponse.setFoodItemResponse(foodItemResponses);
                cartResponse.setTotalAmount(totalAmount);
                cartResponse.setId(savedCart.getId());

                return new ResponseEntity<>(cartResponse, HttpStatus.OK);
            }

            //if cart is not present then create an empty one
            else {
                Cart newCart = new Cart();
                newCart.setUser(user.get());
                newCart.setOpenCart(true);
                Cart savedCart = cartRepository.save(newCart);
                CartResponse cartResponse = new CartResponse();
                cartResponse.setUser(user.get());
                cartResponse.setOpenCart(true);
                cartResponse.setFoodItemResponse(foodItemResponses);
                cartResponse.setTotalAmount(0);
                cartResponse.setId(savedCart.getId());
                return new ResponseEntity<>(cartResponse, HttpStatus.CREATED);
            }
        }

    public ResponseEntity<?> placeOrder(String authHeader, Integer voucherId){

        //Check for valid Customer
        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwt);
        //check for valid customer
        Optional<User> user = userRepository.findByEmail(userEmail);

            List<FoodItemResponse> foodItemResponses = new ArrayList<>();
            //check for an open cart for the customer
            Optional<Cart> cart = cartRepository.findByUserAndOpenCart(user.get(), true);
            if(cart.isPresent()){

                //check for empty cart
                if(cart.get().getFoodItems().isEmpty()) return new ResponseEntity<>("Cart is Empty.", HttpStatus.BAD_REQUEST);

                //update the frequency for the food items in the cart to be ordered
                Map<FoodItem, Integer> foodItems = cart.get().getFoodItems();
                Map<FoodItem, Integer> savedItems = new HashMap<>();
                for(Map.Entry<FoodItem, Integer> set : foodItems.entrySet()){
                    set.getKey().setFrequency(set.getKey().getFrequency() + set.getValue());
                    FoodItem savedItem = foodItemRepository.save(set.getKey());
                    savedItems.put(savedItem, set.getValue());
                    FoodItemResponse foodItemResponse = new FoodItemResponse();
                    foodItemResponse.setFoodItem(savedItem);
                    foodItemResponse.setQuantity(set.getValue());
                    foodItemResponses.add(foodItemResponse);
                }

                //calculate the discounted amount according to the voucher applied
                double discountedAmt = cart.get().getTotalAmount();

                //check for valid voucher
                Voucher voucher = null;
                if (voucherId != 0) {
                    Optional<Voucher> optVoucher = voucherRepository.findById(voucherId);
                    if (optVoucher.isPresent()){
                        voucher = optVoucher.get();
                        if(voucher.getLeastAmount()>discountedAmt){

                            //check for voucher usage condaition
                            String errorMessage = "Voucher Valid above order of" + (voucher.getLeastAmount());
                            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
                        }

                        //check for voucher discount limit
                        else if(optVoucher.get().getDiscountLimit() < discountedAmt*voucher.getDiscount()/100){
                            discountedAmt-=voucher.getDiscountLimit();

                        }
                        else {
                            discountedAmt *= (1 - (voucher.getDiscount() / 100));
                        }
                    }
                    else{
                        return new ResponseEntity<>("Voucher Invalid", HttpStatus.BAD_REQUEST);
                    }
                }

                //close the cart in the db
                cart.get().setFoodItems(savedItems);
                cart.get().setOpenCart(false);
                Cart savedCart = cartRepository.save(cart.get());

                //update frequency of the customer
                user.get().setFrequency(user.get().getFrequency()+1);
                User savedCustomer = userRepository.save(user.get());

                // Create order
                CustomerOrder order = new CustomerOrder();
                order.setUser(user.get());
                order.setFoodItems(savedItems);
                order.setTotalAmount(cart.get().getTotalAmount());
                order.setVoucher(voucher);
                order.setVoucherApply(voucherId!=0);
                order.setDiscountedAmt(discountedAmt);
                order.setOrderDate(new Date());
                order.setCartId(cart.get().getId());

                // Save order
                CustomerOrder savedOrder = orderRepository.save(order);

                OrderResponse orderResponse = new OrderResponse();
                orderResponse.setUser(user.get());
                orderResponse.setOrderDate(savedOrder.getOrderDate());
                orderResponse.setId(savedOrder.getId());
                orderResponse.setCartId(cart.get().getId());
                orderResponse.setTotalAmount(savedOrder.getTotalAmount());
                orderResponse.setVoucher(voucher);
                orderResponse.setDiscountedAmt(discountedAmt);
                orderResponse.setVoucherApply(savedOrder.isVoucherApply());
                orderResponse.setFoodItemResponses(foodItemResponses);
                return new ResponseEntity<>(orderResponse, HttpStatus.CREATED);
            }
            else {

                //if open cart is not present then create a new one and return empty cart
                Cart newCart = new Cart();
                newCart.setUser(user.get());
                newCart.setOpenCart(true);
                Cart savedCart = cartRepository.save(newCart);
                return new ResponseEntity<>("Cart is Empty.", HttpStatus.BAD_REQUEST);
            }
        }


    public ResponseEntity<?> viewDiscount(String authHeader, Integer voucherId){

        //Check for valid Customer
        String jwt = authHeader.substring(7);
        String userEmail = jwtService.extractUsername(jwt);
        //check for valid customer
        Optional<User> user = userRepository.findByEmail(userEmail);
            List<FoodItemResponse> foodItemResponses = new ArrayList<>();
            //check for an open cart for the customer
            Optional<Cart> cart = cartRepository.findByUserAndOpenCart(user.get(), true);
            if(cart.isPresent()){

                //check for empty cart
                if(cart.get().getFoodItems().isEmpty()) return new ResponseEntity<>("Cart is Empty.", HttpStatus.BAD_REQUEST);
                Map<FoodItem, Integer> foodItems = cart.get().getFoodItems();
                for(Map.Entry<FoodItem, Integer> set : foodItems.entrySet()){
                    FoodItemResponse foodItemResponse = new FoodItemResponse();
                    foodItemResponse.setFoodItem(set.getKey());
                    foodItemResponse.setQuantity(set.getValue());
                    foodItemResponses.add(foodItemResponse);
                }
                //calculate the discounted amount according to the voucher applied
                double discountedAmt = cart.get().getTotalAmount();

                //check for valid voucher
                Voucher voucher = null;
                if (voucherId != 0) {
                    Optional<Voucher> optVoucher = voucherRepository.findById(voucherId);
                    if (optVoucher.isPresent()){
                        voucher = optVoucher.get();
                        if(voucher.getLeastAmount()>discountedAmt){

                            //check for voucher usage condition
                            String errorMessage = "Voucher Valid above order of" + (voucher.getLeastAmount());
                            return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
                        }

                        //check for voucher discount limit
                        else if(optVoucher.get().getDiscountLimit() < discountedAmt*voucher.getDiscount()/100){
                            discountedAmt-=voucher.getDiscountLimit();

                        }
                        else {
                            discountedAmt *= (1 - (voucher.getDiscount() / 100));
                        }
                    }
                    else{
                        return new ResponseEntity<>("Voucher Invalid", HttpStatus.BAD_REQUEST);
                    }
                }

                // Create order
                OrderResponse order = new OrderResponse();
                order.setUser(user.get());
                order.setFoodItemResponses(foodItemResponses);
                order.setTotalAmount(cart.get().getTotalAmount());
                order.setVoucher(voucher);
                order.setVoucherApply(voucherId!=0);
                order.setDiscountedAmt(discountedAmt);
                order.setOrderDate(new Date());
                order.setCartId(cart.get().getId());

                return new ResponseEntity<>(order, HttpStatus.CREATED);
            }
            else {

                //if open cart is not present then create a new one and return empty cart
                Cart newCart = new Cart();
                newCart.setUser(user.get());
                newCart.setOpenCart(true);
                Cart savedCart = cartRepository.save(newCart);
                return new ResponseEntity<>("Cart is Empty.", HttpStatus.BAD_REQUEST);
            }
        }

}
//
//}
