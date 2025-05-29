package Bank_GUI;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class CustomerLogin extends Frame {
    TextField idField, passField;

    public CustomerLogin() {
        setTitle("Customer Login");
        setSize(300, 220);
        setLayout(new GridLayout(4, 2)); // Changed from 3 to 4 rows to fit register button

        Label idLabel = new Label("Customer ID:");
        Label passLabel = new Label("Password:");
        idField = new TextField();
        passField = new TextField();
        passField.setEchoChar('*');

        Button loginBtn = new Button("Login");
        Button registerBtn = new Button("Register"); // Register button

        add(idLabel); add(idField);
        add(passLabel); add(passField);
        add(loginBtn); add(registerBtn); // Added in the same row

        loginBtn.addActionListener(e -> login());
        registerBtn.addActionListener(e -> new CustomerRegistration()); // Opens registration form

        setVisible(true);
    }

    void login() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String password = passField.getText().trim();

            try (Connection conn = DBConnection.getConnection()) {
                PreparedStatement pst = conn.prepareStatement("SELECT * FROM customer___details WHERE id=? AND password=?");
                pst.setInt(1, id);
                pst.setString(2, password);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    dispose();
                    new CustomerDashboard(id); // Open customer dashboard after successful login
                } else {
                    System.out.println("Invalid ID or Password!");
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new CustomerLogin();
    }
}
