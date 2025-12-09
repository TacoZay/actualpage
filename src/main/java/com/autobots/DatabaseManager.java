package com.autobots;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {

    //Database Connection Info
    private static final String URL = "jdbc:mysql://localhost:3306/pizza_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    public Customer getCustomer(String phoneNumber){
        String query  = "SELECT " + 
                        "   c.name, " + 
                        "   c.phoneNumber, " + 
                        "   CONCAT(a.streetAddress, ', ', a.city, ', ', a.state, ' ', a.zip) AS full_address " +
                        "FROM Customer c " +
                        "LEFT JOIN Address a ON c.customer_id = a.customer_id " + 
                        "WHERE c.phoneNumber = ?";

        try (Connection conn = getConnection();
            PreparedStatement pstmt = conn.prepareStatement(query)){

                pstmt.setString(1, phoneNumber);
                ResultSet rs = pstmt.executeQuery();

                if(rs.next()){
                    String name = rs.getString("name");
                    String phone = rs.getString("phoneNumber");
                    String address = rs.getString("full_address");

                    if(address == null){
                        address = "No address on file";
                    }

                    return new Customer(name, phone, address);
                }
            } catch(SQLException e){
                e.printStackTrace();
                System.out.println("Error fetching customer: " + e.getMessage());
            }
            
            return null;
    }
    
    /*Note: This will assume the address is formatted as "Street, City, State Zip" */
    public boolean updateCustomer(Customer customer, String originalPhone){

        //1. update the name & Phone number
        String updateCustomerSQL = "Update Customer SET name = ?, phoneNumber = ? WHERE phoneNumber = ?";

        //2. Update the address( the string needs to split)
        String updateAddressSQL = "UPDATE Address SET streetAddress = ?, city = ?, state = ?, zip = ? "+
                                    "WHERE customer_id = (SELECT customer_id FROM Customer WHERE phoneNumber = ?)";

        try(Connection conn = getConnection()){

            // --- Update the Customer Table
            try(PreparedStatement pstmt1 = conn.prepareStatement(updateCustomerSQL)){
                pstmt1.setString(1, customer.nameProperty().get());
                pstmt1.setString(2, customer.phoneProperty().get());
                pstmt1.setString(3, originalPhone);
                pstmt1.executeUpdate();
            }

            // --- This will execute the Address update
            String rawAddress = customer.addressProperty().get();
            String[] parts = rawAddress.split(",");                 //splits the address by commas

            if (parts.length >= 2) {
                String street = parts[0].trim();
                String city = parts[1].trim();

                //State and Zip are sometimes stuck together
                String stateZip = (parts.length > 2) ? parts[2].trim() : "";

                String state = stateZip.split(" ")[0];
                String zip = stateZip.contains(" ") ? stateZip.substring(stateZip.indexOf(" ") + 1) : "";
                
                try(PreparedStatement pstmt2 = conn.prepareStatement(updateAddressSQL)){
                    pstmt2.setString(1, street);
                    pstmt2.setString(2, city);
                    pstmt2.setString(3, state);
                    pstmt2.setString(4, zip);
                    pstmt2.setString(5, customer.phoneProperty().get());
                    pstmt2.executeUpdate();
                }
            }

            System.out.println("Customer updated successfully");
            return true;

        }catch(SQLException e){
            e.printStackTrace();
            System.out.println("Error updating customer: " +e.getMessage());
            return false;
        }
    }
}