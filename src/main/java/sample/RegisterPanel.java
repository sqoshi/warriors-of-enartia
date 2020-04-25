package sample;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.*;

public class RegisterPanel extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField textField;
    private JPasswordField passwordField;
    private JButton btnNewButton;
    private JPanel contentPane;

    public RegisterPanel(Connection connection) throws IOException, SQLException {
        setBounds(450, 190, 1014, 597);
        setResizable(false);
        contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics grphcs) {
                super.paintComponent(grphcs);
                Graphics2D g2d = (Graphics2D) grphcs;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                        RenderingHints.VALUE_ANTIALIAS_ON);
                GradientPaint gp = new GradientPaint(0, 0,
                        getBackground().brighter().brighter(), 0, getHeight(),
                        getBackground().darker().darker());
                g2d.setPaint(gp);
                g2d.fillRect(0, 0, getWidth(), getHeight());

            }

        };
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel lblNewLabel = new JLabel("Registration");
        lblNewLabel.setForeground(Color.BLACK);
        lblNewLabel.setFont(new Font("Times New Roman", Font.PLAIN, 46));
        lblNewLabel.setBounds(423, 13, 273, 93);
        contentPane.add(lblNewLabel);

        textField = new JTextField();
        textField.setFont(new Font("Tahoma", Font.PLAIN, 32));
        textField.setBounds(481, 170, 281, 68);
        contentPane.add(textField);
        textField.setColumns(10);

        passwordField = new JPasswordField();
        passwordField.setFont(new Font("Tahoma", Font.PLAIN, 32));
        passwordField.setBounds(481, 286, 281, 68);
        contentPane.add(passwordField);

        JLabel lblUsername = new JLabel("Username");
        lblUsername.setBackground(Color.BLACK);
        lblUsername.setForeground(Color.BLACK);
        lblUsername.setFont(new Font("Tahoma", Font.PLAIN, 31));
        lblUsername.setBounds(250, 166, 193, 52);
        contentPane.add(lblUsername);

        JLabel lblPassword = new JLabel("Password");
        lblPassword.setForeground(Color.BLACK);
        lblPassword.setBackground(Color.CYAN);
        lblPassword.setFont(new Font("Tahoma", Font.PLAIN, 31));
        lblPassword.setBounds(250, 286, 193, 52);
        contentPane.add(lblPassword);
        btnNewButton = new JButton("Register");
        btnNewButton.setFont(new Font("Tahoma", Font.PLAIN, 26));
        btnNewButton.setBounds(545, 392, 162, 73);
        btnNewButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String userName = textField.getText();
                String password = passwordField.getText();
                int a = 0, b = 0;
                try {

                    Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/project",
                            "root", "secret");

                    PreparedStatement stmt1 = (PreparedStatement) connection
                            .prepareStatement("select count(*) from users");
                    ResultSet rs1 = stmt1.executeQuery();
                    while (rs1.next()) {
                        a = rs1.getInt("count(*)");
                    }

                    PreparedStatement st = (PreparedStatement) connection
                            .prepareStatement("insert into users(login,password,type) values(?,?,1)");

                    st.setString(1, userName);
                    st.setString(2, password);
                    st.executeUpdate();
                    PreparedStatement stmt2 = (PreparedStatement) connection
                            .prepareStatement("select count(*) from users");
                    ResultSet rs2 = stmt2.executeQuery();
                    while (rs2.next()) {
                        b = (rs2.getInt("count(*)"));
                    }

                    System.out.println(a + " : " + b);
                    if (a != b) {
                        dispose();
                        JOptionPane.showMessageDialog(btnNewButton, "You have successfully registered in");

                    }
                    try {
                        st.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                } catch (SQLException sqlException) {
                    JOptionPane.showMessageDialog(btnNewButton, "Wrong Username ");
                }


            }
        });
        contentPane.add(btnNewButton);

    }
}
