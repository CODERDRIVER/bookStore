package com.lyears.projects.bookstore;

import com.lyears.projects.bookstore.util.BarcodeUtil;
import com.lyears.projects.bookstore.util.DateUtil;
import org.junit.Test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Date;

/**
 * @author fzm
 * @date 2018/5/1
 **/
public class test {
    public static void main(String[] args) {
        String URL = "jdbc:mysql://47.95.253.94:3306/BootJPA?&useUnicode=true&characterEncoding=UTF8&useSSL=false";
        String User = "root";
        String Password = "w1w2W1W2,./";
        Connection conn;
        try {
            // The newInstance() call is a work around for some
            // broken Java implementations

            Class.forName("com.mysql.jdbc.Driver").newInstance();
            conn = DriverManager.getConnection(URL, User, Password);
            System.out.println(conn);

            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery("select * from sssp_department");
            while (rs.next()) {
                System.out.println(rs);
            }
            rs.close();
            st.close();
            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }



    @Test
    public void testBarcode()
    {
        String msg = "123456789";
        BarcodeUtil.generateFile(msg,"test.png");
    }
}
