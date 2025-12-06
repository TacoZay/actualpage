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
                        "   CONCAT(a.streetAddress, ', ', a.city, ', ', a.state, ' ', a.zip) AS full_address" +
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
}