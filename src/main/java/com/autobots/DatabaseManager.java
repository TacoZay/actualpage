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
}