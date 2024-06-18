import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

 class Home extends JFrame implements ActionListener {
    JButton manageStudentsButton, manageExamsButton;
    Connection conn;

    public Home(Connection conn) {
        this.conn = conn;

        setTitle("Home");
        setSize(600, 400);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(Color.GREEN);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        manageStudentsButton = new JButton("Manage Students");
        manageStudentsButton.addActionListener(this);
        add(manageStudentsButton, gbc);

        gbc.gridy = 1;
        manageExamsButton = new JButton("Manage Exams");
        manageExamsButton.addActionListener(this);
        add(manageExamsButton, gbc);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == manageStudentsButton) {
            new AddStudent(conn);
        } else if (e.getSource() == manageExamsButton) {
            SwingUtilities.invokeLater(ManageExams::new);
        }
    }

    public static void main(String[] args) {
        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/exam", "root", "");
            if (conn != null) {
                new Home(conn);
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
