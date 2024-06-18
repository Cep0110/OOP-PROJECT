import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
class User extends JFrame implements ActionListener {
    private JLabel usernameLabel, passwordLabel, roleLabel, messageLabel;
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JComboBox<String> roleComboBox;
    private JButton addButton;
    private Connection conn;

    public User() {
        setTitle("License Examination Center");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Color.GREEN);
        setLayout(null);

        // Database Connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/exam", "root", "");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to connect to database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            System.exit(1);
        }

        messageLabel = new JLabel("Welcome to License Examination Center");
        messageLabel.setFont(new Font("Arial", Font.BOLD, 16));
        messageLabel.setBounds(50, 20, 400, 30);
        add(messageLabel);

        usernameLabel = new JLabel("Username:");
        usernameLabel.setBounds(50, 80, 100, 30);
        add(usernameLabel);

        usernameField = new JTextField();
        usernameField.setBounds(150, 80, 300, 30);
        add(usernameField);

        passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(50, 130, 100, 30);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 130, 300, 30);
        add(passwordField);

        roleLabel = new JLabel("Role:");
        roleLabel.setBounds(50, 180, 100, 30);
        add(roleLabel);

        String[] roles = {"Admin", "Student"};
        roleComboBox = new JComboBox<>(roles);
        roleComboBox.setBounds(150, 180, 300, 30);
        add(roleComboBox);

        addButton = new JButton("Add User");
        addButton.setBounds(200, 230, 100, 30);
        addButton.addActionListener(this);
        add(addButton);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addUser();
        }
    }

    private void addUser() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());
        String role = (String) roleComboBox.getSelectedItem();

        if (username.isEmpty() || password.isEmpty() || role.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String query = "INSERT INTO users (username, password, role) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "User added successfully.");
                clearFields();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding user: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        usernameField.setText("");
        passwordField.setText("");
        roleComboBox.setSelectedIndex(0);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(User::new);
    }
}
