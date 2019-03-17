package team.hnuwt.servicesoftware.synchronizer.dao;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.hnuwt.servicesoftware.synchronizer.model.Login;
import team.hnuwt.servicesoftware.synchronizer.util.DBUtil;

import java.io.IOException;
import java.util.List;

public class LoginDao {

    private static Logger logger = LoggerFactory.getLogger(LoginDao.class);

    public void insert(Login data)
    {
        DBUtil dbUtil = new DBUtil();
        SqlSession sqlSession = null;
        try {
            sqlSession = dbUtil.getSqlSession();
            sqlSession.insert("Login.add", data);
            sqlSession.commit();
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            if (sqlSession != null)
                sqlSession.close();
        }
    }

    public void insertBatch(List<Login> datas)
    {
        DBUtil dbUtil = new DBUtil();
        SqlSession sqlSession = null;
        try {
            sqlSession = dbUtil.getSqlSession();
            sqlSession.insert("Login.addBatch", datas);
            sqlSession.commit();
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            if (sqlSession != null)
                sqlSession.close();
        }
    }
}
