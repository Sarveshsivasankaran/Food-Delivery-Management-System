
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import javax.swing.*;

class Main {

    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/";
        String user = "root";
        String password = "";

        try {
            // Load MySQL JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // JFrame
            Customer cust = new Customer();

            // Connect to database
            Connection conn = DriverManager.getConnection(url, user, password);

            // Create Database if not exists
            String createTableSQL = "CREATE database if not exists Restaurant";
            Statement stmt = conn.createStatement();
            stmt.executeUpdate(createTableSQL);
            stmt.executeUpdate("use Restaurant");

            // Tables
            stmt.executeUpdate("CREATE TABLE if not exists Users (user_id INT PRIMARY KEY AUTO_INCREMENT, name VARCHAR(100), email VARCHAR(100) UNIQUE, password VARCHAR(100), phone VARCHAR(15), role ENUM('customer','restaurant','delivery','admin'))");
            stmt.executeUpdate("CREATE TABLE if not exists Restaurants (restaurant_id INT PRIMARY KEY AUTO_INCREMENT, user_id INT, name VARCHAR(100), location VARCHAR(200), contact VARCHAR(15), FOREIGN KEY (user_id) REFERENCES Users(user_id))");
            stmt.executeUpdate("CREATE TABLE if not exists MenuItems (item_id INT PRIMARY KEY AUTO_INCREMENT, restaurant_id INT, name VARCHAR(100), price DECIMAL(8,2), availability BOOLEAN DEFAULT TRUE, FOREIGN KEY (restaurant_id) REFERENCES Restaurants(restaurant_id))");
            stmt.executeUpdate("CREATE TABLE if not exists Orders (order_id INT PRIMARY KEY AUTO_INCREMENT, customer_id INT, restaurant_id INT, order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP, status ENUM('pending','preparing','ready','out for delivery','delivered','cancelled'), payment_mode ENUM('COD','Online'), total_amount DECIMAL(10,2), FOREIGN KEY (customer_id) REFERENCES Users(user_id), FOREIGN KEY (restaurant_id) REFERENCES Restaurants(restaurant_id))");
            stmt.executeUpdate("CREATE TABLE if not exists OrderDetails (detail_id INT PRIMARY KEY AUTO_INCREMENT, order_id INT, item_id INT, quantity INT, price DECIMAL(8,2), FOREIGN KEY (order_id) REFERENCES Orders(order_id), FOREIGN KEY (item_id) REFERENCES MenuItems(item_id))");
            stmt.executeUpdate("CREATE TABLE if not exists Delivery (delivery_id INT PRIMARY KEY AUTO_INCREMENT, order_id INT, agent_id INT, status ENUM('assigned','picked','delivered'), delivery_time TIMESTAMP NULL, FOREIGN KEY (order_id) REFERENCES Orders(order_id), FOREIGN KEY (agent_id) REFERENCES Users(user_id))");
            conn.close();
        } catch (ClassNotFoundException e) {
            System.out.println("❌ MySQL JDBC Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("❌ SQL Error!");
            e.printStackTrace();
        }
    }
}

class Customer extends JFrame {

    public Customer() {
        String url = "jdbc:mysql://localhost:3306/";
        String user = "root";
        String password = "";
        setLayout(new FlowLayout());
        setSize(500, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel LT = new JLabel("Login:");
        JTextField number = new JTextField(20);
        JButton btn = new JButton("OK");

        add(LT);
        add(number);
        add(btn);
        btn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    int n = Integer.parseInt(number.getText());
                    System.out.println("Parsed number: " + n);

                    // Example DB query
                    try (Connection conn = DriverManager.getConnection(
                            "jdbc:mysql://localhost:3306/Restaurant?serverTimezone=UTC",
                            "root", "")) {
                        String insertSQL = "INSERT INTO Users (name, email, password, phone, role) VALUES (?,?,?,?,?)";
                        PreparedStatement ps = conn.prepareStatement(insertSQL);

                        // Example static values (you can replace with JTextFields)
                        ps.setString(1, "John Doe");           // name
                        ps.setString(2, "john" + n + "@mail.com"); // email must be unique
                        ps.setString(3, "secret123");          // password
                        ps.setString(4, String.valueOf(n));    // phone (from input)
                        ps.setString(5, "customer");           // role

                        int rows = ps.executeUpdate();  // ✅ executeUpdate for INSERT
                        if (rows > 0) {
                            System.out.println("✅ User inserted successfully!");
                        } else {
                            System.out.println("⚠️ No user inserted.");
                        }
                    } catch (SQLException sqlEx) {
                        sqlEx.printStackTrace();
                    }

                } catch (NumberFormatException ex) {
                    System.out.println("Invalid number entered: " + number.getText());
                }
            }
        });

        setVisible(true);
    }
}