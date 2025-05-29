package Bank_GUI;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

public class AdminLogin extends Frame {
    TextField usernameField, passwordField;

    public AdminLogin() {
        setTitle("Admin Login");
        setSize(300, 200);
        setLayout(new GridLayout(3, 2));

        Label userLabel = new Label("Username:");
        Label passLabel = new Label("Password:");
        usernameField = new TextField();
        passwordField = new TextField();
        passwordField.setEchoChar('*');
        Button loginBtn = new Button("Login");

        add(userLabel); add(usernameField);
        add(passLabel); add(passwordField);
        add(new Label()); add(loginBtn);

        loginBtn.addActionListener(e -> login());

        setVisible(true);
    }

    void login() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        // Simple hardcoded admin check
        if (username.equals("admin") && password.equals("admin123")) {
            dispose();
            new AdminDashboard();
        } else {
            System.out.println("Invalid Admin Credentials");
        }
    }

    public static void main(String[] args) {
        new AdminLogin();
    }
}
