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
                lblStatus.setStyle("-fx-text-fill: lightgreen;");
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
        Button logout = new Button("Logout");
        logout.setStyle("-fx-background-color: #ff4757; -fx-text-fill: white;");
        logout.setOnAction(e -> showLoginPage());

        Button orderBtn = new Button("🛒 Place Dummy Order");
        Label lblMsg = new Label();

        orderBtn.setOnAction(e -> {
            try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Restaurant", USER, PASSWORD)) {
                PreparedStatement ps = conn.prepareStatement("INSERT INTO Users (name, email, password, phone, role) VALUES (?,?,?,?,?)");
                ps.setString(1, name + "_order");
                ps.setString(2, name + "_order@mail.com");
                ps.setString(3, "temp");
                ps.setString(4, "9999999999");
                ps.setString(5, "customer");
                ps.executeUpdate();
                lblMsg.setText("✅ Dummy data inserted!");
                lblMsg.setStyle("-fx-text-fill: yellow;");
            } catch (SQLException ex) {
                lblMsg.setText("❌ " + ex.getMessage());
                lblMsg.setStyle("-fx-text-fill: red;");
            }
        });

        VBox box = new VBox(15, lbl, orderBtn, lblMsg, logout);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(25));
        box.setStyle("-fx-background-color: linear-gradient(to bottom right, #74ABE2, #5563DE);");

        stage.setScene(new Scene(box, 420, 350));
    }

    // ---------------- RESTAURANT DASHBOARD ----------------
    private void showRestaurantDashboard(String name) {
        Label lbl = new Label("🏪 Welcome " + name + " (Restaurant)");
        lbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        Button logout = new Button("Logout");
        logout.setStyle("-fx-background-color: #ff6b81; -fx-text-fill: white;");
        logout.setOnAction(e -> showLoginPage());

        VBox box = new VBox(20, lbl, new Label("🍔 Manage menu & orders here soon!"), logout);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(25));
        box.setStyle("-fx-background-color: linear-gradient(to bottom right, #ff758c, #ff7eb3);");

        stage.setScene(new Scene(box, 420, 350));
    }

    // ---------------- ADMIN DASHBOARD ----------------
    private void showAdminDashboard(String name) {
        Label lbl = new Label("🧑‍💼 Admin Panel - " + name);
        lbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");

        Button btnView = new Button("📋 View All Users");
        TextArea txt = new TextArea();
        txt.setPrefHeight(200);
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

        Button logout = new Button("Logout");
        logout.setStyle("-fx-background-color: #ff4757; -fx-text-fill: white;");
        logout.setOnAction(e -> showLoginPage());

        VBox box = new VBox(15, lbl, btnView, txt, logout);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(25));
        box.setStyle("-fx-background-color: linear-gradient(to bottom right, #434343, #000000);");

        stage.setScene(new Scene(box, 500, 400));
    }

    // ---------------- DELIVERY DASHBOARD ----------------
    private void showDeliveryDashboard(String name) {
        Label lbl = new Label("🚚 Welcome " + name + " (Delivery Agent)");
        lbl.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: white;");
        Button logout = new Button("Logout");
        logout.setStyle("-fx-background-color: #343a40; -fx-text-fill: white;");
        logout.setOnAction(e -> showLoginPage());

        VBox box = new VBox(20, lbl, new Label("📦 Delivery updates coming soon!"), logout);
        box.setAlignment(Pos.CENTER);
        box.setPadding(new Insets(25));
        box.setStyle("-fx-background-color: linear-gradient(to bottom right, #11998e, #38ef7d);");

        stage.setScene(new Scene(box, 420, 350));
    }
}
