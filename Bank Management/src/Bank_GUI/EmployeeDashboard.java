package Bank_GUI;

import java.awt.*;
import java.awt.event.*;

public class EmployeeDashboard extends Frame {

    public EmployeeDashboard(String employeeName) {
        setTitle("Employee Dashboard");
        setSize(300, 200);
        setLayout(new FlowLayout());

        Label welcome = new Label("Welcome, " + employeeName);
        Button logoutBtn = new Button("Logout");

        add(welcome);
        add(logoutBtn);

        // Handle logout button action
        logoutBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close the dashboard
                new EmployeeLogin(); // Go back to Employee Login
            }
        });

        setVisible(true);
    }
}
