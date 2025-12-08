** Autobot's Pizza Deliver System**

-- This is a work-in-progress pizza delivery application that will allow users to create accounts, make pizza orders from a menu, and received a itemized bill of the transaction that will be sent over to a database for later records

**Features
- A responsive UI: The layout adapts to different window sizes using a Grid system
- Profile management: view and edit customer details
- Order History: View past orers including the pizza type, date, tip, and total
- Database Integration: Retrieves and updates customer data directly from a MySQL database
- Navigation: Includes a top navigation bar for Menu, Cart, and Logout functionality

  **Prerequisites
  To properly run this project, the following must be installed:
  - Java JDK 11
  - Apache Maven( or VS Code Maven extension)
  - MySQL server(8.0 or higher)
  - Visual Studio Code
 
  **Database Setup
  This application needs MySQL database to work properly
  1. Download & install MySQL Workbench
  2. Open your MySQL Workbench
  3. Run the following SQL script to create the required database and tables:
     **SQL**
     CREATE DATABASE pizza_db;

     -- Create Customer Table
     CREATE TABLE Customer (
      customer_id INT AUTO_INCREMENT PRIMARY KEY,
      name VARCHAR(100),
      phoneNumber VARCHAR(20) UNIQUE
     );

     -- Create Address Table
     CREATE TABLE Address (
      address_id INT AUTO_INCREMENT PRIMARY KEY,
     customer_id INT,
     streetAddress VARCHAR(255),
     city VARCHAR(100),
     state VARCHAR(50),
     zip VARCHAR(20),
     FOREIGN KEY (customer_id) REFERENCES Customer(customer_id)
     );

     -- Insert Dummy Data
     INSERT INTO Customer(name, phoneNumber) VALUES ('Xavier Terry', '578-311-4000');
     INSERT INTO Address(customer_id, streetAddress, city, state, zip)
     VALUES (1, '4321 Street St', 'Marietta', 'GA', '30060');
