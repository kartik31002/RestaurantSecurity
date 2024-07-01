INSERT INTO food_item (name, price, category, availability, veg, frequency) VALUES
('Pizza Margherita', 11.00, 'MainCourse', true, true, 0),
('BBQ Chicken Pizza', 13.00, 'MainCourse', true, false, 0),
('Caesar Salad', 8.50, 'Appetizer', true, true, 0),
('Greek Salad', 9.00, 'Appetizer', true, true, 0),
('Tomato Soup', 6.00, 'Soup', true, true, 0),
('Chicken Soup', 7.50, 'Soup', true, false, 0),
('Beef Burger', 11.99, 'MainCourse', true, false, 0),
('Veggie Burger', 10.50, 'MainCourse', true, true, 0),
('Grilled Chicken Sandwich',10.00, 'MainCourse', true, false, 0),
('BLT Sandwich', 9.00, 'MainCourse', true, false, 0),
('Spaghetti Carbonara', 12.00, 'MainCourse', true, false, 0),
('Penne Arrabbiata', 11.00, 'MainCourse', true, true, 0),
('Mushroom Risotto', 13.50, 'MainCourse', false, true, 0),
('Seafood Paella', 15.00, 'MainCourse', false, false, 0),
('Fish Tacos', 10.50, 'MainCourse', true, false, 0),
('Vegetable Spring Rolls', 7.00, 'Appetizer', true, true, 0),
('Chicken Wings', 9.00, 'Appetizer', true, false, 0),
('Garlic Bread', 4.50, 'Appetizer', true, true, 0),
('Cheesecake', 6.50, 'Dessert', true, true, 0),
('Chocolate Cake', 6.50, 'Dessert', true, true, 0),
('Ice Cream Sundae', 5.00, 'Dessert', true, true, 0),
('Apple Pie', 5.50, 'Dessert', true, true, 0),
('Pancakes', 7.50, 'Dessert', false, true, 0),
('French Fries', 3.50, 'Appetizer', true, true, 0),
('Mozzarella Sticks', 5.50, 'Appetizer', true, true, 0),
('Chicken Caesar Wrap', 8.50, 'MainCourse', true, false, 0),
('Turkey Club Sandwich', 9.50, 'MainCourse', true, false, 0),
('Beef Tacos', 10.00, 'MainCourse', false, false, 0),
('Veggie Tacos', 9.00, 'MainCourse', false, true, 0),
('Shrimp Scampi', 14.50, 'MainCourse', false, false, 0);
INSERT INTO voucher (discount, discount_limit, least_amount) VALUES (10,20,20), (20,30,20), (30,180,20);