package team.hnuwt.tool;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {

    Properties prop =new Properties();
    String driver=null;
    String url=null;
    String username=null;
    String password=null;

    public Connection openConnection(){

        try {
            prop.load(this.getClass().getClassLoader().getResourceAsStream("team/hnuwt/properties/db.properties"));
            driver=prop.getProperty("jdbc.driver");
            url=prop.getProperty("jdbc.url");
            username=prop.getProperty("jdbc.user");
            password=prop.getProperty("jdbc.password");
            Class.forName(driver);
            return DriverManager.getConnection(url,username,password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void closeConnection(Connection conn){//关闭客户端
        if(conn!=null){
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) throws Exception {
        System.out.println(new DBUtil().openConnection());
    }

}
