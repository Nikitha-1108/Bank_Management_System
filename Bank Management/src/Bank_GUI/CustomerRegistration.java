package Bank_GUI;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.*;

public class CustomerRegistration extends Frame {
    TextField nameField, emailField, passField, balanceField;
    Button registerBtn;

    public CustomerRegistration() {
        setTitle("Customer Registration");
        setSize(350, 250);
        setLayout(new GridLayout(5, 2));

        add(new Label("Name:"));
        nameField = new TextField();
        add(nameField);

        add(new Label("Email:"));
        emailField = new TextField();
        add(emailField);

        add(new Label("Password:"));
        passField = new TextField();
        passField.setEchoChar('*');
        add(passField);

        add(new Label("Initial Balance:"));
        balanceField = new TextField();
        add(balanceField);

        registerBtn = new Button("Register");
        add(new Label());
        add(registerBtn);

        registerBtn.addActionListener(e -> registerCustomer());

        setVisible(true);
    }

    void registerCustomer() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passField.getText().trim();
        double balance = 0.0;

        try {
            balance = Double.parseDouble(balanceField.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid balance input.");
            return;
        }

        if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.");
            return;
        }

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement pst = conn.prepareStatement(
                "INSERT INTO customer___details (name, gmail, password, balance) VALUES (?, ?, ?, ?)"
            );
            pst.setString(1, name);
            pst.setString(2, email);
            pst.setString(3, password);
            pst.setDouble(4, balance);

            int result = pst.executeUpdate();
            if (result > 0) {
                JOptionPane.showMessageDialog(this, "Registration successful!");
                dispose();
                new CustomerLogin();
            } else {
                JOptionPane.showMessageDialog(this, "Registration failed.");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Database error.");
        }
    }

    public static void main(String[] args) {
        new CustomerRegistration();
    }
}
