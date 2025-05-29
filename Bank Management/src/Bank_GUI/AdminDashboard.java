package Bank_GUI;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class AdminDashboard extends Frame {

    TextArea output;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(500, 450);
        setLayout(new BorderLayout());

        Label title = new Label("Customer Records", Label.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 18));
        add(title, BorderLayout.NORTH);

        output = new TextArea();
        output.setEditable(false);
        add(output, BorderLayout.CENTER);

        // Panel for buttons
        Panel buttonPanel = new Panel();
        buttonPanel.setLayout(new GridLayout(1, 3)); // Changed to 3 columns

        Button refreshBtn = new Button("Refresh");
        Button monthlyInterestBtn = new Button("Simulate Monthly Interest Update");
        Button logoutBtn = new Button("Logout"); // New logout button

        buttonPanel.add(refreshBtn);
        buttonPanel.add(monthlyInterestBtn);
        buttonPanel.add(logoutBtn); // Add logout button to panel
        add(buttonPanel, BorderLayout.SOUTH);

        // Action listeners
        refreshBtn.addActionListener(e -> loadCustomerData());
        monthlyInterestBtn.addActionListener(e -> updateMonthlyInterest());
        logoutBtn.addActionListener(e -> {
            dispose(); // Close current dashboard
            new AdminLogin(); // Open AdminLogin screen again
        });

        setVisible(true);
        loadCustomerData();
    }

    void loadCustomerData() {
        output.setText(""); // Clear previous content
        try (Connection conn = DBConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM customer___details");

            while (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String gmail = rs.getString("gmail");
                double balance = rs.getDouble("balance");

                output.append("ID: " + id + ", Name: " + name + ", Email: " + gmail + ", Balance: ₹" + balance + "\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
            output.setText("Failed to load customer data.");
        }
    }

    void updateMonthlyInterest() {
        double interestRate = 0.05; // Example: 5% interest rate

        try (Connection conn = DBConnection.getConnection()) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, balance FROM customer___details");

            while (rs.next()) {
                int customerId = rs.getInt("id");
                double balance = rs.getDouble("balance");
                double interest = balance * interestRate;
                double updatedBalance = balance + interest;

                // Update balance
                PreparedStatement updateStmt = conn.prepareStatement(
                        "UPDATE customer___details SET balance=? WHERE id=?");
                updateStmt.setDouble(1, updatedBalance);
                updateStmt.setInt(2, customerId);
                updateStmt.executeUpdate();

                // Insert into transaction_history
                PreparedStatement logStmt = conn.prepareStatement(
                        "INSERT INTO transaction_history (customer_id, type, amount, balance_after) VALUES (?, ?, ?, ?)");
                logStmt.setInt(1, customerId);
                logStmt.setString(2, "Interest");
                logStmt.setDouble(3, interest);
                logStmt.setDouble(4, updatedBalance);
                logStmt.executeUpdate();
            }

            JOptionPane.showMessageDialog(this, "Monthly interest updated for all customers.");
            loadCustomerData(); // Refresh the view

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Failed to update interest.");
        }
    }
}
