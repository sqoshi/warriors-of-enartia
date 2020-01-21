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
    private JPanel contentPane;
    private JScrollPane scrollPaneW, scrollPaneA, scrollPaneH, scrollPaneS;

    public RankingWindow(Connection connection, int classID) throws IOException, SQLException {
        setResizable(false);
        ArrayList<String> weapons = new ArrayList<>();
        ArrayList<String> shields = new ArrayList<>();
        ArrayList<String> armors = new ArrayList<>();
        ArrayList<String> helmets = new ArrayList<>();
        setBounds(0, 0, 510, 410);
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
                weapons.add(rs2.getString("name") + ", Att: " + rs2.getInt("att"));
            }
            st = (PreparedStatement) connection
                    .prepareStatement("select * from helmets where class_id=? order by def desc");
            st.setInt(1, classID);
            rs2 = st.executeQuery();
            while (rs2.next()) {
                helmets.add(rs2.getString("name") + ", Def: " + rs2.getInt("def"));
            }
            st = (PreparedStatement) connection
                    .prepareStatement("select * from armors where class_id=? order by def desc");
            st.setInt(1, classID);
            rs2 = st.executeQuery();
            while (rs2.next()) {
                armors.add(rs2.getString("name") + ", Def: " + rs2.getInt("def"));
            }
            st = (PreparedStatement) connection
                    .prepareStatement("select * from shields where class_id=? order by def desc");
            st.setInt(1, classID);
            rs2 = st.executeQuery();
            while (rs2.next()) {
                shields.add(rs2.getString("name") + ", Def: " + rs2.getInt("def"));
            }

            try {
                st.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        JList list = new JList(armors.toArray());
        scrollPaneA = new JScrollPane(list);
        scrollPaneA.setBounds(0, 210, 250, 200);
        contentPane.add(scrollPaneA);

        JList list1 = new JList(shields.toArray());
        scrollPaneS = new JScrollPane(list1);
        scrollPaneS.setBounds(260, 0, 250, 200);
        contentPane.add(scrollPaneS);

        JList list2 = new JList(helmets.toArray());
        scrollPaneH = new JScrollPane(list2);
        scrollPaneH.setBounds(260, 210, 250, 200);
        contentPane.add(scrollPaneH);

        JList list3 = new JList(weapons.toArray());
        scrollPaneW = new JScrollPane(list3);
        scrollPaneW.setBounds(0, 0, 250, 200);
        contentPane.add(scrollPaneW);


    }
}
