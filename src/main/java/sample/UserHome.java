package sample;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.*;

public class UserHome extends JFrame {
    private static final long serialVersionUID = 1L;
    private JPanel contentPane;
    private JButton btn;
    private JTextField textField;
    private JTextField passwordField;

    public UserHome(Connection connection, String userName, String password) throws IOException {
        int cl_id = -98;
        int count = 0;
        int usr_id = -98;
        int hero_id = -98;

        String sh_name = "";
        String weap_name = "";
        String arm_name = "";
        String hel_name = "";

        int hel_id = -98;
        int arm_id = -98;
        int sh_id = -98;
        int weap_id = -98;

        try {
            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("Select id from users where login=? and password=?");
            st.setString(1, userName);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();
            //pobranie id usera
            while (rs.next()) {
                usr_id = rs.getInt("id");
                System.out.println("user_id:" + usr_id);
            }


            st = (PreparedStatement) connection
                    .prepareStatement("select * from heroes where user_id = ?");
            st.setInt(1, usr_id);
            //pobraeni id classy, herosa
            rs = st.executeQuery();
            while (rs.next()) {
                hero_id = rs.getInt("id");//heores id
                cl_id = rs.getInt("class_id");
                System.out.println("class_id: " + cl_id + " | heroes_id: " + hero_id);
            }


            st = (PreparedStatement) connection
                    .prepareStatement("select * from equipment where heroes_id = ?");
            st.setInt(1, hero_id);
            rs = st.executeQuery();
            while (rs.next()) {
                weap_id = rs.getInt("weapon_id");
                arm_id = rs.getInt("armor_id");
                hel_id = rs.getInt("helmet_id");
                sh_id = rs.getInt("shield_id");
                System.out.println("shield = " + sh_id + ", helmet = " + hel_id + ", armor = " + arm_id + ", weapon = " + weap_id);
            }
            //getiing items
            st = (PreparedStatement) connection
                    .prepareStatement("select name from helmets where id = ?");
            st.setInt(1, hel_id);
            rs = st.executeQuery();
            while (rs.next()) {
                hel_name = rs.getString("name");
                System.out.println("helmet_name: " + hel_name);
            }
            st = (PreparedStatement) connection
                    .prepareStatement("select name from armors where id = ?");
            st.setInt(1, arm_id);
            rs = st.executeQuery();
            while (rs.next()) {
                arm_name = rs.getString("name");
                System.out.println("armor_name: " + arm_name);
            }
            st = (PreparedStatement) connection
                    .prepareStatement("select name from weapons where id = ?");
            st.setInt(1, weap_id);
            rs = st.executeQuery();
            while (rs.next()) {
                weap_name = rs.getString("name");
                System.out.println("weapon_name: " + weap_name);
            }
            st = (PreparedStatement) connection
                    .prepareStatement("select name from shields where id = ?");
            st.setInt(1, sh_id);
            rs = st.executeQuery();
            while (rs.next()) {
                sh_name = rs.getString("name");
                System.out.println("armor_name: " + sh_name);
            }

            st = (PreparedStatement) connection
                    .prepareStatement("Select count(*) from heroes where user_id=?");
            st.setInt(1, usr_id);
            rs = st.executeQuery();
            //pobranie id usera
            while (rs.next()) {
                count = rs.getInt("count(*)");
                System.out.println("Has user heroes?: " + count);
            }


            try {
                st.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }


        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(450, 190, 1000, 700);
        setResizable(false);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        contentPane.setLayout(null);

        JLabel helLabel = new JLabel(hel_name);
        helLabel.setBounds(800, 100, 200, 100);
        helLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(helLabel);


        JLabel armLabel = new JLabel(arm_name);
        armLabel.setBounds(800, 200, 200, 100);
        armLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(armLabel);

        JLabel weapLabel = new JLabel(weap_name);
        weapLabel.setBounds(800, 300, 200, 100);
        weapLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(weapLabel);


        JLabel shiLabel = new JLabel(sh_name);
        shiLabel.setBounds(800, 400, 200, 150);
        shiLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(shiLabel);
        int finalUsr_id = usr_id;


        if (count <= 0) {
            textField = new JTextField();
            textField.setFont(new Font("Tahoma", Font.PLAIN, 32));
            textField.setBounds(481, 170, 281, 68);
            contentPane.add(textField);
            textField.setColumns(10);

            passwordField = new JTextField();
            passwordField.setFont(new Font("Tahoma", Font.PLAIN, 32));
            passwordField.setBounds(481, 286, 281, 68);
            contentPane.add(passwordField);
            btn = new JButton("Create Character ");
            btn.setFont(new Font("Tahoma", Font.PLAIN, 16));
            btn.setBounds(100, 100, 162, 73);
            btn.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    String heroesName = textField.getText();
                    int s = -5;
                    String className = passwordField.getText();
                    try {

                        Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/project",
                                "root", "piotrek22");
                        PreparedStatement st = (PreparedStatement) connection
                                .prepareStatement("select id from classes where name = ?");
                        st.setString(1, className);
                        ResultSet rs = st.executeQuery();
                        //pobranie id usera
                        while (rs.next()) {
                            s = rs.getInt("id");
                        }
                        System.out.println(s);
                        if (s < 0)
                            JOptionPane.showMessageDialog(btn, "Wrong Class Name ");
                        else {
                            st = (PreparedStatement) connection
                                    .prepareStatement("insert into heroes(class_id,name,user_id) values(?,?,?)");

                            st.setInt(1, s);
                            st.setString(2, heroesName);
                            st.setInt(3, finalUsr_id);
                            st.executeUpdate();
                            dispose();
                            JOptionPane.showMessageDialog(btn, "You need to relog! ");
                            UserLogin frame = new UserLogin();
                            frame.setVisible(true);
                        }

                    } catch (SQLException sqlException) {
                        sqlException.printStackTrace();
                        JOptionPane.showMessageDialog(btn, "Name Of character has already been taken ");
                    }


                }
            });
            contentPane.add(btn);
        }


       /* BufferedImage myPicture = ImageIO.read(new File("/home/piotr/Documents/database-project/src/jpg/warrior.jpg"));
        JLabel picLabel = new JLabel(
                new ImageIcon(
                        myPicture.getScaledInstance(
                                300,500,Image.SCALE_DEFAULT)));
        picLabel.setBounds(500, 100, 300, 500);
        contentPane.add(picLabel);*/


    }

}
