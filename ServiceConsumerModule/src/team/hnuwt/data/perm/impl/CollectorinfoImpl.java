package team.hnuwt.data.perm.impl;

import team.hnuwt.data.perm.bean.CollectorinfoBean;
import team.hnuwt.data.perm.dao.CollectorinfoDao;
import team.hnuwt.tool.DBUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;

public class CollectorinfoImpl implements CollectorinfoDao {

    @Override
    public void insertOrUpdateCollectorinfo(CollectorinfoBean collectorinfoBean) {
        String sql = "replace into " +
                "tb_collectorinfo(CollectorID, CollectorType, CreateTime) " +
                "values(?,?,?)" ;
        DBUtil dbUtil = new DBUtil();
        Connection conn = dbUtil.openConnection();

        try {
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, collectorinfoBean.getCollectorID());
            ptmt.setString(2, collectorinfoBean.getCollectorType());
            ptmt.setTimestamp(3, new Timestamp(new Date().getTime()));
            ptmt.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            dbUtil.closeConnection(conn);
        }
    }


}
