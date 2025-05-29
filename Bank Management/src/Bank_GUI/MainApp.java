package Bank_GUI;

import java.awt.*;
import java.awt.event.*;

public class MainApp extends Frame {

    public MainApp() {
        setTitle("Bank Management System");
        setSize(300, 150);
        setLayout(new FlowLayout());
        setResizable(false);

        Label label = new Label("Select Role:");
        Choice roleChoice = new Choice();
        roleChoice.add("Customer");
        roleChoice.add("Employee");
        roleChoice.add("Admin");

        Button proceedBtn = new Button("Proceed");

        add(label);
        add(roleChoice);
        add(proceedBtn);

        // Action: Role-based redirection
        proceedBtn.addActionListener(e -> {
            String role = roleChoice.getSelectedItem();
            dispose(); // Close main menu window

            switch (role) {
                case "Customer":
                    new CustomerLogin(); // ✅ Opens CustomerLogin
                    break;
                case "Employee":
                    new EmployeeLogin(); // ✅ Opens EmployeeLogin (not dashboard directly)
                    break;
                case "Admin":
                    new AdminLogin(); // ✅ Opens AdminLogin
                    break;
                default:
                    System.out.println("Unknown role selected.");
            }
        });

        // Window close handler
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        new MainApp();
    }
}
