import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

class Login extends JFrame implements ActionListener {
    JLabel userLabel, passwordLabel, loginAsLabel;
    JTextField userText;
    JPasswordField passwordText;
    JButton loginButton, cancelButton;
    JComboBox<String> loginAsCombo;
    Connection conn;

    public Login() {
        // Initialize the database connection
        try {
            conn = DatabaseConnection.getConnection();
            if (conn == null) {
                JOptionPane.showMessageDialog(this, "Database connection is null. Exiting...", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to connect to the database. Exiting...", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            System.exit(1);
        }

        setTitle("Login");
        setSize(400, 300);
        setLayout(null);
        getContentPane().setBackground(Color.GREEN);

        loginAsLabel = new JLabel("Login as:");
        loginAsLabel.setBounds(50, 30, 80, 25);
        add(loginAsLabel);

        String[] options = {"Student", "Admin"};
        loginAsCombo = new JComboBox<>(options);
        loginAsCombo.setBounds(150, 30, 165, 25);
        add(loginAsCombo);

        userLabel = new JLabel("Full Name:");
        userLabel.setBounds(50, 70, 80, 25);
        add(userLabel);

        userText = new JTextField(20);
        userText.setBounds(150, 70, 165, 25);
        add(userText);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 110, 80, 25);
        add(passwordLabel);

        passwordText = new JPasswordField(20);
        passwordText.setBounds(150, 110, 165, 25);
        add(passwordText);

        loginButton = new JButton("Login");
        loginButton.setBounds(50, 150, 80, 25);
        loginButton.addActionListener(this);
        add(loginButton);

        cancelButton = new JButton("Cancel");
        cancelButton.setBounds(150, 150, 80, 25);
        cancelButton.addActionListener(this);
        add(cancelButton);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == loginButton) {
            String username = userText.getText();
            String password = new String(passwordText.getPassword());
            String loginAs = (String) loginAsCombo.getSelectedItem();

            if (loginAs.equals("Student")) {
                loginAsStudent(username, password);
            } else if (loginAs.equals("Admin")) {
                loginAsAdmin(username, password);
            }
        } else if (e.getSource() == cancelButton) {
            System.exit(0);
        }
    }

    void loginAsStudent(String username, String password) {
        try {
            String query = "SELECT * FROM Students WHERE fullName = ? AND password = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // Successful login
                JOptionPane.showMessageDialog(this, "Welcome to Online Examination!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                new OnlineLicenseExamination("Online License Examination", username); // Pass the username
                this.dispose(); // Close the login window
            } else {
                // Failed login
                JOptionPane.showMessageDialog(this, "Invalid full name or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error while authenticating: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void loginAsAdmin(String username, String password) {
        try {
            String query = "SELECT * FROM Users WHERE username = ? AND password = ? AND role = 'admin'";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                // Successful login
                JOptionPane.showMessageDialog(this, "Welcome Admin!", "Login Successful", JOptionPane.INFORMATION_MESSAGE);
                new Home(conn); // Assuming this is the class handling the admin home page
                this.dispose(); // Close the login window
            } else {
                // Failed login
                JOptionPane.showMessageDialog(this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error while authenticating: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Login();
    }
}
