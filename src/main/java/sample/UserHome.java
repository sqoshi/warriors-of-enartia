package sample;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserHome extends JFrame {
    private static final long serialVersionUID = 1L;
    private JTextField textField;
    private JPanel contentPane;

    public UserHome(Connection connection, String userName, String password) throws IOException {
        int cl_id = -98;
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
        helLabel.setBounds(600, 100, 100, 100);
        helLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(helLabel);


        JLabel armLabel = new JLabel(arm_name);
        armLabel.setBounds(600, 300, 100, 100);
        armLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(armLabel);

        JLabel weapLabel = new JLabel( weap_name);
        weapLabel.setBounds(400, 300, 100, 100);
        weapLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(weapLabel);


        JLabel shiLabel = new JLabel(sh_name);
        shiLabel.setBounds(800, 300, 150, 150);
        shiLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(shiLabel);




        BufferedImage myPicture = ImageIO.read(new File("/home/piotr/Documents/database-project/src/jpg/warrior.jpg"));
        JLabel picLabel = new JLabel(
                new ImageIcon(
                        myPicture.getScaledInstance(
                                300,500,Image.SCALE_DEFAULT)));
        picLabel.setBounds(500, 100, 300, 500);
        contentPane.add(picLabel);


    }

}
