package Bank_GUI;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import javax.swing.JOptionPane;

public class EmployeeLogin extends Frame {
    TextField idField, nameField, positionField, passField;
    Button loginBtn;

    public EmployeeLogin() {
        setTitle("Employee Login");
        setSize(300, 300);
        setLayout(new GridLayout(6, 2)); // Added row for position

        // Fields and Labels
        add(new Label("Employee ID:"));
        idField = new TextField();
        add(idField);

        add(new Label("Name:"));
        nameField = new TextField();
        add(nameField);

        add(new Label("Position:")); // ✅ Added position
        positionField = new TextField();
        add(positionField);

        add(new Label("Password:"));
        passField = new TextField();
        passField.setEchoChar('*');
        add(passField);

        loginBtn = new Button("Login");
        add(new Label()); // Spacer
        add(loginBtn);

        loginBtn.addActionListener(e -> login());

        setVisible(true);
    }

    void login() {
        try {
            int id = Integer.parseInt(idField.getText().trim());
            String name = nameField.getText().trim();
            String position = positionField.getText().trim(); // ✅ Get position
            String password = passField.getText().trim();

            try (Connection conn = DBConnection.getConnection()) {
                PreparedStatement pst = conn.prepareStatement(
                    "SELECT * FROM employee_details WHERE id=? AND name=? AND position=? AND password=?"
                );
                pst.setInt(1, id);
                pst.setString(2, name);
                pst.setString(3, position); // ✅ Include position in check
                pst.setString(4, password);

                ResultSet rs = pst.executeQuery();

                if (rs.next()) {
                    dispose();
                    new EmployeeDashboard(name); // You can pass more details if needed
                } else {
                    JOptionPane.showMessageDialog(this, "Invalid login details!");
                }
            }
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error during login.");
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new EmployeeLogin();
    }
}
