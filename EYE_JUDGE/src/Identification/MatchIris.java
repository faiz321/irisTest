/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Identification;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author HP
 */
public class MatchIris {

    private String code;

    public MatchIris(String irisCode) {
        code = irisCode;
    }

    public void connectToDatabase() {
        float HD = 99999;
        System.out.println("Hamming Distance: " + HD);
        Popup p;
        JFrame f = new JFrame("DB SEARCH OUTPUT");

        // create a label
        JLabel l = new JLabel("USER NOT FOUND");

        f.setSize(400, 100);

        PopupFactory pf = new PopupFactory();

        // create a panel
        JPanel p2 = new JPanel();

        // set Background of panel


        p2.add(l);

        // create a popup
        p = pf.getPopup(f, p2, 180, 100);


        // create a panel
        JPanel p1 = new JPanel();

        f.add(p1);
        f.add(p2);
//        f.setVisible(true);

        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection conn;
            String db = "jdbc:mysql://localhost:3306/eyejudge";
            conn = DriverManager.getConnection(db, "root", "root");
            System.out.println("*****connected to database*****");
            Statement st = conn.createStatement();
            String query = "select * from personinfo";
            ResultSet rs = st.executeQuery(query);
            String dbCode = "";
            float count = 0, countPix = 0;
            int userHit = 1;
            while (rs.next()) {
                userHit++;
                dbCode = rs.getString("IrisCode");
                count = 0;
                countPix = 0;
                for (int i = 0; i < code.length(); i++) {
                    if (code.charAt(i) != dbCode.charAt(i)) {
                        count++;
                        System.out.println("DB BAD:" + dbCode.charAt(i));
                    }
                    countPix++;
                    System.out.println("DB GOOD:" + dbCode.charAt(i));
                    System.out.println("IN: " + code.charAt(i));
                }
                HD = count / countPix;


                if (HD < 0.1) {

                    System.out.println(" :) :) USER FOUND " + userHit);
                    p2.setBackground(Color.green);
                    l.setText("USER FOUND " + userHit);

                    break;
                }
            }
            if (HD >= 0.1) {

                System.out.println("USER NOT FOUND ");
                p2.setBackground(Color.red);
                l.setText("USER NOT FOUND ");


            }
            System.out.println("***************");
            System.out.println("count:" + count);
            System.out.println("countPix:" + countPix);
            System.out.println("HD:" + HD);
            System.out.println("^^^^^^^^^^^^^");

            f.setVisible(true);
            st.close();
            conn.close();
        } catch (SQLException ex) {
            Logger.getLogger(MatchIris.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MatchIris.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
