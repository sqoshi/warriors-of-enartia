package sample;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class RankingWindow extends JFrame {

    private static final long serialVersionUID = 1L;
    private JTextField textField;
    private JPasswordField passwordField;
    private JButton btnNewButton;
    private JPanel contentPane;
    private JScrollPane scrollPane;

    public RankingWindow(Connection connection, int classID) throws IOException, SQLException {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        ArrayList<String> weapons = new ArrayList<>();
        ArrayList<Integer> vals = new ArrayList<>();
        setBounds(0, 0, 550, 200);
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

        try {
            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("select * from weapons where class_id=? order by att desc");
            st.setInt(1, classID);
            ResultSet rs2 = st.executeQuery();
            while (rs2.next()) {
                weapons.add(rs2.getString("name") +", Att: "+ rs2.getInt("att"));
                vals.add(rs2.getInt("att"));
            }

            try {
                st.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        JList list = new JList(weapons.toArray());
        scrollPane = new JScrollPane(list);
        scrollPane.setBounds(0, 0, 250, 200);
        contentPane.add(scrollPane);


    }
}
