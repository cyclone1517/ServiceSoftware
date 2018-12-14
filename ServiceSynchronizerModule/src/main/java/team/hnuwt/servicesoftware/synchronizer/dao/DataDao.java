package team.hnuwt.servicesoftware.synchronizer.dao;

import java.io.IOException;
import java.util.List;

import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import team.hnuwt.servicesoftware.synchronizer.model.Data;
import team.hnuwt.servicesoftware.synchronizer.util.DBUtil;

public class DataDao {

    private static Logger logger = LoggerFactory.getLogger(DataDao.class);

    public void insert(Data data)
    {
        DBUtil dbUtil = new DBUtil();
        SqlSession sqlSession = null;
        try {
            sqlSession = dbUtil.getSqlSession();
            sqlSession.insert("Data.add", data);
            sqlSession.commit();
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            if (sqlSession != null)
                sqlSession.close();
        }
    }

    public void insertBatch(List<Data> datas)
    {
        DBUtil dbUtil = new DBUtil();
        SqlSession sqlSession = null;
        try {
            sqlSession = dbUtil.getSqlSession();
            sqlSession.insert("Data.addBatch", datas);
            sqlSession.commit();
        } catch (IOException e) {
            logger.error("", e);
        } finally {
            if (sqlSession != null)
                sqlSession.close();
        }
    }
}
