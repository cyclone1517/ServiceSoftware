package team.hnuwt.tool;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class TmpDBUtil {

    private static String url;
    private static String user;
    private static String pwd;
    private static String driver;

    static {
        try {
            url = "jdbc:mysql://localhost:3306/servicesoftware?serverTimezone=UTC";
            user = "root";
            pwd = "root";
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        try{
            Connection conn = DriverManager.getConnection(url,user,pwd);
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void closeConnection(Connection conn) {
        try {
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(getConnection());
    }

}
