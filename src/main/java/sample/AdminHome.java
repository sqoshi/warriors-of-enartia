package sample;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class AdminHome extends JFrame {
    private static final long serialVersionUID = 1L;
    String ClassSetName;
    int classID;
    int user_id;
    String heroes_name;
    int heroes_id;
    int heroes_gold;
    int index;
    private JPanel contentPane;
    private JButton banbtn1, banbtn, goldGiveBtn, promoteToAdminBtn, insertClassSetBtn, inertWeaponSet, removeItembtn;
    private JTextField textFieldNick, textFieldUID;
    private JScrollPane scrollPane;

    public AdminHome(Connection connection, String userName, String password) throws IOException {
        ArrayList<String> heroesInfo = new ArrayList<>();
        ArrayList<Integer> users_ids = new ArrayList<>();
        ArrayList<String> heroes_names = new ArrayList<>();
        ArrayList<Integer> heroes_ids = new ArrayList<>();
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
        try {
            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("select * from heroes");
            ResultSet rs2 = st.executeQuery();
            while (rs2.next()) {
                heroesInfo.add(rs2.getString("user_id") + ", " + rs2.getString("name") + ", " + rs2.getInt("gold"));
                heroes_ids.add(rs2.getInt("id"));
                users_ids.add(rs2.getInt("user_id"));
                heroes_names.add(rs2.getString("name"));
                setHeroes_gold(rs2.getInt("gold"));
            }

            try {
                st.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        DefaultListModel model = new DefaultListModel();
        model.addAll(heroesInfo);
        JList list = new JList(model);
        scrollPane = new JScrollPane(list);
        scrollPane.setBounds(100, 100, 300, 400);

        list.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                JList list = (JList) evt.getSource();
                if (evt.getClickCount() == 1) {
                    setIndex(list.locationToIndex(evt.getPoint()));
                    setHeroes_name(heroes_names.get(getIndex()));
                    setHeroes_id(heroes_ids.get(getIndex()));
                    setUser_id(users_ids.get(getIndex()));
                }
            }
        });
        contentPane.add(scrollPane);


        textFieldUID = new JTextField();
        textFieldUID.setFont(new Font("Tahoma", Font.PLAIN, 32));
        textFieldUID.setBounds(450, 300, 200, 60);
        contentPane.add(textFieldUID);
        textFieldUID.setColumns(10);

        goldGiveBtn = new JButton("Send Gold");
        goldGiveBtn.setFont(new Font("Tahoma", Font.PLAIN, 12));
        goldGiveBtn.setBounds(720, 300, 120, 60);
        goldGiveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    PreparedStatement st = (PreparedStatement) connection
                            .prepareStatement("update heroes set gold = gold + ? where name = ?");
                    st.setInt(1, Integer.parseInt(textFieldUID.getText()));
                    System.out.println(getHeroes_name());

                    String testString = (String) model.get(getIndex());
                    String[] parts = testString.split(", ");
                    setHeroes_gold(Integer.parseInt(parts[parts.length - 1]));
                    setHeroes_gold(getHeroes_gold() + Integer.parseInt(textFieldUID.getText()));
                    parts[parts.length - 1] = String.valueOf(getHeroes_gold());

                    setHeroes_name(parts[parts.length - 2]);
                    st.setString(2, getHeroes_name());
                    System.out.println(getHeroes_name());
                    st.execute();
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < parts.length; i++) {
                        if (i == parts.length - 1)
                            sb.append(parts[i]);
                        else
                            sb.append(parts[i]).append(", ");
                    }
                    String str = sb.toString();
                    model.set(getIndex(), str);

                    JOptionPane.showMessageDialog(contentPane, "Gold have been sent " + parts[parts.length - 1]);
                    try {
                        st.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }

            }
        });
        contentPane.add(goldGiveBtn);

        promoteToAdminBtn = new JButton("Promote");
        promoteToAdminBtn.setFont(new Font("Tahoma", Font.PLAIN, 12));
        promoteToAdminBtn.setBounds(720, 200, 120, 60);
        promoteToAdminBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    PreparedStatement st = (PreparedStatement) connection
                            .prepareStatement("update users set type = 3 where id = ?");
                    st.setInt(1, getUser_id());
                    st.executeUpdate();
                    model.remove(getIndex());

                    try {
                        st.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
        });
        contentPane.add(promoteToAdminBtn);

        removeItembtn = new JButton("Delete Item");
        removeItembtn.setFont(new Font("Tahoma", Font.PLAIN, 12));
        removeItembtn.setBounds(450, 200, 120, 60);
        removeItembtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (textFieldUID.getText().equals("")) {
                        JOptionPane.showMessageDialog(contentPane, "You need to input name of item to be deleted");
                    }
                    if (isNumeric(textFieldUID.getText())) {
                        JOptionPane.showMessageDialog(contentPane, "Item cant be a number");
                        return;
                    }
                    PreparedStatement st = (PreparedStatement) connection
                            .prepareStatement("delete from shields where name = ?");
                    st.setString(1, textFieldUID.getText());
                    st.execute();
                    st = (PreparedStatement) connection
                            .prepareStatement("delete from weapons where name = ?");
                    st.setString(1, textFieldUID.getText());
                    st.execute();
                    st = (PreparedStatement) connection
                            .prepareStatement("delete from helmets where name = ?");
                    st.setString(1, textFieldUID.getText());
                    st.execute();
                    st = (PreparedStatement) connection
                            .prepareStatement("delete from armors where name = ?");
                    st.setString(1, textFieldUID.getText());
                    st.execute();
                    JOptionPane.showMessageDialog(contentPane, "Item deleted");
                    try {
                        st.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                } catch (SQLException sqlException) {
                    JOptionPane.showMessageDialog(contentPane, "Input name in window");
                    sqlException.printStackTrace();
                }
            }
        });
        contentPane.add(removeItembtn);

        insertClassSetBtn = new JButton("Insert Class Set");
        insertClassSetBtn.setFont(new Font("Tahoma", Font.PLAIN, 10));
        insertClassSetBtn.setBounds(585, 200, 120, 60);
        insertClassSetBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (isNumeric(textFieldUID.getText())) {
                        JOptionPane.showMessageDialog(contentPane, "Name cant be a number");
                        return;
                    }
                    if (textFieldUID.getText().equals("")) {
                        JOptionPane.showMessageDialog(contentPane, "You need to input something in as a name");
                        return;
                    }
                    PreparedStatement st = (PreparedStatement) connection
                            .prepareStatement("call insertClassSet(?,?)");
                    st.setString(1, textFieldUID.getText());
                    st.setString(2, getClassSetName());
                    st.execute();
                    JOptionPane.showMessageDialog(contentPane, "Weapon set named " + textFieldUID.getText() + " has been inserted.");

                    try {
                        st.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                } catch (SQLException sqlException) {
                    JOptionPane.showMessageDialog(contentPane, "Already Inserted");
                    sqlException.printStackTrace();
                }
            }
        });
        contentPane.add(insertClassSetBtn);

        inertWeaponSet = new JButton("Insert Weapon Set");
        inertWeaponSet.setFont(new Font("Tahoma", Font.PLAIN, 10));
        inertWeaponSet.setBounds(585, 100, 120, 60);
        inertWeaponSet.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    if (isNumeric(textFieldUID.getText())) {
                        JOptionPane.showMessageDialog(contentPane, "Name cant be a number");
                        return;
                    }
                    if (textFieldUID.getText().equals("")) {
                        JOptionPane.showMessageDialog(contentPane, "You need to input something in as a name");
                        return;
                    }
                   // Integer.parseInt(textFieldUID.getText());
                    PreparedStatement st = (PreparedStatement) connection
                            .prepareStatement("call insertClassSet(?,?)");
                    st.setString(1, textFieldUID.getText());
                    st.setString(2, getClassSetName());
                    st.execute();
                    JOptionPane.showMessageDialog(contentPane, "Class set named " + textFieldUID.getText() + " has been inserted.");
                    try {
                        st.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }

            }
        });
        contentPane.add(inertWeaponSet);


        banbtn1 = new JButton("Delete Account");
        banbtn1.setFont(new Font("Tahoma", Font.PLAIN, 8));
        banbtn1.setBounds(450, 100, 120, 60);
        banbtn1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    PreparedStatement st = (PreparedStatement) connection
                            .prepareStatement("delete from users where id = ?");
                    st.setInt(1, getUser_id());
                    st.execute();
                    model.remove(getIndex());

                    JOptionPane.showMessageDialog(contentPane, "Deleted user " + getUser_id());

                    try {
                        st.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
        });
        contentPane.add(banbtn1);

        banbtn = new JButton("Delete Heroes");
        banbtn.setFont(new Font("Tahoma", Font.PLAIN, 12));
        banbtn.setBounds(720, 100, 120, 60);
        banbtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    PreparedStatement st = (PreparedStatement) connection
                            .prepareStatement("delete from heroes where name = ?");
                    st.setString(1, getHeroes_name());
                    st.execute();
                    System.out.println(getHeroes_name());
                    System.out.println(getIndex());
                    System.out.println(Arrays.toString(model.toArray()));
                    model.remove(getIndex());
                    System.out.println(Arrays.toString(model.toArray()));
                    JOptionPane.showMessageDialog(contentPane, "Heroes deleted: " + getHeroes_name());

                    try {
                        st.close();
                    } catch (SQLException ex) {
                        ex.printStackTrace();
                    }

                } catch (SQLException sqlException) {
                    sqlException.printStackTrace();
                }
            }
        });
        contentPane.add(banbtn);

        JButton rankBtn = new JButton("Items Ranking");
        rankBtn.setBounds(825, 540, 140, 80);
        rankBtn.setFont(new Font("Tahoma", Font.PLAIN, 12));
        rankBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    RankingWindow rw = new RankingWindow(connection, getClassId());
                    rw.setVisible(true);
                } catch (IOException | SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        contentPane.add(rankBtn);

        JButton class_button1 = new JButton("Mage");
        JButton class_button2 = new JButton("Warrior");
        JButton class_button3 = new JButton("Archer");
        class_button1.setBounds(720, 450, 120, 60);
        class_button1.setFont(new Font("Tahoma", Font.PLAIN, 16));
        class_button2.setBounds(585, 450, 120, 60);
        class_button2.setFont(new Font("Tahoma", Font.PLAIN, 16));
        class_button3.setBounds(450, 450, 120, 60);
        class_button3.setFont(new Font("Tahoma", Font.PLAIN, 16));
        class_button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setClassSetName("Mage");
                setClassId(connection, "Mage");
            }
        });
        class_button2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setClassSetName("Warrior");
                setClassId(connection, "Warrior");
            }
        });
        class_button3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                setClassSetName("Archer");
                setClassId(connection, "Archer");
            }
        });
        contentPane.add(class_button1);
        contentPane.add(class_button2);
        contentPane.add(class_button3);
    }

    public static boolean isNumeric(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public String getClassSetName() {
        return ClassSetName;
    }

    public void setClassSetName(String classSetName) {
        ClassSetName = classSetName;
    }

    public int getClassId() {
        return classID;
    }

    public void setClassId(Connection connection, String className) {
        try {
            PreparedStatement st = (PreparedStatement) connection
                    .prepareStatement("select * from classes where name = ?");
            st.setString(1, className);
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                classID = rs.getInt("id");
            }
            try {
                st.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getHeroes_name() {
        return heroes_name;
    }

    public void setHeroes_name(String heroes_name) {
        this.heroes_name = heroes_name;
    }

    public int getHeroes_id() {
        return heroes_id;
    }

    public void setHeroes_id(int heroes_id) {
        this.heroes_id = heroes_id;
    }

    public int getHeroes_gold() {
        return heroes_gold;
    }

    public void setHeroes_gold(int heroes_gold) {
        this.heroes_gold = heroes_gold;
    }
}

