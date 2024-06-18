import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class AddStudent extends JFrame implements ActionListener {
 JTextField regNumberField, fullNameField, phoneNumberField, birthDateField, bloodTypeField, addressField, schoolNameField, photoPathField;
 JPasswordField passwordField;
 JButton addButton, updateButton, deleteButton, clearButton;
  Connection conn;

    public AddStudent(Connection conn) {
        this.conn = conn;

        setTitle("Manage Students");
        setSize(600, 500);
        setLayout(null);
        getContentPane().setBackground(Color.GREEN);

        // Initialize GUI components
        JLabel regNumberLabel = new JLabel("Registration Number:");
        regNumberLabel.setBounds(30, 30, 150, 25);
        add(regNumberLabel);

        regNumberField = new JTextField();
        regNumberField.setBounds(200, 30, 200, 25);
        add(regNumberField);

        JLabel fullNameLabel = new JLabel("Full Name:");
        fullNameLabel.setBounds(30, 70, 150, 25);
        add(fullNameLabel);

        fullNameField = new JTextField();
        fullNameField.setBounds(200, 70, 200, 25);
        add(fullNameField);

        JLabel phoneNumberLabel = new JLabel("Phone Number:");
        phoneNumberLabel.setBounds(30, 110, 150, 25);
        add(phoneNumberLabel);

        phoneNumberField = new JTextField();
        phoneNumberField.setBounds(200, 110, 200, 25);
        add(phoneNumberField);

        JLabel birthDateLabel = new JLabel("Birth Date (YYYY-MM-DD):");
        birthDateLabel.setBounds(30, 150, 150, 25);
        add(birthDateLabel);

        birthDateField = new JTextField();
        birthDateField.setBounds(200, 150, 200, 25);
        add(birthDateField);

        JLabel bloodTypeLabel = new JLabel("Blood Type:");
        bloodTypeLabel.setBounds(30, 190, 150, 25);
        add(bloodTypeLabel);

        bloodTypeField = new JTextField();
        bloodTypeField.setBounds(200, 190, 200, 25);
        add(bloodTypeField);

        JLabel addressLabel = new JLabel("Address:");
        addressLabel.setBounds(30, 230, 150, 25);
        add(addressLabel);

        addressField = new JTextField();
        addressField.setBounds(200, 230, 200, 25);
        add(addressField);

        JLabel schoolNameLabel = new JLabel("School Name:");
        schoolNameLabel.setBounds(30, 270, 150, 25);
        add(schoolNameLabel);

        schoolNameField = new JTextField();
        schoolNameField.setBounds(200, 270, 200, 25);
        add(schoolNameField);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(30, 310, 150, 25);
        add(passwordLabel);

        passwordField = new JPasswordField();
        passwordField.setBounds(200, 310, 200, 25);
        add(passwordField);

        JLabel photoPathLabel = new JLabel("Photo Path:");
        photoPathLabel.setBounds(30, 350, 150, 25);
        add(photoPathLabel);

        photoPathField = new JTextField();
        photoPathField.setBounds(200, 350, 200, 25);
        add(photoPathField);

        addButton = new JButton("Add");
        addButton.setBounds(50, 400, 100, 30);
        addButton.addActionListener(this);
        add(addButton);

        updateButton = new JButton("Update");
        updateButton.setBounds(180, 400, 100, 30);
        updateButton.addActionListener(this);
        add(updateButton);

        deleteButton = new JButton("Delete");
        deleteButton.setBounds(310, 400, 100, 30);
        deleteButton.addActionListener(this);
        add(deleteButton);

        clearButton = new JButton("Clear");
        clearButton.setBounds(440, 400, 100, 30);
        clearButton.addActionListener(this);
        add(clearButton);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addStudent();
        } else if (e.getSource() == updateButton) {
            updateStudent();
        } else if (e.getSource() == deleteButton) {
            deleteStudent();
        } else if (e.getSource() == clearButton) {
            clearFields();
        }
    }

     void addStudent() {
        try {
            String regNumber = regNumberField.getText();
            String fullName = fullNameField.getText();
            String phoneNumber = phoneNumberField.getText();
            String birthDateStr = birthDateField.getText();
            String bloodType = bloodTypeField.getText();
            String address = addressField.getText();
            String schoolName = schoolNameField.getText();
            String password = new String(passwordField.getPassword());
            String photoPath = photoPathField.getText();

            // Parse birth date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date birthDate = sdf.parse(birthDateStr);
            int birthDateInt = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(birthDate));

            String query = "INSERT INTO Students (regNumber, fullName, phoneNumber, birthDate, bloodType, address, password, schoolName, photoPath) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, regNumber);
            pstmt.setString(2, fullName);
            pstmt.setString(3, phoneNumber);
            pstmt.setInt(4, birthDateInt);
            pstmt.setString(5, bloodType);
            pstmt.setString(6, address);
            pstmt.setString(7, password);
            pstmt.setString(8, schoolName);
            pstmt.setString(9, photoPath);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student added successfully!");
            clearFields();
        } catch (SQLException | ParseException ex) {
            JOptionPane.showMessageDialog(this, "Error adding student: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void updateStudent() {
        try {
            String regNumber = regNumberField.getText();
            String fullName = fullNameField.getText();
            String phoneNumber = phoneNumberField.getText();
            String birthDateStr = birthDateField.getText();
            String bloodType = bloodTypeField.getText();
            String address = addressField.getText();
            String schoolName = schoolNameField.getText();
            String password = new String(passwordField.getPassword());
            String photoPath = photoPathField.getText();

            // Parse birth date
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date birthDate = sdf.parse(birthDateStr);
            int birthDateInt = Integer.parseInt(new SimpleDateFormat("yyyyMMdd").format(birthDate));

            String query = "UPDATE Students SET fullName = ?, phoneNumber = ?, birthDate = ?, bloodType = ?, address = ?, password = ?, schoolName = ?, photoPath = ? WHERE regNumber = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, fullName);
            pstmt.setString(2, phoneNumber);
            pstmt.setInt(3, birthDateInt);
            pstmt.setString(4, bloodType);
            pstmt.setString(5, address);
            pstmt.setString(6, password);
            pstmt.setString(7, schoolName);
            pstmt.setString(8, photoPath);
            pstmt.setString(9, regNumber);

            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student updated successfully!");
            clearFields();
        } catch (SQLException | ParseException ex) {
            JOptionPane.showMessageDialog(this, "Error updating student: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void deleteStudent() {
        try {
            String regNumber = regNumberField.getText();
            String query = "DELETE FROM Students WHERE regNumber = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, regNumber);
            pstmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Student deleted successfully!");
            clearFields();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error deleting student: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void clearFields() {
        regNumberField.setText("");
        fullNameField.setText("");
        phoneNumberField.setText("");
        birthDateField.setText("");
        bloodTypeField.setText("");
        addressField.setText("");
        schoolNameField.setText("");
        passwordField.setText("");
        photoPathField.setText("");
    }

    public static void main(String[] args) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            if (conn != null) {
                new AddStudent(conn);
            } else {
                JOptionPane.showMessageDialog(null, "Database connection is null. Exiting...", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(null, "Failed to connect to the database. Exiting...", "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
