package com.autobots.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.autobots.model.Customer;
import com.autobots.model.PaymentCard;

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

    public boolean createCustomer(String name, String phone, String rawAddress){
        String insertCustomer = "INSERT INTO Customer (name, phoneNumber) VALUES (?, ?)";
        String insertAddress = "INSERT INTO Address (customer_id, streetAddress, city, state, zip) VALUES(?, ?, ?, ? ?)";

        Connection conn = null;
        PreparedStatement pstmt1 = null;
        PreparedStatement pstmt2 = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            conn.setAutoCommit(false);

            //Inserts customer
            pstmt1 = conn.prepareStatement(insertCustomer, java.sql.Statement.RETURN_GENERATED_KEYS);
            pstmt1.setString(1, name);
            pstmt1.setString(2, phone);
            pstmt1.executeUpdate();

            //Generates ID
            rs = pstmt1.getGeneratedKeys();
            int customerId = 0;
            if(rs.next()){
                customerId = rs.getInt(1);
            }


            //Parse and insert the address
            String[] parts = rawAddress.split(",");
            String street = (parts.length > 0) ? parts[0].trim() : rawAddress;
            String city = (parts.length > 1 ) ? parts[1].trim() : "";
            String state = "";
            String zip = "";

            if (parts.length > 2){
                String stateZip = parts[2].trim();
                String[] szParts = stateZip.split(" ");
                if(szParts.length > 0) state = szParts[0];
                if(szParts.length > 1) zip = szParts[1];
            }

            pstmt2 = conn.prepareStatement(insertAddress);
            pstmt2.setInt(1, customerId);
            pstmt2.setString(2, street);
            pstmt2.setString(3, city);
            pstmt2.setString(4, state);
            pstmt2.setString(5, zip);
            pstmt2.executeUpdate();

            conn.commit();
            return true;

            

        } catch(SQLException e){
            e.printStackTrace();
            if(conn != null){
                try{ conn.rollback(); } catch (SQLException ex){ex.printStackTrace();}
            }
            return false;
        } finally {
            try {if(rs != null) rs.close(); if(pstmt1 != null) pstmt2.close(); if(pstmt2 != null) pstmt2.close(); if(conn != null) conn.close();} catch(SQLException e){}
        }
    }

    //Method to Login
    public Customer validateUser(String phone, String password){
        String sql = "SELECT * FROM Customer WHERE phoneNumber = ? AND password = ?";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
                pstmt.setString(1, phone);
                pstmt.setString(2, password);
                ResultSet rs = pstmt.executeQuery();

                if(rs.next()){
                    return new Customer(rs.getString("name"), rs.getString("phoneNumber"), "");
                }
             } catch (SQLException e ){
                e.printStackTrace();
             }
             return null;       //Login failed
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

    //--- Payment method features

    public java.util.List<PaymentCard> getPaymentMethods(String phoneNumber){
        java.util.List<PaymentCard> cards = new java.util.ArrayList<>();
        
        //Select ALL columns now
        String sql = "SELECT cardNumber, cardHolder, expiration, cvv FROM PaymentMethods " +
                        "WHERE customer_id = (SELECT customer_id FROM Customer WHERE phoneNumber = ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, phoneNumber);
            ResultSet rs = pstmt.executeQuery();

            while(rs.next()) {
                cards.add(new PaymentCard(
                    rs.getString("cardNumber"), 
                    rs.getString("cardHolder"),
                    rs.getString("expiration"),
                    rs.getString("cvv")));
            }
            }catch(SQLException e){
                e.printStackTrace();
            }
            return cards;
    }

    public String addPaymentMethod(String phoneNumber, String newCard, String holder, String exp, String cvv){
        String countSql = "SELECT COUNT(*) FROM PaymentMethods " +
                            "WHERE customer_id = (SELECT customer_id FROM Customer WHERE phoneNumber = ?)";

        String insertSql = "INSERT INTO PaymentMethods (customer_id, cardNumber, cardHolder, expiration, cvv) " +
                            "VALUES ((SELECT customer_id FROM Customer WHERE phoneNumber = ?), ?, ?, ?, ?)";

        try(Connection conn = getConnection()){
            //Check count
            try(PreparedStatement pstmt = conn.prepareStatement(countSql)){
                pstmt.setString(1, phoneNumber);
                ResultSet rs = pstmt.executeQuery();
                if(rs.next() && rs.getInt(1) >= 3){
                    return "Maximum of 3 cards allowed.";
                }
            }

            //Add card
            try(PreparedStatement pstmt = conn.prepareStatement(insertSql)){
                pstmt.setString(1, phoneNumber);
                pstmt.setString(2, newCard);
                pstmt.setString(3, holder);
                pstmt.setString(4, exp);
                pstmt.setString(5, cvv);
                pstmt.executeUpdate();
                return "Success";
            }
        }  catch(SQLException e){
            e.printStackTrace();
            return "Database Error: " + e.getMessage();
        }    
    }

    public void deletePaymentMethod(String phoneNumber, String cardToDelete){
        String sql = "DELETE FROM PaymentMethods " +
                     "WHERE cardNumber = ? AND customer_id = (SELECT customer_id FROM Customer WHERE phoneNumber = ?)";

        try(Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, cardToDelete);
            pstmt.setString(2, phoneNumber);
            pstmt.executeUpdate();
             }catch(SQLException e){
                e.printStackTrace();
             }
    }

    //--- ORDER TRANSACTIONS

    public boolean createOrder(String phoneNumber, String pizzaType, double price, double tip){
        String sql = "INSERT INTO Orders (customer_id, pizzaType, price, tip, total) " +
                     "VALUES ((SELECT customer_id FROM Customer WHERE phoneNumber = ?), ?, ?, ?,?)";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)){

                double total = price + tip;
                pstmt.setString(1, phoneNumber);
                pstmt.setString(2, pizzaType);
                pstmt.setDouble(3, price);
                pstmt.setDouble(4, tip);
                pstmt.setDouble(5, total);

                pstmt.executeUpdate();
                return true;
             } catch(SQLException e){
                e.printStackTrace();
                System.out.println("Error: "+e.getMessage());
                return false;
             }
    }
}