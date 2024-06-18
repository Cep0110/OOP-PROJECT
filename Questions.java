import javax.swing.*;
import java.awt.event.*;
import java.sql.*;
import java.util.ArrayList;

class OnlineLicenseExamination extends JFrame implements ActionListener {
    JLabel l;
    JRadioButton[] jb = new JRadioButton[5];
    JButton b1, b2;
    ButtonGroup bg;
    int count = 0, current = 0, x = 1, y = 1, now = 0;
    ArrayList<Question> questions;
    String username;
    Connection conn;

    OnlineLicenseExamination(String s, String username) {
        super(s);
        this.username = username;

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

        l = new JLabel();
        add(l);
        bg = new ButtonGroup();
        for (int i = 0; i < 5; i++) {
            jb[i] = new JRadioButton();
            add(jb[i]);
            bg.add(jb[i]);
        }
        b1 = new JButton("Next");
        b2 = new JButton("Bookmark");
        b1.addActionListener(this);
        b2.addActionListener(this);
        add(b1);
        add(b2);
        l.setBounds(30, 40, 450, 20);
        jb[0].setBounds(50, 80, 200, 20);
        jb[1].setBounds(50, 110, 200, 20);
        jb[2].setBounds(50, 140, 200, 20);
        jb[3].setBounds(50, 170, 200, 20);
        b1.setBounds(100, 240, 100, 30);
        b2.setBounds(270, 240, 100, 30);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocation(250, 100);
        setVisible(true);
        setSize(600, 350);

        // Initialize the questions list
        questions = new ArrayList<>();
        loadQuestions();
        set();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == b1) {
            if (check()) {
                count = count + 1;
            }
            current++;
            set();
            if (current == questions.size() - 1) {
                b1.setEnabled(false);
                b2.setText("Result");
            }
        }
        if (e.getActionCommand().equals("Bookmark")) {
            JButton bk = new JButton("Bookmark" + x);
            bk.setBounds(480, 20 + 30 * x, 100, 30);
            add(bk);
            bk.addActionListener(this);
            x++;
            current++;
            set();
            if (current == questions.size() - 1) {
                b2.setText("Result");
            }
            setVisible(false);
            setVisible(true);
        }
        for (int i = 0, y = 1; i < x; i++, y++) {
            if (e.getActionCommand().equals("Bookmark" + y)) {
                if (check()) {
                    count = count + 1;
                }
                now = current;
                current = y - 1;
                set();
                ((JButton) e.getSource()).setEnabled(false);
                current = now;
            }
        }

        if (e.getActionCommand().equals("Result")) {
            if (check()) {
                count = count + 1;
            }
            JOptionPane.showMessageDialog(this, "Correct answers = " + count);
            saveScore();
            System.exit(0);
        }
    }

    void set() {
        if (current < questions.size()) {
            jb[4].setSelected(true);
            Question q = questions.get(current);
            l.setText("Que" + (current + 1) + ": " + q.questionText);
            jb[0].setText(q.option1);
            jb[1].setText(q.option2);
            jb[2].setText(q.option3);
            jb[3].setText(q.option4);
        }
    }

    boolean check() {
        if (current < questions.size()) {
            Question q = questions.get(current);
            return (jb[q.correctOption - 1].isSelected());
        }
        return false;
    }

    void loadQuestions() {
        try {
            String query = "SELECT * FROM Questions";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                int questionID = rs.getInt("questionID");
                String questionText = rs.getString("questionText");
                String option1 = rs.getString("option1");
                String option2 = rs.getString("option2");
                String option3 = rs.getString("option3");
                String option4 = rs.getString("option4");
                int correctOption = rs.getInt("correctOption");
                questions.add(new Question(questionID, questionText, option1, option2, option3, option4, correctOption));
            }
            if (questions.isEmpty()) {
                JOptionPane.showMessageDialog(this, "No questions found in the database.", "Error", JOptionPane.ERROR_MESSAGE);
                System.exit(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading questions: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    void saveScore() {
        try {
            String query = "UPDATE Students SET score = ? WHERE fullName = ?";
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, count);
            pstmt.setString(2, username);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error saving score: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        new OnlineLicenseExamination("Online License Examination", "testuser"); // Pass a test user for initial testing
    }
}

class Question {
    int questionID;
    String questionText, option1, option2, option3, option4;
    int correctOption;

    Question(int questionID, String questionText, String option1, String option2, String option3, String option4, int correctOption) {
        this.questionID = questionID;
        this.questionText = questionText;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.correctOption = correctOption;
    }
}
