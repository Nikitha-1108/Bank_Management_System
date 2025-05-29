package Bank_GUI;

import java.awt.*;
import java.sql.*;

public class TransactionHistoryWindow extends Frame {
    TextArea historyArea;

    public TransactionHistoryWindow(int customerId) {
        setTitle("Transaction History");
        setSize(600, 450); // Slightly larger for clarity
        setLayout(new BorderLayout());

        historyArea = new TextArea();
        historyArea.setEditable(false);

        // Use monospaced font for clean alignment
        historyArea.setFont(new Font("Courier New", Font.PLAIN, 14));

        add(new Label("Recent Transactions:", Label.CENTER), BorderLayout.NORTH);
        add(historyArea, BorderLayout.CENTER);

        loadHistory(customerId);

        setVisible(true);
    }

    private void loadHistory(int customerId) {
        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement pst = conn.prepareStatement(
                "SELECT type, amount, balance_after, timestamp FROM transaction_history WHERE customer_id = ? ORDER BY timestamp DESC"
            );
            pst.setInt(1, customerId);
            ResultSet rs = pst.executeQuery();

            StringBuilder sb = new StringBuilder();

            // Header
            sb.append(String.format("%-12s %-12s %-18s %-25s\n", "Type", "Amount", "Balance After", "Date & Time"));
            sb.append("-------------------------------------------------------------------------------\n");

            // Records
            while (rs.next()) {
                String type = rs.getString("type");
                double amount = rs.getDouble("amount");
                double balance = rs.getDouble("balance_after");
                Timestamp time = rs.getTimestamp("timestamp");

                sb.append(String.format(
                    "%-12s ₹%-11.2f ₹%-16.2f %-25s\n",
                    type, amount, balance, time.toString()
                ));
            }

            historyArea.setText(sb.toString());

        } catch (Exception e) {
            historyArea.setText("Failed to load transaction history.");
            e.printStackTrace();
        }
    }
}
