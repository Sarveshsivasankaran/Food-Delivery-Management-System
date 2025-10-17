import javafx.application.Application;
import javafx.geometry.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.*;

public class Main extends Application {

    private static final String URL = "jdbc:mysql://localhost:3306/";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    private Stage stage;

    public static void main(String[] args) {
        setupDatabase();
        launch(args);
    }

    // ---------------- DATABASE SETUP ----------------
    private static void setupDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS Restaurant");
            stmt.executeUpdate("USE Restaurant");

            // Users
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Users (
                    user_id INT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(100),
                    email VARCHAR(100) UNIQUE,
                    password VARCHAR(100),
                    phone VARCHAR(15),
                    role ENUM('customer','restaurant','delivery','admin')
                )
            """);

            // Menu
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Menu (
                    item_id INT PRIMARY KEY AUTO_INCREMENT,
                    item_name VARCHAR(100),
                    price DOUBLE,
                    restaurant_name VARCHAR(100)
                )
            """);

            // Orders
            stmt.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Orders (
                    order_id INT PRIMARY KEY AUTO_INCREMENT,
                    customer_name VARCHAR(100),
                    item_name VARCHAR(100),
                    status ENUM('Pending','Preparing','Out for Delivery','Delivered') DEFAULT 'Pending',
                    delivery_agent VARCHAR(100)
                )
            """);

            System.out.println("✅ Database ready!");
            conn.close();
        } catch (Exception e) {
            System.out.println("❌ Database setup failed");
            e.printStackTrace();
        }
    }

    // ---------------- START APP ----------------
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        stage.setTitle("🍴 Restaurant Management System");
        showLoginPage();
        stage.show();
    }

    // ---------------- LOGIN PAGE ----------------
    private void showLoginPage() {
        Label lblTitle = new Label("🔐 Login");
        lblTitle.setStyle("-fx-font-size: 22px; -fx-font-weight: bold; -fx-text-fill: white;");

        TextField tfEmail = new TextField();
        tfEmail.setPromptText("Enter Email");

        PasswordField tfPass = new PasswordField();
        tfPass.setPromptText("Enter Password");

        Button btnLogin = new Button("Login");
        Button btnRegister = new Button("Register");
        Label lblStatus = new Label();

        btnLogin.setStyle("-fx-background-color: #007bff; -fx-text-fill: white;");
        btnRegister.setStyle("-fx-background-color: #28a745; -fx-text-fill: white;");

        btnLogin.setOnAction(e -> {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Restaurant", USER, PASSWORD)) {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM Users WHERE email=? AND password=?");
                ps.setString(1, tfEmail.getText());
                ps.setString(2, tfPass.getText());
                ResultSet rs = ps.executeQuery();

                if (rs.next()) {
                    String name = rs.getString("name");
                    String role = rs.getString("role");
                    switch (role) {
                        case "customer" -> showCustomerDashboard(name);
                        case "restaurant" -> showRestaurantDashboard(name);
                        case "admin" -> showAdminDashboard(name);
                        case "delivery" -> showDeliveryDashboard(name);
                    }
                } else {
                    lblStatus.setText("❌ Invalid email or password!");
                    lblStatus.setStyle("-fx-text-fill: yellow;");
                }
            } catch (Exception ex) {
                lblStatus.setText("❌ Database error!");
                lblStatus.setStyle("-fx-text-fill: red;");
                ex.printStackTrace();
            }
        });

        btnRegister.setOnAction(e -> showRegisterPage());

        VBox box = new VBox(12, lblTitle, tfEmail, tfPass, btnLogin, btnRegister, lblStatus);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(20));
        box.setStyle("-fx-background-color: linear-gradient(to bottom right, #0062E6, #33AEFF);");

        stage.setScene(new Scene(box, 400, 350));
    }

    // ---------------- REGISTER PAGE ----------------
    private void showRegisterPage() {
        Label lblTitle = new Label("📝 Register New User");
        lblTitle.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        TextField tfName = new TextField(); tfName.setPromptText("Name");
        TextField tfEmail = new TextField(); tfEmail.setPromptText("Email");
        PasswordField tfPass = new PasswordField(); tfPass.setPromptText("Password");
        TextField tfPhone = new TextField(); tfPhone.setPromptText("Phone Number");

        ComboBox<String> cbRole = new ComboBox<>();
        cbRole.getItems().addAll("customer", "restaurant", "delivery", "admin");
        cbRole.setPromptText("Select Role");

        Button btnRegister = new Button("Register");
        Button btnBack = new Button("← Back");
        Label lblStatus = new Label();

        btnRegister.setStyle("-fx-background-color: #20c997; -fx-text-fill: white;");
        btnBack.setStyle("-fx-background-color: #ffc107; -fx-text-fill: black;");

        btnRegister.setOnAction(e -> {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Restaurant", USER, PASSWORD)) {
                String sql = "INSERT INTO Users (name, email, password, phone, role) VALUES (?,?,?,?,?)";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setString(1, tfName.getText());
                ps.setString(2, tfEmail.getText());
                ps.setString(3, tfPass.getText());
                ps.setString(4, tfPhone.getText());
                ps.setString(5, cbRole.getValue());
                ps.executeUpdate();
                lblStatus.setText("✅ Registered Successfully!");
                lblStatus.setStyle("-fx-text-fill: white;");
                tfName.clear(); tfEmail.clear(); tfPass.clear(); tfPhone.clear(); cbRole.setValue(null);
            } catch (SQLException ex) {
                lblStatus.setText("❌ Error: " + ex.getMessage());
                lblStatus.setStyle("-fx-text-fill: red;");
            }
        });

        btnBack.setOnAction(e -> showLoginPage());

        VBox box = new VBox(10, lblTitle, tfName, tfEmail, tfPass, tfPhone, cbRole, btnRegister, btnBack, lblStatus);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(25));
        box.setStyle("-fx-background-color: linear-gradient(to bottom right, #00C9A7, #92FE9D);");

        stage.setScene(new Scene(box, 420, 450));
    }

    // ---------------- CUSTOMER DASHBOARD ----------------
    private void showCustomerDashboard(String name) {
        Label lbl = new Label("👋 Welcome " + name + " (Customer)");
        lbl.setStyle("-fx-font-size: 20px; -fx-text-fill: white; -fx-font-weight: bold;");

        ComboBox<String> cbItems = new ComboBox<>();
        Label lblStatus = new Label();

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Restaurant", USER, PASSWORD)) {
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("SELECT item_name, restaurant_name, price FROM Menu");
            while (rs.next()) {
                cbItems.getItems().add(rs.getString("item_name") + " - ₹" + rs.getDouble("price") + " (" + rs.getString("restaurant_name") + ")");
            }
        } catch (SQLException ex) {
            lblStatus.setText("❌ Failed to load menu: " + ex.getMessage());
            lblStatus.setStyle("-fx-text-fill: yellow;");
        }

        Button btnOrder = new Button("🛒 Place Order");
        Button btnHistory = new Button("📜 View Orders");
        Button logout = new Button("Logout");

        TextArea txtOrders = new TextArea(); txtOrders.setPrefHeight(200); txtOrders.setEditable(false);

        btnOrder.setOnAction(e -> {
            if (cbItems.getValue() == null) {
                lblStatus.setText("⚠️ Select an item first!");
                lblStatus.setStyle("-fx-text-fill: yellow;");
                return;
            }

            String item = cbItems.getValue().split(" - ")[0];

            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Restaurant", USER, PASSWORD)) {
                // auto-assign delivery agent
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT name FROM Users WHERE role='delivery' ORDER BY RAND() LIMIT 1");
                String agent = rs.next() ? rs.getString("name") : "Unassigned";

                PreparedStatement ps = conn.prepareStatement("INSERT INTO Orders (customer_name, item_name, delivery_agent) VALUES (?,?,?)");
                ps.setString(1, name);
                ps.setString(2, item);
                ps.setString(3, agent);
                ps.executeUpdate();

                lblStatus.setText("✅ Order placed! Assigned to: " + agent);
                lblStatus.setStyle("-fx-text-fill: white;");
            } catch (SQLException ex) {
                lblStatus.setText("❌ " + ex.getMessage());
                lblStatus.setStyle("-fx-text-fill: red;");
            }
        });

        btnHistory.setOnAction(e -> {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Restaurant", USER, PASSWORD)) {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM Orders WHERE customer_name=?");
                ps.setString(1, name);
                ResultSet rs = ps.executeQuery();
                StringBuilder sb = new StringBuilder();
                while (rs.next()) {
                    sb.append("#").append(rs.getInt("order_id")).append(" - ")
                            .append(rs.getString("item_name")).append(" [")
                            .append(rs.getString("status")).append("] (Agent: ")
                            .append(rs.getString("delivery_agent")).append(")\n");
                }
                txtOrders.setText(sb.toString());
            } catch (SQLException ex) {
                txtOrders.setText("❌ Error fetching orders");
            }
        });

        logout.setOnAction(e -> showLoginPage());

        VBox box = new VBox(10, lbl, cbItems, btnOrder, btnHistory, txtOrders, lblStatus, logout);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(25));
        box.setStyle("-fx-background-color: linear-gradient(to bottom right, #74ABE2, #5563DE);");

        stage.setScene(new Scene(box, 480, 500));
    }

    // ---------------- DELIVERY DASHBOARD (UPGRADED) ----------------
    private void showDeliveryDashboard(String name) {
        Label lbl = new Label("🚚 Welcome " + name + " (Delivery Agent)");
        lbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        Button btnLoad = new Button("📦 Load My Orders");
        TextArea txt = new TextArea(); txt.setPrefHeight(250);
        TextField tfOrderID = new TextField(); tfOrderID.setPromptText("Enter Order ID");

        Button btnPending = new Button("Pending");
        Button btnPreparing = new Button("Preparing");
        Button btnOut = new Button("Out for Delivery");
        Button btnDelivered = new Button("Delivered");
        Button logout = new Button("Logout");

        HBox statusBtns = new HBox(10, btnPending, btnPreparing, btnOut, btnDelivered);
        statusBtns.setAlignment(Pos.CENTER);

        btnLoad.setOnAction(e -> {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Restaurant", USER, PASSWORD)) {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM Orders WHERE delivery_agent=?");
                ps.setString(1, name);
                ResultSet rs = ps.executeQuery();
                StringBuilder sb = new StringBuilder();
                while (rs.next()) {
                    sb.append("#").append(rs.getInt("order_id")).append(" - ")
                            .append(rs.getString("item_name")).append(" [")
                            .append(rs.getString("status")).append("]\n");
                }
                txt.setText(sb.toString());
            } catch (SQLException ex) {
                txt.setText("❌ Failed to fetch deliveries");
            }
        });

        // update order status
        btnPending.setOnAction(e -> updateStatus(tfOrderID.getText(), "Pending", name, txt));
        btnPreparing.setOnAction(e -> updateStatus(tfOrderID.getText(), "Preparing", name, txt));
        btnOut.setOnAction(e -> updateStatus(tfOrderID.getText(), "Out for Delivery", name, txt));
        btnDelivered.setOnAction(e -> updateStatus(tfOrderID.getText(), "Delivered", name, txt));

        logout.setOnAction(e -> showLoginPage());

        VBox box = new VBox(10, lbl, btnLoad, txt, tfOrderID, statusBtns, logout);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(25));
        box.setStyle("-fx-background-color: linear-gradient(to bottom right, #11998e, #38ef7d);");

        stage.setScene(new Scene(box, 520, 520));
    }

    private void updateStatus(String orderId, String newStatus, String agent, TextArea txt) {
        if (orderId.isEmpty()) {
            txt.setText("⚠️ Enter an order ID first!");
            return;
        }
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Restaurant", USER, PASSWORD)) {
            PreparedStatement ps = conn.prepareStatement("UPDATE Orders SET status=?, delivery_agent=? WHERE order_id=?");
            ps.setString(1, newStatus);
            ps.setString(2, agent);
            ps.setInt(3, Integer.parseInt(orderId));
            int rows = ps.executeUpdate();
            txt.setText(rows > 0 ? "✅ Status updated to: " + newStatus : "⚠️ Order not found!");
        } catch (Exception ex) {
            txt.setText("❌ " + ex.getMessage());
        }
    }

    // ---------------- RESTAURANT DASHBOARD ----------------
    private void showRestaurantDashboard(String name) {
        Label lbl = new Label("🏪 Welcome " + name + " (Restaurant)");
        lbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        TextField tfItem = new TextField(); tfItem.setPromptText("Item name");
        TextField tfPrice = new TextField(); tfPrice.setPromptText("Price");
        Button btnAdd = new Button("➕ Add Item");
        Button btnViewOrders = new Button("📦 View Orders");
        Button logout = new Button("Logout");

        TextArea txt = new TextArea(); txt.setPrefHeight(250); txt.setEditable(false);
        Label lblStatus = new Label();

        btnAdd.setOnAction(e -> {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Restaurant", USER, PASSWORD)) {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO Menu (item_name, price, restaurant_name) VALUES (?,?,?)");
                ps.setString(1, tfItem.getText());
                ps.setDouble(2, Double.parseDouble(tfPrice.getText()));
                ps.setString(3, name);
                ps.executeUpdate();
                lblStatus.setText("✅ Item added!");
                lblStatus.setStyle("-fx-text-fill: white;");
                tfItem.clear(); tfPrice.clear();
            } catch (Exception ex) {
                lblStatus.setText("❌ " + ex.getMessage());
                lblStatus.setStyle("-fx-text-fill: red;");
            }
        });

        btnViewOrders.setOnAction(e -> {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Restaurant", USER, PASSWORD)) {
                PreparedStatement ps = conn.prepareStatement("SELECT * FROM Orders WHERE item_name IN (SELECT item_name FROM Menu WHERE restaurant_name=?)");
                ps.setString(1, name);
                ResultSet rs = ps.executeQuery();
                StringBuilder sb = new StringBuilder();
                while (rs.next()) {
                    sb.append("#").append(rs.getInt("order_id")).append(" - ")
                            .append(rs.getString("customer_name")).append(" ordered ")
                            .append(rs.getString("item_name")).append(" [")
                            .append(rs.getString("status")).append("]\n");
                }
                txt.setText(sb.toString());
            } catch (SQLException ex) {
                txt.setText("❌ Failed to fetch orders");
            }
        });

        logout.setOnAction(e -> showLoginPage());

        VBox box = new VBox(10, lbl, tfItem, tfPrice, btnAdd, btnViewOrders, txt, lblStatus, logout);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(25));
        box.setStyle("-fx-background-color: linear-gradient(to bottom right, #ff758c, #ff7eb3);");

        stage.setScene(new Scene(box, 520, 500));
    }

    // ---------------- ADMIN DASHBOARD ----------------
    private void showAdminDashboard(String name) {
        Label lbl = new Label("🧑‍💼 Admin Panel - " + name);
        lbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        Button btnView = new Button("📋 View All Users");
        TextArea txt = new TextArea();
        txt.setPrefHeight(250);
        txt.setEditable(false);

        btnView.setOnAction(e -> {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Restaurant", USER, PASSWORD)) {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM Users");
                StringBuilder sb = new StringBuilder();
                while (rs.next()) {
                    sb.append(rs.getInt("user_id")).append(". ")
                            .append(rs.getString("name")).append(" - ")
                            .append(rs.getString("email")).append(" (")
                            .append(rs.getString("role")).append(")\n");
                }
                txt.setText(sb.toString());
            } catch (SQLException ex) {
                txt.setText("❌ " + ex.getMessage());
            }
        });

        Button btnViewOrders = new Button("📦 View All Orders");
        btnViewOrders.setOnAction(e -> {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Restaurant", USER, PASSWORD)) {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM Orders");
                StringBuilder sb = new StringBuilder();
                while (rs.next()) {
                    sb.append("#").append(rs.getInt("order_id")).append(" - ")
                            .append(rs.getString("customer_name")).append(" ordered ")
                            .append(rs.getString("item_name")).append(" [")
                            .append(rs.getString("status")).append("] (Agent: ")
                            .append(rs.getString("delivery_agent")).append(")\n");
                }
                txt.setText(sb.toString());
            } catch (SQLException ex) {
                txt.setText("❌ Error fetching orders: " + ex.getMessage());
            }
        });

        Button btnViewMenu = new Button("🍔 View All Menu Items");
        btnViewMenu.setOnAction(e -> {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Restaurant", USER, PASSWORD)) {
                Statement st = conn.createStatement();
                ResultSet rs = st.executeQuery("SELECT * FROM Menu");
                StringBuilder sb = new StringBuilder();
                while (rs.next()) {
                    sb.append("#").append(rs.getInt("item_id")).append(" - ")
                            .append(rs.getString("item_name")).append(" ₹")
                            .append(rs.getDouble("price")).append(" (")
                            .append(rs.getString("restaurant_name")).append(")\n");
                }
                txt.setText(sb.toString());
            } catch (SQLException ex) {
                txt.setText("❌ Error fetching menu: " + ex.getMessage());
            }
        });

        Button btnDeleteUser = new Button("🗑 Delete User by ID");
        TextField tfUserId = new TextField();
        tfUserId.setPromptText("Enter User ID");
        Label lblStatus = new Label();

        btnDeleteUser.setOnAction(e -> {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Restaurant", USER, PASSWORD)) {
                PreparedStatement ps = conn.prepareStatement("DELETE FROM Users WHERE user_id=?");
                ps.setInt(1, Integer.parseInt(tfUserId.getText()));
                int rows = ps.executeUpdate();
                if (rows > 0) {
                    lblStatus.setText("✅ User deleted successfully!");
                    lblStatus.setStyle("-fx-text-fill: white;");
                } else {
                    lblStatus.setText("⚠️ User not found!");
                    lblStatus.setStyle("-fx-text-fill: yellow;");
                }
            } catch (Exception ex) {
                lblStatus.setText("❌ " + ex.getMessage());
                lblStatus.setStyle("-fx-text-fill: red;");
            }
        });

        Button logout = new Button("Logout");
        logout.setStyle("-fx-background-color: #ff4757; -fx-text-fill: white;");
        logout.setOnAction(e -> showLoginPage());

        VBox box = new VBox(10,
                lbl,
                new HBox(10, btnView, btnViewOrders, btnViewMenu),
                txt,
                new HBox(10, tfUserId, btnDeleteUser),
                lblStatus,
                logout
        );
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(25));
        box.setStyle("-fx-background-color: linear-gradient(to bottom right, #434343, #000000);");

        stage.setScene(new Scene(box, 600, 500));
    }
}