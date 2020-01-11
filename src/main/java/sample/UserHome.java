package sample;

import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserHome extends JFrame {
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI(Connection connection,String userName,String password){
        int id=1000;
        try{
        PreparedStatement st = (PreparedStatement) connection
                .prepareStatement("Select id from users where login=? and password=?");
        st.setString(1, userName);
        st.setString(2, password);
        ResultSet rs = st.executeQuery();
            while (rs.next())
            {
                id = rs.getInt("id");
                System.out.println(id);
            }
            //rs = st.executeQuery(""); tutaj skonczyles

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        JFrame frame = new JFrame("Okno Konta");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JLabel emptyLabel = new JLabel("empty"+ id);
        emptyLabel.setPreferredSize(new Dimension(600, 500));
        frame.getContentPane().add(emptyLabel, BorderLayout.CENTER);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
    }

    public UserHome(Connection connection,String userName,String password){
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI(connection,userName,password);
            }
        });
    }

}
