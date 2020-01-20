package sample;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.Random;

public class UserHome extends JFrame {

    private static final long serialVersionUID = 1L;
    int dungeonTime;
    boolean ind;
    int gold;
    int hero_id;
    int user_id;
    int classOf_id;
    String newHeroClass;
    String class_name;
    String shield_name;
    String weapon_name;
    String armor_name;
    String helmet_name;
    int helmet_id;
    int armor_id;
    int shield_id;
    int weapon_id;
    int h_val;
    int a_val;
    int s_val;
    int w_val;
    JLabel attLabel;
    JLabel defLabel;
    JLabel shiLabel;
    JLabel weapLabel;
    JLabel armLabel;
    JLabel helLabel;
    JLabel dungeonTimeL;
    int heroes_Attack;
    int heroes_defence;
    private JPanel contentPane;
    private JButton btn;
    private JTextField textField;
    private JScrollPane scrollPane;

    public UserHome(Connection connection, String userName, String password) throws IOException {
        int count = 0;


        ArrayList<String> dungs = new ArrayList<>();
        ArrayList<Integer> dungsAttReq = new ArrayList<>();
        ArrayList<Integer> dungsDefReq = new ArrayList<>();
        ArrayList<String> shop = new ArrayList<>();
        ArrayList<Integer> shopPrices = new ArrayList<>();
        ArrayList<Integer> shopVal = new ArrayList<>();
        try {
            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("Select id from users where login=? and password=?");
            st.setString(1, userName);
            st.setString(2, password);
            ResultSet rs = st.executeQuery();
            //pobranie id usera
            while (rs.next()) {
                setUser_id(rs.getInt("id"));
                System.out.println("user_id:" + getUser_id());
            }


            st = (PreparedStatement) connection
                    .prepareStatement("select * from heroes where user_id = ?");
            st.setInt(1, getUser_id());
            //pobraeni id classy, herosa
            rs = st.executeQuery();
            while (rs.next()) {
                setGold(rs.getInt("gold"));
                setHero_id(rs.getInt("id"));//heores id
                setClassOf_id(rs.getInt("class_id"));
                System.out.println("class_id: " + getClassOf_id() + " | heroes_id: " + getHero_id() + " | GOLD: " + getGold());
            }

            st = (PreparedStatement) connection
                    .prepareStatement("select * from equipment where heroes_id = ?");
            st.setInt(1, hero_id);
            rs = st.executeQuery();
            while (rs.next()) {
                setWeapon_id(rs.getInt("weapon_id"));
                setArmor_id(rs.getInt("armor_id"));
                setHelmet_id(rs.getInt("helmet_id"));
                setShield_id(rs.getInt("shield_id"));
                System.out.println("shield = " + getShield_id() + ", helmet = " + getHelmet_id() + ", armor = " + getArmor_id() + ", weapon = " + getWeapon_id());
            }

            //getiing items
            st = (PreparedStatement) connection
                    .prepareStatement("select name,def from helmets where id = ?");
            st.setInt(1, getHelmet_id());
            rs = st.executeQuery();
            while (rs.next()) {
                setH_val(rs.getInt("def"));
                setHelmet_name(rs.getString("name"));
                System.out.println("helmet_name: " + getHelmet_name());
            }
            st = (PreparedStatement) connection
                    .prepareStatement("select name,def from armors where id = ?");
            st.setInt(1, getArmor_id());
            rs = st.executeQuery();
            while (rs.next()) {
                setA_val(rs.getInt("def"));
                setArmor_name(rs.getString("name"));
                System.out.println("armor_name: " + getArmor_name());
            }

            st = (PreparedStatement) connection
                    .prepareStatement("select name,att from weapons where id = ?");
            st.setInt(1, getWeapon_id());
            rs = st.executeQuery();
            while (rs.next()) {
                setW_val(rs.getInt("att"));
                setWeapon_name(rs.getString("name"));
                System.out.println("weapon_name: " + getWeapon_name());
            }
            st = (PreparedStatement) connection
                    .prepareStatement("select name,def from shields where id = ?");
            st.setInt(1, getShield_id());
            rs = st.executeQuery();
            while (rs.next()) {
                setS_val(rs.getInt("def"));
                setShield_name(rs.getString("name"));
                System.out.println("armor_name: " + getShield_name());
            }

            st = (PreparedStatement) connection
                    .prepareStatement("Select count(*) from heroes where user_id=?");
            st.setInt(1, getUser_id());
            rs = st.executeQuery();
            while (rs.next()) {
                count = rs.getInt("count(*)");
                System.out.println("Has user heroes?: " + count);
            }
            st = (PreparedStatement) connection
                    .prepareStatement("Select name,att,def from dungeons");
            rs = st.executeQuery();
            while (rs.next()) {
                dungsDefReq.add(rs.getInt("def"));
                dungsAttReq.add(rs.getInt("att"));
                dungs.add(rs.getString("name"));
                System.out.println("Dung: " + rs.getString("name") + "A: " + rs.getInt("att") + "D: " + rs.getInt("def"));
            }
            st = (PreparedStatement) connection
                    .prepareStatement("(Select id,name,att as val from weapons where class_id = ? and name  NOT LIKE '%Standard%' ORDER BY RAND() limit 1 )union " +
                            "(Select id,name,def as val from armors where class_id = ? and name  NOT LIKE '%Standard%' ORDER BY RAND() limit 1 )union " +
                            "(Select id,name,def as val from helmets where class_id = ? and name  NOT LIKE '%Standard%' ORDER BY RAND() limit 1) union " +
                            "(Select id,name,def as val from shields where class_id = ? and name  NOT LIKE '%Standard%' ORDER BY RAND() limit 1) ");
            st.setInt(1, getClassOf_id());
            st.setInt(2, getClassOf_id());
            st.setInt(3, getClassOf_id());
            st.setInt(4, getClassOf_id());
            rs = st.executeQuery();
            while (rs.next()) {
                shopVal.add(rs.getInt("val"));
                shop.add(rs.getString("name"));
                System.out.println("ShopItem: " + rs.getString("name"));
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

        dungeonTimeL = new JLabel("");
        dungeonTimeL.setBounds(50, 250, 250, 100);
        dungeonTimeL.setFont(new Font("Times New Roman", Font.PLAIN, 10));
        dungeonTimeL.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(dungeonTimeL);

        helLabel = new JLabel(getHelmet_name());
        helLabel.setBounds(770, 100, 200, 100);
        helLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(helLabel);

        armLabel = new JLabel(getArmor_name());
        armLabel.setBounds(770, 200, 200, 100);
        armLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(armLabel);

        weapLabel = new JLabel(getWeapon_name());
        weapLabel.setBounds(770, 300, 200, 100);
        weapLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(weapLabel);


        shiLabel = new JLabel(getShield_name());
        shiLabel.setBounds(770, 400, 200, 100);
        shiLabel.setHorizontalAlignment(JLabel.CENTER);
        contentPane.add(shiLabel);

        if (count > 0) {
            JLabel priceLabel = new JLabel("Price: ");
            priceLabel.setBounds(50, 550, 300, 100);
            contentPane.add(priceLabel);

            JLabel reqLabel = new JLabel("Requirments: ");
            reqLabel.setBounds(50, 0, 300, 100);
            contentPane.add(reqLabel);


            JLabel goldLabel = new JLabel("Gold: " + gold);
            goldLabel.setBounds(600, 550, 200, 100);
            goldLabel.setHorizontalAlignment(JLabel.CENTER);
            contentPane.add(goldLabel);
            Timer timer = new Timer(2000, event -> {
                try {
                    PreparedStatement st = (PreparedStatement) connection
                            .prepareStatement("select * from heroes where user_id = ? and id = ?");
                    st.setInt(1, getUser_id());
                    st.setInt(2, getHero_id());
                    //pobraeni id classy, herosa
                    ResultSet rs = st.executeQuery();
                    while (rs.next()) {
                        setGold(rs.getInt("gold"));
                    }
                    try {
                        st.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
                goldLabel.setText("Gold: " + getGold());
            });
            timer.setRepeats(true);
            timer.start();


            updateLabels(connection);
            for (String x : shop
            ) {
                shopPrices.add(new Random().nextInt(1200));
            }
            attLabel = new JLabel("Attack Damage: " + heroes_Attack);
            attLabel.setBounds(450, 0, 200, 100);
            attLabel.setHorizontalAlignment(JLabel.CENTER);
            contentPane.add(attLabel);

            defLabel = new JLabel("Defence Points: " + heroes_defence);
            defLabel.setBounds(650, 0, 200, 100);
            defLabel.setHorizontalAlignment(JLabel.CENTER);
            contentPane.add(defLabel);
            JList list = new JList(dungs.toArray());
            scrollPane = new JScrollPane(list);
            scrollPane.setBounds(100, 100, 250, 150);
            list.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    JList list = (JList) evt.getSource();
                    if (evt.getClickCount() == 1) {
                        reqLabel.setText("Requirments : \n Att: " + dungsAttReq.get(list.locationToIndex(evt.getPoint())) + " Def: "
                                + dungsDefReq.get(list.locationToIndex(evt.getPoint())));
                    }
                    if (evt.getClickCount() == 2) {
                        int index = list.locationToIndex(evt.getPoint());
                        if (!isInd()) {
                            if (dungsAttReq.get(index) <= getHeroes_Attack()
                                    && dungsDefReq.get(index) <= getHeroes_defence()) {
                                System.out.println("You send ur hero to this dunegon");
                                JOptionPane.showMessageDialog(btn, "Heroes send to dungeon.");
                                setInd(true);
                                int delay = 8000 + new Random().nextInt(dungsAttReq.get(index) * dungsDefReq.get(index));
                                setDungeonTime(delay);
                                System.out.println(getDungeonTime());
                                Timer timer = new Timer(delay, event -> {
                                    try {
                                        PreparedStatement st = (PreparedStatement) connection
                                                .prepareStatement("update heroes set gold = gold + FLOOR(RAND()*?+15) where user_id =? and id=?");
                                        st.setInt(1, getHeroes_Attack() + getHeroes_defence());
                                        st.setInt(2, getUser_id());
                                        st.setInt(3, getHero_id());
                                        st.executeUpdate();
                                        setInd(false);
                                        if (new Random().nextInt(10) >= 5) {
                                            JOptionPane.showMessageDialog(btn, "Some thieves almost kill you in dark forest and steal some of your gold. ");
                                        }
                                        System.out.println("You finished dunegon!");
                                        JOptionPane.showMessageDialog(btn, "You have succesfully finished dungeon.");

                                        try {
                                            st.close();
                                        } catch (SQLException ex) {
                                            ex.printStackTrace();
                                        }
                                    } catch (SQLException sqlException) {
                                        sqlException.printStackTrace();
                                    }

                                });
                                timer.setRepeats(false);
                                timer.start();

                            } else {
                                JOptionPane.showMessageDialog(btn, "You can not send your heroes on this dungeon check requirements.");
                                System.out.println("you cant send hero to this dungon");
                            }
                        } else {
                            JOptionPane.showMessageDialog(btn, "Hero is already in dungeon, you need to wait.");
                            System.out.println("Hero is Already in Dungeon already in dung");
                        }
                    }
                }
            });
            contentPane.add(scrollPane);

            JList list1 = new JList(shop.toArray());
            scrollPane = new JScrollPane(list1);
            scrollPane.setBounds(100, 400, 250, 150);
            list1.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent evt) {
                    JList list1 = (JList) evt.getSource();

                    if (evt.getClickCount() == 1) {
                        System.out.println();
                        priceLabel.setText("Price:" + shopPrices.get(list1.locationToIndex(evt.getPoint())) + ", Val:" + shopVal.get(list1.locationToIndex(evt.getPoint())));
                    }
                    if (evt.getClickCount() == 2) {
                        int index = list1.locationToIndex(evt.getPoint());
                        String is = "";
                        if (getGold() > shopPrices.get(list1.locationToIndex(evt.getPoint()))) {
                            System.out.println(index + " " + shop.get(index));
                            String testString = shop.get(index);
                            String[] parts = testString.split(" ");
                            String lastWord = parts[parts.length - 1];
                            System.out.println(lastWord);
                            try {
                                PreparedStatement st = (PreparedStatement) connection
                                        .prepareStatement("select * from ? where name =?");
                                if (lastWord.equals("Shield") || lastWord.equals("Book") || lastWord.equals("Quiver")) {
                                    st = (PreparedStatement) connection
                                            .prepareStatement("select * from shields where name =?");
                                    st.setString(1, shop.get(index));
                                    is = "s";
                                } else if (lastWord.equals("Rod") || lastWord.equals("Bow") || lastWord.equals("Sword")) {
                                    st = (PreparedStatement) connection
                                            .prepareStatement("select * from weapons where name =?");
                                    st.setString(1, shop.get(index));
                                    is = "w";
                                } else if (lastWord.equals("Coverlet") || lastWord.equals("Robe") || lastWord.equals("Armor")) {
                                    st = (PreparedStatement) connection
                                            .prepareStatement("select * from armors where name =?");
                                    st.setString(1, shop.get(index));
                                    is = "a";
                                } else if (lastWord.equals("Band") || lastWord.equals("Hat") || lastWord.equals("Helmet")) {
                                    st = (PreparedStatement) connection
                                            .prepareStatement("select * from helmets where name =?");
                                    st.setString(1, shop.get(index));
                                    is = "h";
                                } else {
                                    System.out.println("ERROR");
                                    return;
                                }
                                ResultSet rs = st.executeQuery();
                                int x = -1;
                                while (rs.next()) {
                                    x = rs.getInt("id");
                                }
                                System.out.println("Item is: " + is);
                                if (is.equals("w")) {
                                    st = (PreparedStatement) connection
                                            .prepareStatement("update equipment set weapon_id = ? where heroes_id = ?");
                                }
                                if (is.equals("a")) {
                                    st = (PreparedStatement) connection
                                            .prepareStatement("update equipment set armor_id = ? where heroes_id = ?");
                                }
                                if (is.equals("h")) {
                                    st = (PreparedStatement) connection
                                            .prepareStatement("update equipment set helmet_id = ? where heroes_id = ?");
                                }
                                if (is.equals("s")) {
                                    st = (PreparedStatement) connection
                                            .prepareStatement("update equipment set shield_id = ? where heroes_id = ?");
                                }
                                st.setInt(1, x);
                                st.setInt(2, getHero_id());
                                st.executeUpdate();
                                st = (PreparedStatement) connection
                                        .prepareStatement("update heroes set gold = gold - ? where id =?");
                                st.setInt(1, shopPrices.get(list1.locationToIndex(evt.getPoint())));
                                st.setInt(2, getHero_id());
                                st.executeUpdate();
                                try {
                                    st.close();
                                } catch (SQLException ex) {
                                    ex.printStackTrace();
                                }
                            } catch (SQLException sqlException) {
                                sqlException.printStackTrace();
                            }
                        } else {
                            System.out.println("Not enough Gold");
                            JOptionPane.showMessageDialog(btn, "You have not got enough gold to buy this item. ");

                        }
                    }
                }
            });
            File file = null;
            contentPane.add(scrollPane);
            if (getClass_name(connection).equals("Mage")) {
                file = new File("/home/piotr/Documents/database-project/src/jpg/mage.jpeg");
            }
            if ((getClass_name(connection).equals("Warrior"))) {
                file = new File("/home/piotr/Documents/database-project/src/jpg/warrior.jpg");
            }
            if (getClass_name(connection).equals("Archer")) {
                file = new File("/home/piotr/Documents/database-project/src/jpg/archer.jpg");
            }
            BufferedImage myPicture = ImageIO.read(file);
            JLabel picLabel = new JLabel(
                    new ImageIcon(
                            myPicture.getScaledInstance(
                                    300, 400, Image.SCALE_DEFAULT)));
            picLabel.setBounds(450, 100, 300, 400);
            contentPane.add(picLabel);

            JButton rankBtn = new JButton("Items Ranking");
            rankBtn.setBounds(825, 540, 140, 80);
            rankBtn.setFont(new Font("Tahoma", Font.PLAIN, 12));
            rankBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    try {
                        RankingWindow rw = new RankingWindow(connection, classOf_id);
                        rw.setVisible(true);
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }
                }
            });
            contentPane.add(rankBtn);
        }


        if (count <= 0) {
            JLabel nickLabel = new JLabel("Nickname: ");
            textField = new JTextField();
            textField.setFont(new Font("Tahoma", Font.PLAIN, 32));
            nickLabel.setFont(new Font("Tahoma", Font.PLAIN, 16));
            textField.setBounds(181, 500, 281, 68);
            nickLabel.setBounds(80, 500, 100, 68);
            contentPane.add(textField);
            contentPane.add(nickLabel);
            textField.setColumns(10);

            btn = new JButton("Create Character ");
            btn.setFont(new Font("Tahoma", Font.PLAIN, 10));
            btn.setBounds(650, 550, 162, 73);
            btn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String heroesName = textField.getText();
                    int s = -5;
                    try {

                        Connection connection = (Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/project",
                                "root", "piotrek22");
                        PreparedStatement st = (PreparedStatement) connection
                                .prepareStatement("select id from classes where name = ?");
                        st.setString(1, getNewHeroClass());
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
                            st.setInt(3, getUser_id());
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

            BufferedImage magImg = ImageIO.read(new File("/home/piotr/Documents/database-project/src/jpg/mage.jpeg"));
            JLabel magImgLabel = new JLabel(
                    new ImageIcon(
                            magImg.getScaledInstance(
                                    200, 300, Image.SCALE_DEFAULT)));
            magImgLabel.setBounds(150, 50, 200, 300);
            contentPane.add(magImgLabel);
            BufferedImage warImg = ImageIO.read(new File("/home/piotr/Documents/database-project/src/jpg/warrior.jpg"));
            JLabel warImgLabel = new JLabel(
                    new ImageIcon(
                            warImg.getScaledInstance(
                                    200, 300, Image.SCALE_DEFAULT)));
            warImgLabel.setBounds(400, 50, 200, 300);
            contentPane.add(warImgLabel);
            BufferedImage archImg = ImageIO.read(new File("/home/piotr/Documents/database-project/src/jpg/archer.jpg"));
            JLabel archImgLabel = new JLabel(
                    new ImageIcon(
                            archImg.getScaledInstance(
                                    200, 300, Image.SCALE_DEFAULT)));
            archImgLabel.setBounds(650, 50, 200, 300);
            contentPane.add(archImgLabel);


            JButton class_button1 = new JButton("Mage");
            JButton class_button2 = new JButton("Warrior");
            JButton class_button3 = new JButton("Archer");
            class_button1.setBounds(200, 400, 100, 60);
            class_button1.setFont(new Font("Tahoma", Font.PLAIN, 16));
            class_button2.setBounds(450, 400, 100, 60);
            class_button2.setFont(new Font("Tahoma", Font.PLAIN, 16));
            class_button3.setBounds(700, 400, 100, 60);
            class_button3.setFont(new Font("Tahoma", Font.PLAIN, 16));
            class_button1.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setNewHeroClass("Mage");
                    JOptionPane.showMessageDialog(btn, "You choosed " + getNewHeroClass());
                }
            });
            class_button2.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setNewHeroClass("Warrior");
                    JOptionPane.showMessageDialog(btn, "You choosed " + getNewHeroClass());
                }
            });
            class_button3.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setNewHeroClass("Archer");
                    JOptionPane.showMessageDialog(btn, "You choosed " + getNewHeroClass());
                }
            });
            contentPane.add(class_button1);
            contentPane.add(class_button2);
            contentPane.add(class_button3);


        }

    }

    public int getHeroes_Attack() {
        return heroes_Attack;
    }

    public void setHeroes_Attack(int heroes_Attack) {
        this.heroes_Attack = heroes_Attack;
    }

    public int getHeroes_defence() {
        return heroes_defence;
    }

    public void setHeroes_defence(int heroes_defence) {
        this.heroes_defence = heroes_defence;
    }

    public int getDungeonTime() {
        return dungeonTime;
    }

    public void setDungeonTime(int dungeonTime) {
        this.dungeonTime = dungeonTime;
    }

    public String getNewHeroClass() {
        return newHeroClass;
    }

    public void setNewHeroClass(String newHeroClass) {
        this.newHeroClass = newHeroClass;
    }

    public void setClass_name(String class_name) {
        this.class_name = class_name;
    }

    public String getClass_name(Connection connection) {
        try {
            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("select * from classes where id = ?");
            st.setInt(1, getClassOf_id());
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                setClass_name(rs.getString("name"));
            }
            try {
                st.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return class_name;
    }


    public void updateLabels(Connection connection) {
        int del = 500;
        Timer timer = new Timer(del, event -> {

            try {
                PreparedStatement st = (PreparedStatement) connection
                        .prepareStatement("select * from equipment where heroes_id = ?");
                st.setInt(1, getHero_id());
                ResultSet rs = st.executeQuery();
                while (rs.next()) {
                    setWeapon_id(rs.getInt("weapon_id"));
                    setArmor_id(rs.getInt("armor_id"));
                    setShield_id(rs.getInt("shield_id"));
                    setHelmet_id(rs.getInt("helmet_id"));
                }

                st = (PreparedStatement) connection
                        .prepareStatement("select * from weapons where id = ?");
                st.setInt(1, getWeapon_id());
                rs = st.executeQuery();
                while (rs.next()) {
                    setW_val(rs.getInt("att"));
                    setWeapon_name(rs.getString("name"));
                }
                st = (PreparedStatement) connection
                        .prepareStatement("select * from armors where id = ?");
                st.setInt(1, getArmor_id());
                rs = st.executeQuery();
                while (rs.next()) {
                    setA_val(rs.getInt("def"));
                    setArmor_name(rs.getString("name"));
                }
                st = (PreparedStatement) connection
                        .prepareStatement("select * from shields where id = ?");
                st.setInt(1, getShield_id());
                rs = st.executeQuery();
                while (rs.next()) {
                    setS_val(rs.getInt("def"));
                    setShield_name(rs.getString("name"));
                }
                st = (PreparedStatement) connection
                        .prepareStatement("select * from helmets where id = ?");
                st.setInt(1, getHelmet_id());
                rs = st.executeQuery();
                while (rs.next()) {
                    setH_val(rs.getInt("def"));
                    setHelmet_name(rs.getString("name"));
                }
                setHeroes_defence(getH_val() + getS_val() + getA_val());
                setHeroes_Attack(getW_val());
                try {
                    st.close();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            } catch (SQLException sqlException) {
                sqlException.printStackTrace();
            }
            if (isInd()) {
                if (getDungeonTime() > 0)
                    setDungeonTime(getDungeonTime() - del);
                dungeonTimeL.setText("Heroes will be back in " + ((getDungeonTime() / 10 / 10 / 10) + " seconds."));
            } else
                dungeonTimeL.setText("");
            attLabel.setText("Att: " + getHeroes_Attack());
            defLabel.setText("Def: " + getHeroes_defence());
            weapLabel.setText(getWeapon_name() + ", Att: " + getW_val());
            helLabel.setText(getHelmet_name() + ", Def:" + getH_val());
            armLabel.setText(getArmor_name() + ", Def:" + getA_val());
            shiLabel.setText(getShield_name() + ", Def:" + getS_val());
        });
        timer.setRepeats(true);
        timer.start();
    }

    public int getH_val() {
        return h_val;
    }

    public void setH_val(int h_val) {
        this.h_val = h_val;
    }

    public int getA_val() {
        return a_val;
    }

    public void setA_val(int a_val) {
        this.a_val = a_val;
    }

    public int getS_val() {
        return s_val;
    }

    public void setS_val(int s_val) {
        this.s_val = s_val;
    }

    public int getW_val() {
        return w_val;
    }

    public void setW_val(int w_val) {
        this.w_val = w_val;
    }

    public String getShield_name() {
        return shield_name;
    }

    public void setShield_name(String shield_name) {
        this.shield_name = shield_name;
    }

    public String getWeapon_name() {
        return weapon_name;
    }

    public void setWeapon_name(String weapon_name) {
        this.weapon_name = weapon_name;
    }

    public String getArmor_name() {
        return armor_name;
    }

    public void setArmor_name(String armor_name) {
        this.armor_name = armor_name;
    }

    public String getHelmet_name() {
        return helmet_name;
    }

    public void setHelmet_name(String helmet_name) {
        this.helmet_name = helmet_name;
    }

    public int getHelmet_id() {
        return helmet_id;
    }

    public void setHelmet_id(int helmet_id) {
        this.helmet_id = helmet_id;
    }

    public int getArmor_id() {
        return armor_id;
    }

    public void setArmor_id(int armor_id) {
        this.armor_id = armor_id;
    }

    public int getShield_id() {
        return shield_id;
    }

    public void setShield_id(int shield_id) {
        this.shield_id = shield_id;
    }

    public int getWeapon_id() {
        return weapon_id;
    }

    public void setWeapon_id(int weapon_id) {
        this.weapon_id = weapon_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getClassOf_id() {
        return classOf_id;
    }

    public void setClassOf_id(int classOf_id) {
        this.classOf_id = classOf_id;
    }

    public int getHero_id() {
        return hero_id;
    }

    public void setHero_id(int hero_id) {
        this.hero_id = hero_id;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public boolean isInd() {
        return ind;
    }

    public void setInd(boolean ind) {
        this.ind = ind;
    }


}
