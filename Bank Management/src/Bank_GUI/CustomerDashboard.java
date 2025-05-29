package Bank_GUI;

import java.awt.*;
import java.sql.*;
import javax.swing.*;

public class CustomerDashboard extends Frame {
    Label nameLabel, emailLabel, balanceLabel, interestLabel;
    TextField amountField, interestRateField;
    Button withdrawBtn, depositBtn, interestBtn, historyBtn, logoutBtn; // ✅ Added logoutBtn

    public CustomerDashboard(int id) {
        setTitle("Customer Dashboard");
        setSize(400, 500); // Increased height to fit logout button
        setLayout(new GridLayout(10, 2)); // ✅ Increased rows from 9 to 10

        // Labels
        nameLabel = new Label("Name: ");
        emailLabel = new Label("Email: ");
        balanceLabel = new Label("Balance: ₹");
        interestLabel = new Label("Interest: ₹0.00");

        // Input fields
        amountField = new TextField();
        interestRateField = new TextField();

        // Buttons
        withdrawBtn = new Button("Withdraw");
        depositBtn = new Button("Deposit");
        interestBtn = new Button("Calculate Interest");
        historyBtn = new Button("View History");
        logoutBtn = new Button("Logout"); // ✅ Initialize logout button

        // Layout
        add(nameLabel); add(new Label());
        add(emailLabel); add(new Label());
        add(balanceLabel); add(new Label());
        add(new Label("Enter Amount:")); add(amountField);
        add(withdrawBtn); add(depositBtn);
        add(new Label("Interest Rate (%):")); add(interestRateField);
        add(interestBtn); add(interestLabel);
        add(historyBtn); add(logoutBtn); // ✅ Add logout to layout

        // Load initial details
        loadCustomerDetails(id);

        // Button Actions
        withdrawBtn.addActionListener(e -> {
            withdrawAmount(id);
            loadCustomerDetails(id);
        });

        depositBtn.addActionListener(e -> {
            depositAmount(id);
            loadCustomerDetails(id);
        });

        interestBtn.addActionListener(e -> {
            calculateInterestOnly(id);
        });

        historyBtn.addActionListener(e -> {
            new TransactionHistoryWindow(id);
        });

        logoutBtn.addActionListener(e -> { // ✅ Handle logout
            dispose(); // Close current window
            new CustomerLogin(); // Show login window again
        });

        setVisible(true);
    }

    private void loadCustomerDetails(int id) {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement pst = conn.prepareStatement("SELECT * FROM customer___details WHERE id=?");
            pst.setInt(1, id);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                nameLabel.setText("Name: " + rs.getString("name"));
                emailLabel.setText("Email: " + rs.getString("gmail"));
                balanceLabel.setText("Balance: ₹" + String.format("%.2f", rs.getDouble("balance")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void withdrawAmount(int id) {
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Invalid withdrawal amount.");
                return;
            }

            try (Connection conn = DBConnection.getConnection()) {
                PreparedStatement pst = conn.prepareStatement("SELECT balance FROM customer___details WHERE id=?");
                pst.setInt(1, id);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    double currentBalance = rs.getDouble("balance");
                    if (currentBalance >= amount) {
                        double newBalance = currentBalance - amount;
                        pst = conn.prepareStatement("UPDATE customer___details SET balance=? WHERE id=?");
                        pst.setDouble(1, newBalance);
                        pst.setInt(2, id);
                        pst.executeUpdate();

                        // ✅ Log the withdrawal
                        pst = conn.prepareStatement("INSERT INTO transaction_history (customer_id, type, amount, balance_after) VALUES (?, 'Withdraw', ?, ?)");
                        pst.setInt(1, id);
                        pst.setDouble(2, amount);
                        pst.setDouble(3, newBalance);
                        pst.executeUpdate();

                        JOptionPane.showMessageDialog(this, "Withdrawn ₹" + amount);
                        amountField.setText("");
                        interestLabel.setText("Interest: ₹0.00");
                    } else {
                        JOptionPane.showMessageDialog(this, "Insufficient balance.");
                    }
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error during withdrawal.");
        }
    }

    private void depositAmount(int id) {
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            if (amount <= 0) {
                JOptionPane.showMessageDialog(this, "Invalid deposit amount.");
                return;
            }

            if (amount > 50000) {
                JOptionPane.showMessageDialog(this, "Maximum deposit per transaction is ₹50,000.");
                return;
            }

            try (Connection conn = DBConnection.getConnection()) {
                PreparedStatement pst = conn.prepareStatement("SELECT balance FROM customer___details WHERE id=?");
                pst.setInt(1, id);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    double currentBalance = rs.getDouble("balance");
                    double newBalance = currentBalance + amount;
                    pst = conn.prepareStatement("UPDATE customer___details SET balance=? WHERE id=?");
                    pst.setDouble(1, newBalance);
                    pst.setInt(2, id);
                    pst.executeUpdate();

                    // ✅ Log the deposit
                    pst = conn.prepareStatement("INSERT INTO transaction_history (customer_id, type, amount, balance_after) VALUES (?, 'Deposit', ?, ?)");
                    pst.setInt(1, id);
                    pst.setDouble(2, amount);
                    pst.setDouble(3, newBalance);
                    pst.executeUpdate();

                    JOptionPane.showMessageDialog(this, "Deposited ₹" + amount);
                    amountField.setText("");
                    interestLabel.setText("Interest: ₹0.00");
                }
            }

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid amount.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error during deposit.");
        }
    }

    private void calculateInterestOnly(int id) {
        try {
            double rate = Double.parseDouble(interestRateField.getText().trim());
            if (rate <= 0) {
                interestLabel.setText("Interest: Invalid rate");
                return;
            }

            try (Connection conn = DBConnection.getConnection()) {
                PreparedStatement pst = conn.prepareStatement("SELECT balance FROM customer___details WHERE id=?");
                pst.setInt(1, id);
                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    double balance = rs.getDouble("balance");
                    double interest = balance * rate / 100.0;
                    interestLabel.setText("Interest @" + rate + "% = ₹" + String.format("%.2f", interest));
                }
            }

        } catch (NumberFormatException e) {
            interestLabel.setText("Interest: Invalid input");
        } catch (Exception e) {
            interestLabel.setText("Interest: Error");
        }
    }
}
