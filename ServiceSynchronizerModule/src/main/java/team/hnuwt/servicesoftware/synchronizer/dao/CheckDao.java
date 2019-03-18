package team.hnuwt.servicesoftware.synchronizer.dao;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.synchronizer.handler.CheckOfflineHandler;
import team.hnuwt.servicesoftware.synchronizer.model.Login;
import team.hnuwt.servicesoftware.synchronizer.util.DBUtil;

import java.io.IOException;
import java.util.List;

public class CheckDao {

    private static Logger logger = LoggerFactory.getLogger(CheckDao.class);

    public void check(Login data)
    {
        DBUtil dbUtil = new DBUtil();
        SqlSession sqlSession = null;
        try {
            sqlSession = dbUtil.getSqlSession();
            sqlSession.select("check.offline", new CheckOfflineHandler());
            sqlSession.commit();
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            if (sqlSession != null)
                sqlSession.close();
        }
    }
}
