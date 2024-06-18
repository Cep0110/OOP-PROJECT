import javax.swing.*;
//import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

 class ManageExams extends JFrame implements ActionListener {
    private JLabel questionLabel, option1Label, option2Label, option3Label, option4Label, correctOptionLabel;
    private JTextField questionField, option1Field, option2Field, option3Field, option4Field, correctOptionField;
    private JButton addButton, updateButton;
    private Connection conn;

    public ManageExams() {
        setTitle("Manage Exams");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        // Database Connection
        try {
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/exam", "root", "");
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Failed to connect to database: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
            System.exit(1);
        }

        questionLabel = new JLabel("Question:");
        questionLabel.setBounds(50, 50, 100, 30);
        add(questionLabel);

        questionField = new JTextField();
        questionField.setBounds(160, 50, 400, 30);
        add(questionField);

        option1Label = new JLabel("Option 1:");
        option1Label.setBounds(50, 100, 100, 30);
        add(option1Label);

        option1Field = new JTextField();
        option1Field.setBounds(160, 100, 400, 30);
        add(option1Field);

        option2Label = new JLabel("Option 2:");
        option2Label.setBounds(50, 150, 100, 30);
        add(option2Label);

        option2Field = new JTextField();
        option2Field.setBounds(160, 150, 400, 30);
        add(option2Field);

        option3Label = new JLabel("Option 3:");
        option3Label.setBounds(50, 200, 100, 30);
        add(option3Label);

        option3Field = new JTextField();
        option3Field.setBounds(160, 200, 400, 30);
        add(option3Field);

        option4Label = new JLabel("Option 4:");
        option4Label.setBounds(50, 250, 100, 30);
        add(option4Label);

        option4Field = new JTextField();
        option4Field.setBounds(160, 250, 400, 30);
        add(option4Field);

        correctOptionLabel = new JLabel("Correct Option:");
        correctOptionLabel.setBounds(50, 300, 100, 30);
        add(correctOptionLabel);

        correctOptionField = new JTextField();
        correctOptionField.setBounds(160, 300, 200, 30);
        add(correctOptionField);

        addButton = new JButton("Add Question");
        addButton.setBounds(200, 350, 150, 30);
        addButton.addActionListener(this);
        add(addButton);

        updateButton = new JButton("Update Question");
        updateButton.setBounds(370, 350, 150, 30);
        updateButton.addActionListener(this);
        add(updateButton);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            addQuestion();
        } else if (e.getSource() == updateButton) {
            updateQuestion();
        }
    }

    private void addQuestion() {
        String questionText = questionField.getText();
        String option1 = option1Field.getText();
        String option2 = option2Field.getText();
        String option3 = option3Field.getText();
        String option4 = option4Field.getText();
        String correctOption = correctOptionField.getText();

        if (questionText.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty() || correctOption.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String query = "INSERT INTO questions (questionText, option1, option2, option3, option4, correctOption) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, questionText);
            pstmt.setString(2, option1);
            pstmt.setString(3, option2);
            pstmt.setString(4, option3);
            pstmt.setString(5, option4);
            pstmt.setString(6, correctOption);

            int rowsInserted = pstmt.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Question added successfully.");
                clearFields();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error adding question: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private void updateQuestion() {
        String questionText = questionField.getText();
        String option1 = option1Field.getText();
        String option2 = option2Field.getText();
        String option3 = option3Field.getText();
        String option4 = option4Field.getText();
        String correctOption = correctOptionField.getText();

        if (questionText.isEmpty() || option1.isEmpty() || option2.isEmpty() || option3.isEmpty() || option4.isEmpty() || correctOption.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill in all fields.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            String query = "UPDATE questions SET questionText = ?, option1 = ?, option2 = ?, option3 = ?, option4 = ?, correctOption = ? WHERE questionID = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, questionText);
            pstmt.setString(2, option1);
            pstmt.setString(3, option2);
            pstmt.setString(4, option3);
            pstmt.setString(5, option4);
            pstmt.setString(6, correctOption);
            pstmt.setInt(7, getCurrentQuestionID());

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                JOptionPane.showMessageDialog(this, "Question updated successfully.");
                clearFields();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Error updating question: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            ex.printStackTrace();
        }
    }

    private int getCurrentQuestionID() {
        // Retrieve questionID of the currently displayed question (if needed for updating)
        // For simplicity, you may use a direct input or fetch from UI context.
        return 0; // Replace with actual logic to get current question ID
    }

    private void clearFields() {
        questionField.setText("");
        option1Field.setText("");
        option2Field.setText("");
        option3Field.setText("");
        option4Field.setText("");
        correctOptionField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ManageExams::new);
    }
}
